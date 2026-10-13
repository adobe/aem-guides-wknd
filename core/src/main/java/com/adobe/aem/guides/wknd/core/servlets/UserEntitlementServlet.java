package com.adobe.aem.guides.wknd.core.servlets;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.RepositoryException;
import javax.jcr.Session;
import javax.servlet.Servlet;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Pattern;
import com.google.gson.stream.JsonWriter;

/**
 * Servlet that returns content fragments accessible by a specific user.
 * Uses impersonation so the query itself is ACL-filtered — only assets
 * the target user can read are returned.
 * Usage: /bin/jigsaw/user-entitlement?email=xxx@example.com
 */
@Component(service = Servlet.class)
@SlingServletPaths("/bin/jigsaw/user-entitlement")
@ServiceDescription("User Entitlement Servlet - Returns accessible content fragments for a user by email")
public class UserEntitlementServlet extends SlingSafeMethodsServlet {

    private static final Logger log = LoggerFactory.getLogger(UserEntitlementServlet.class);
    private static final long serialVersionUID = 1L;

    // ==================== Error Codes ====================
    private static final String ERROR_INVALID_REQUEST = "INVALID_REQUEST";
    private static final String ERROR_USER_NOT_FOUND = "USER_NOT_FOUND";
    private static final String ERROR_USER_NOT_AUTHORIZED = "USER_NOT_AUTHORIZED";
    private static final String ERROR_SERVICE_ERROR = "SERVICE_ERROR";

    // ==================== Error Messages ====================
    private static final String MSG_INVALID_EMAIL = "The email address is missing or not in a valid format";
    private static final String MSG_SESSION_ERROR = "Unable to establish a secure session";
    private static final String MSG_USER_NOT_FOUND = "No account exists with the provided email address";
    private static final String MSG_SERVICE_UNAVAILABLE = "The entitlement service is temporarily unavailable";
    private static final String MSG_CONTENT_RETRIEVAL_ERROR = "Unable to retrieve content information";
    private static final String MSG_PERMISSION_ERROR = "Unable to verify user permissions";

    /** Service username mapped in the Sling Service User Mapper configuration. */
    private static final String SUBSERVICE = "jigsawServiceUser";

    /** Query parameter name used to pass the user's email address. */
    private static final String PARAM_EMAIL = "email";

    /** Date format pattern used when serializing {@link Calendar} values to JSON. */
    private static final String DATE_FORMAT = "EEE MMM dd yyyy HH:mm:ss 'GMT'Z";

    /** Compact Gson instance for error responses — Gson is thread-safe and reusable. */
    private static final Gson GSON = new Gson();

    /** Factory used to obtain service-level and impersonated resource resolvers. */
    @Reference
    private transient ResourceResolverFactory resolverFactory;

    /** AEM QueryBuilder service used to execute JCR queries for DAM assets. */
    @Reference
    private transient QueryBuilder queryBuilder;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$");

    /**
     * Handles GET requests by resolving the user from the provided email,
     * impersonating that user, and returning all accessible DAM assets as JSON.
     *
     * @param request  the Sling HTTP request (expects query param {@code email})
     * @param response the Sling HTTP response (JSON output)
     * @throws IOException if writing to the response fails
     */
    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String userEmail = request.getParameter(PARAM_EMAIL);

        if (!isValidEmail(userEmail)) {
            sendError(response, HttpServletResponse.SC_BAD_REQUEST,
                    ERROR_INVALID_REQUEST, MSG_INVALID_EMAIL);
            return;
        }

        try (ResourceResolver serviceResolver = getServiceResolver()) {

            // Step 1: Look up userId by email
            JackrabbitSession serviceSession = getJackrabbitSession(serviceResolver);
            if (serviceSession == null) {
                sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        ERROR_SERVICE_ERROR, MSG_SESSION_ERROR);
                return;
            }

            UserManager userManager = serviceSession.getUserManager();
            String userId = findUserIdByEmail(userManager, userEmail);
            if (userId == null) {
                sendError(response, HttpServletResponse.SC_BAD_REQUEST,
                        ERROR_USER_NOT_FOUND, MSG_USER_NOT_FOUND);
                return;
            }

            // Step 2: Impersonate and query
            queryAndRespond(serviceResolver, userId, response);

        } catch (LoginException e) {
            log.error("Service login failed", e);
            sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    ERROR_SERVICE_ERROR, MSG_SERVICE_UNAVAILABLE);
        } catch (RepositoryException e) {
            log.error("Repository error while checking entitlements", e);
            sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    ERROR_SERVICE_ERROR, MSG_CONTENT_RETRIEVAL_ERROR);
        }
    }

    private boolean isValidEmail(String email) {
        return email != null && !email.trim().isEmpty()
                && EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * Impersonates the given user, queries for content fragments, and streams the JSON response.
     *
     * @param serviceResolver the service-level resource resolver
     * @param userId          the user ID to impersonate
     * @param response        the HTTP response to write to
     * @throws IOException if writing to the response fails
     */
    private void queryAndRespond(ResourceResolver serviceResolver, String userId,
                                SlingHttpServletResponse response) throws IOException {
        try (ResourceResolver impersonatedResolver = serviceResolver.clone(
                Collections.singletonMap(ResourceResolverFactory.USER_IMPERSONATION, userId))) {

            Session impersonatedSession = impersonatedResolver.adaptTo(Session.class);
            if (impersonatedSession == null) {
                sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        ERROR_SERVICE_ERROR, MSG_PERMISSION_ERROR);
                return;
            }

            SearchResult searchResult = queryAssets(impersonatedSession);
            streamSuccessResponse(response, searchResult, impersonatedResolver);

        } catch (LoginException e) {
            log.error("Failed to impersonate user '{}'. Ensure the service user has "
                    + "jcr:impersonate privilege.", userId, e);
            sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    ERROR_SERVICE_ERROR, MSG_PERMISSION_ERROR);
        } catch (RuntimeException e) {
            log.error("Query failed for user '{}'.", userId, e);
            sendError(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    ERROR_SERVICE_ERROR, MSG_CONTENT_RETRIEVAL_ERROR);
        }
    }

    /**
     * Streams assets directly to the response as they're processed,
     * avoiding holding all results in memory simultaneously.
     */
    private void streamSuccessResponse(SlingHttpServletResponse response,
                                       SearchResult searchResult,
                                       ResourceResolver resolver) throws IOException {

        response.setStatus(HttpServletResponse.SC_OK);

        try (JsonWriter writer = new JsonWriter(response.getWriter())) {
            writer.setIndent("  "); // Pretty print (remove for compact output)

            writer.beginObject();
            writer.name("success").value(true);
            writer.name("total").value(searchResult.getTotalMatches());
            writer.name("more").value(searchResult.hasMore());

            // Stream hits array
            writer.name("hits");
            writer.beginArray();

            int count = 0;
            int errorCount = 0;

            for (Hit hit : searchResult.getHits()) {
                try {
                    String path = hit.getPath();
                    Resource assetResource = resolver.getResource(path);

                    if (assetResource != null) {
                        Map<String, Object> assetData = buildAssetData(assetResource, path);
                        GSON.toJson(assetData, Map.class, writer);
                        count++;
                    }
                } catch (RepositoryException e) {
                    errorCount++;
                    if (errorCount <= 5) {
                        log.warn("Error processing hit: {}", e.getMessage());
                    }
                }
            }

            writer.endArray();

            // Write count after streaming (we now know the actual count)
            writer.name("results").value(count);

            if (errorCount > 0) {
                writer.name("errors").value(errorCount);
            }

            writer.endObject();
        }
    }

    /**
     * Adapts the given resolver's session to a {@link JackrabbitSession}.
     *
     * @param resolver the resource resolver to adapt
     * @return the JackrabbitSession, or {@code null} if the session is not a Jackrabbit implementation
     */
    private JackrabbitSession getJackrabbitSession(ResourceResolver resolver) {
        Session session = resolver.adaptTo(Session.class);
        if (session instanceof JackrabbitSession) {
            return (JackrabbitSession) session;
        }
        log.error("Session is not a JackrabbitSession");
        return null;
    }

    /**
     * Looks up a user's JCR ID by searching the {@code profile/email} property.
     *
     * @param userManager the Jackrabbit user manager
     * @param email       the email address to search for
     * @return the user ID, or {@code null} if no matching user is found
     * @throws RepositoryException if the user query fails
     */
    private String findUserIdByEmail(UserManager userManager, String email)
            throws RepositoryException {
        Iterator<Authorizable> it = userManager.findAuthorizables(
                "profile/email", email, UserManager.SEARCH_TYPE_USER);
        if (it.hasNext()) {
            return it.next().getID();
        }
        return null;
    }

    /**
     * Executes a QueryBuilder search for all {@code dam:Asset} nodes under
     * {@code /content/dam/jigsaw} using the provided session.
     *
     * @param session the JCR session (determines ACL-filtered results)
     * @return the search result containing matching asset hits
     */
    private SearchResult queryAssets(Session session) {
            Map<String, String> queryMap = Map.of(
                "type", "dam:Asset",
                "path", "/content/dam/jigsaw",
                "p.limit", "-1",
                "p.guessTotal", "true"
        );
        return queryBuilder.createQuery(PredicateGroup.create(queryMap), session).getResult();
    }

    /**
     * Builds a map representing a single asset, including its properties
     * and the full {@code jcr:content} subtree.
     *
     * @param assetResource the Sling resource for the DAM asset
     * @param path          the JCR path of the asset
     * @return an ordered map of the asset's data suitable for JSON serialization
     */
    private Map<String, Object> buildAssetData(Resource assetResource, String path) {
        Map<String, Object> assetData = new LinkedHashMap<>();
        assetData.put("jcr:path", path);
        addAllProperties(assetResource, assetData);

        Resource jcrContent = assetResource.getChild("jcr:content");
        if (jcrContent != null) {
            assetData.put("jcr:content", buildNodeMap(jcrContent));
        }
        return assetData;
    }

    /**
     * Copies all serializable properties from a resource's {@link ValueMap} into the target map.
     *
     * @param resource the Sling resource whose properties are read
     * @param map      the destination map to populate
     */
    private void addAllProperties(Resource resource, Map<String, Object> map) {
        if (resource == null) {
            return;
        }
        ValueMap props = resource.getValueMap();
        for (Map.Entry<String, Object> entry : props.entrySet()) {
            Object serializable = toSerializable(entry.getValue());
            if (serializable != null) {
                map.put(entry.getKey(), serializable);
            }
        }
    }

    /**
     * Recursively converts a resource and all its children into a nested map structure.
     *
     * @param resource the root resource to traverse
     * @return an ordered map representing the node tree
     */
    private Map<String, Object> buildNodeMap(Resource resource) {
        Map<String, Object> map = new LinkedHashMap<>();
        addAllProperties(resource, map);
        for (Resource child : resource.getChildren()) {
            map.put(child.getName(), buildNodeMap(child));
        }
        return map;
    }

    /**
     * Formats a {@link Calendar} value into a human-readable UTC date string.
     *
     * @param cal the calendar instance to format
     * @return the formatted date string, or {@code null} if {@code cal} is null
     */
    private String formatDate(Calendar cal) {
        if (cal == null) {
            return null;
        }
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(cal.getTime());
    }

    /**
     * Writes a JSON error response with the given HTTP status code, error code, and message.
     *
     * @param response  the HTTP response to write to
     * @param status    the HTTP status code (e.g. 400, 403, 500)
     * @param errorCode the application-specific error code (e.g. INVALID_REQUEST)
     * @param message   the error message included in the JSON body
     * @throws IOException if writing to the response output stream fails
     */
    private void sendError(SlingHttpServletResponse response, int status, 
                          String errorCode, String message) throws IOException {
        response.setStatus(status);
        JsonObject json = new JsonObject();
        json.addProperty("error", message);
        json.addProperty("errorCode", errorCode);
        response.getWriter().write(json.toString());
    }

    /**
     * Converts a JCR property value into a JSON-safe type.
     * Handles primitives, strings, numbers, booleans, calendars, and arrays
     * (including primitive arrays via reflection).
     *
     * @param value the raw JCR property value; may be {@code null}
     * @return JSON-serializable representation, or {@code null} if unsupported
     */
    private Object toSerializable(Object value) {
        if (value == null) {
            return null;
        }
        if (value instanceof Calendar) {
            return formatDate((Calendar) value);
        }
        if (value instanceof String || value instanceof Number || value instanceof Boolean) {
            return value;
        }
        if (value instanceof String[]) {
            return value;
        }
        if (value.getClass().isArray()) {
            int length = java.lang.reflect.Array.getLength(value);
            List<Object> list = new ArrayList<>(length);
            for (int i = 0; i < length; i++) {
                list.add(java.lang.reflect.Array.get(value, i));
            }
            return list;
        }
        return null;
    }

    /**
     * Obtains a service-level {@link ResourceResolver} using the configured sub-service name.
     *
     * @return the service resource resolver
     * @throws LoginException if the service user mapping is missing or login fails
     */
    private ResourceResolver getServiceResolver() throws LoginException {
        return resolverFactory.getServiceResourceResolver(
                Collections.singletonMap(ResourceResolverFactory.SUBSERVICE, SUBSERVICE));
    }
}
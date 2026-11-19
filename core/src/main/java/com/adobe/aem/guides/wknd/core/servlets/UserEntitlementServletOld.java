package com.adobe.aem.guides.wknd.core.servlets;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.apache.jackrabbit.api.JackrabbitSession;
import org.apache.jackrabbit.api.security.JackrabbitAccessControlManager;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.Group;
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
import javax.jcr.security.Privilege;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimeZone;

/**
 * Servlet that returns content fragments accessible by a specific user.
 * Checks ACL permissions and returns full asset metadata in JSON format.
 * 
 * Usage:
 * /bin/jigsaw/user-entitlement?email=xxx@example.com&rootPath=/content/dam/xxx
 */
@Component(service = Servlet.class)
@SlingServletPaths("/bin/jigsaw/user-entitlement-old")
@ServiceDescription("User Entitlement Servlet Old- Returns accessible content fragments for a user by email")
public class UserEntitlementServletOld extends SlingSafeMethodsServlet {

    private static final Logger log = LoggerFactory.getLogger(UserEntitlementServletOld.class);
    private static final long serialVersionUID = 1L;
    private static final String SUBSERVICE = "jigsawServiceUser";

    private static final String PARAM_EMAIL = "email";
    private static final String PARAM_ROOT_PATH = "path";
    private static final String DATE_FORMAT = "EEE MMM dd yyyy HH:mm:ss 'GMT'Z";

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Reference
    private QueryBuilder queryBuilder;

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String userEmail = request.getParameter(PARAM_EMAIL);

        // Validate required parameters
        if (isBlank(userEmail)) {
            sendError(response, SlingHttpServletResponse.SC_BAD_REQUEST,
                    "Missing required parameters: email");
            return;
        }

        try (ResourceResolver serviceResolver = getServiceResolver()) {

            // Get JackrabbitSession for UserManager access
            JackrabbitSession jackrabbitSession = getJackrabbitSession(serviceResolver);
            if (jackrabbitSession == null) {
                sendError(response, SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                        "Could not obtain JackrabbitSession");
                return;
            }

            // Find userId by email
            UserManager userManager = jackrabbitSession.getUserManager();
            String userId = findUserIdByEmail(userManager, userEmail);
            if (userId == null) {
                sendError(response, SlingHttpServletResponse.SC_BAD_REQUEST,
                        "User not found with email: " + userEmail);
                return;
            }

            // Get user's principals (user + all groups)
            Set<Principal> principals = getUserPrincipals(userManager, userId);
            if (principals == null || principals.isEmpty()) {
                sendError(response, SlingHttpServletResponse.SC_BAD_REQUEST,
                        "Could not retrieve principals for user: " + userId);
                return;
            }

            // Query all assets
            SearchResult searchResult = queryContentFragments(jackrabbitSession);

            // Filter by ACL and build response
            List<Map<String, Object>> accessibleAssets = filterByPermissions(
                    searchResult, jackrabbitSession, principals);

            // Build and send response
            sendSuccessResponse(response, accessibleAssets, searchResult);

        } catch (LoginException e) {
            log.error("Service login failed", e);
            sendError(response, SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Service login failed");
        } catch (RepositoryException e) {
            log.error("Repository error while checking entitlements", e);
            sendError(response, SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR,
                    "Repository error while checking entitlements");
        }
    }

    /**
     * Gets JackrabbitSession from ResourceResolver.
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
     * Finds user ID by email address.
     */
    private String findUserIdByEmail(UserManager userManager, String email) throws RepositoryException {
        Iterator<Authorizable> it = userManager.findAuthorizables(
                "profile/email",
                email,
                UserManager.SEARCH_TYPE_USER);

        if (it.hasNext()) {
            return it.next().getID();
        }
        return null;
    }

    /**
     * Retrieves user principal and all group principals for ACL checking.
     */
    private Set<Principal> getUserPrincipals(UserManager userManager, String userId)
            throws RepositoryException {

        Authorizable authorizable = userManager.getAuthorizable(userId);
        if (authorizable == null) {
            return null;
        }

        Set<Principal> principals = new HashSet<>();
        principals.add(authorizable.getPrincipal());

        Iterator<Group> groups = authorizable.memberOf();
        while (groups.hasNext()) {
            principals.add(groups.next().getPrincipal());
        }

        return principals;
    }

    /**
     * Queries all content fragments under the specified root path.
     */
    private SearchResult queryContentFragments(Session session) {
        Map<String, String> queryMap = new HashMap<>();
        queryMap.put("type", "dam:Asset");
        queryMap.put("path", "/content/dam/jigsaw");
        queryMap.put("p.limit", "-1");
        queryMap.put("p.guessTotal", "true");

        return queryBuilder.createQuery(PredicateGroup.create(queryMap), session).getResult();
    }

    /**
     * Filters search results based on user's read permissions.
     */
    private List<Map<String, Object>> filterByPermissions(SearchResult searchResult,
            Session session, Set<Principal> principals) throws RepositoryException {

        JackrabbitAccessControlManager acm = (JackrabbitAccessControlManager) session.getAccessControlManager();
        Privilege[] readPrivilege = new Privilege[] {
                acm.privilegeFromName(Privilege.JCR_READ)
        };

        List<Map<String, Object>> accessibleAssets = new ArrayList<>();

        for (Hit hit : searchResult.getHits()) {
            try {
                String path = hit.getPath();

                if (acm.hasPrivileges(path, principals, readPrivilege)) {
                    Resource assetResource = hit.getResource();
                    if (assetResource != null) {
                        accessibleAssets.add(buildAssetData(assetResource, path));
                    }
                }
            } catch (RepositoryException e) {
                log.warn("Error processing hit at {}: {}", hit.getPath(), e.getMessage());
            }
        }

        return accessibleAssets;
    }

    /**
     * Builds complete asset data map including all properties and child nodes.
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
     * Adds all properties from a resource to the map.
     * Only includes JSON-serializable values.
     */
    private void addAllProperties(Resource resource, Map<String, Object> map) {
        if (resource == null) {
            return;
        }

        ValueMap props = resource.getValueMap();
        for (String key : props.keySet()) {
            Object value = props.get(key);
            Object serializable = toSerializable(value);
            if (serializable != null) {
                map.put(key, serializable);
            }
        }
    }

    /**
     * Recursively builds a map of all properties and child nodes.
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
     * Formats calendar to readable date string.
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
     * Sends successful JSON response.
     */
    private void sendSuccessResponse(SlingHttpServletResponse response,
            List<Map<String, Object>> assets, SearchResult searchResult) throws IOException {

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("success", true);
        result.put("results", assets.size());
        result.put("total", searchResult.getTotalMatches());
        result.put("more", searchResult.hasMore());
        result.put("offset", 0);
        result.put("hits", assets);

        response.setStatus(SlingHttpServletResponse.SC_OK);
        response.getWriter().write(new GsonBuilder().setPrettyPrinting().create().toJson(result));
    }

    /**
     * Sends JSON error response.
     */
    private void sendError(SlingHttpServletResponse response, int status, String message)
            throws IOException {
        response.setStatus(status);
        response.getWriter().write(new Gson().toJson(Map.of("error", message)));
    }

    /**
     * Checks if string is null or blank.
     */
    private boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }

    /**
     * Converts value to JSON-serializable type.
     */
    private Object toSerializable(Object value) {
        if (value == null) {
            return null;
        }

        if (value instanceof Calendar) {
            return formatDate((Calendar) value);
        }

        if (value instanceof String || value instanceof Number ||
                value instanceof Boolean || value instanceof String[]) {
            return value;
        }

        // Convert other arrays to string representation
        if (value.getClass().isArray()) {
            return Arrays.toString((Object[]) value);
        }

        return null;
    }

    /**
     * Gets service resource resolver for elevated access.
     */
    private ResourceResolver getServiceResolver() throws LoginException {
        Map<String, Object> params = new HashMap<>();
        params.put(ResourceResolverFactory.SUBSERVICE, SUBSERVICE);
        return resolverFactory.getServiceResourceResolver(params);
    }
}

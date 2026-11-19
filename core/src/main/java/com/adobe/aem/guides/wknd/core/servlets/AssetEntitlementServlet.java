package com.adobe.aem.guides.wknd.core.servlets;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.google.gson.Gson;
import org.apache.jackrabbit.api.security.JackrabbitAccessControlManager;
import org.apache.jackrabbit.api.security.user.Authorizable;
import org.apache.jackrabbit.api.security.user.UserManager;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.LoginException;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.apache.sling.servlets.annotations.SlingServletPaths;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceDescription;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javax.jcr.Session;
import javax.jcr.SimpleCredentials;
import javax.jcr.security.AccessControlManager;
import javax.jcr.security.Privilege;
import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.security.Principal;
import java.util.*;
import org.apache.jackrabbit.api.JackrabbitSession;

@Component(service = Servlet.class)
@SlingServletPaths("/bin/asset-entitlements")
@ServiceDescription("Asset Entitlement Servlet")
public class AssetEntitlementServlet extends SlingSafeMethodsServlet {

    private static final Logger log = LoggerFactory.getLogger(AssetEntitlementServlet.class);
    private static final long serialVersionUID = 1L;
    private static final String SUBSERVICE = "jigsawServiceUser";

    @Reference
    private ResourceResolverFactory resolverFactory;

    @Reference
    private QueryBuilder queryBuilder;

private String findUserIdByEmail(UserManager userManager, String email) throws Exception {
    // Search for users with matching email in profile/email property
    Iterator<Authorizable> it = userManager.findAuthorizables(
        "profile/email", 
        email, 
        UserManager.SEARCH_TYPE_USER  // Add this to search only users, not groups
    );
    
    if (it.hasNext()) {
        Authorizable auth = it.next();
        return auth.getID();
    }
    return null;
}

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        String userId = request.getParameter("userId");
        String rootPath = request.getParameter("rootPath");

        if (userId == null || rootPath == null) {
            response.setStatus(SlingHttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\":\"Missing required parameters: userId, rootPath\"}");
            return;
        }

        try (ResourceResolver serviceResolver = getServiceResolver()) {

            Session adminSession = serviceResolver.adaptTo(Session.class);
            if (adminSession == null) {
                response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\":\"Cannot adapt to JCR Session\"}");
                return;
            }

            // Cast to JackrabbitSession to access getUserManager()
            if (!(adminSession instanceof JackrabbitSession)) {
                response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\":\"Session is not a JackrabbitSession\"}");
                return;
            }

            JackrabbitSession jackrabbitSession = (JackrabbitSession) adminSession;
            UserManager userManager = jackrabbitSession.getUserManager();

            String id = findUserIdByEmail(userManager, userId);
            if (id == null) {
                response.setStatus(SlingHttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"error\":\"No user found for email\"}");
                return;
            }

            Map<String, Object> userAuthInfo = new HashMap<>();
            userAuthInfo.put(ResourceResolverFactory.USER, userId);

            try (ResourceResolver userResolver = resolverFactory.getResourceResolver(userAuthInfo)) {
                Session userSession = userResolver.adaptTo(Session.class);

                if (userSession == null || !userSession.isLive()) {
                    response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    response.getWriter().write("{\"error\":\"Cannot obtain user session\"}");
                    return;
                }

                Map<String, String> map = new HashMap<>();
                map.put("type", "dam:Asset");
                map.put("path", rootPath);
                map.put("1_property", "jcr:content/contentFragment");
                map.put("1_property.value", "true");
                map.put("p.limit", "-1");
                map.put("p.guessTotal", "true");

                SearchResult searchResult = queryBuilder
                        .createQuery(PredicateGroup.create(map), userSession)
                        .getResult();

                List<String> paths = new ArrayList<>();

                // JsonArray assetsArray = new JsonArray();
                for (Hit hit : searchResult.getHits()) {
                    paths.add(hit.getPath());
                }

                Map<String, Object> result = new LinkedHashMap<>();
                result.put("userId", userId);
                result.put("rootPath", rootPath);
                result.put("count", paths.size());
                result.put("assets", paths);

                response.setStatus(SlingHttpServletResponse.SC_OK);
                response.getWriter().write(new Gson().toJson(result));
            }
        } catch (LoginException le) {
            log.error("LoginException while obtaining service resolver: {}", le.getMessage(), le);
            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"LoginException: " + le.getMessage() + "\"}");
        } catch (Exception e) {
            response.setStatus(SlingHttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    private ResourceResolver getServiceResolver() throws LoginException {
        Map<String, Object> params = new HashMap<>();
        params.put(ResourceResolverFactory.SUBSERVICE, SUBSERVICE);
        return resolverFactory.getServiceResourceResolver(params);
    }
}
package com.adobe.aem.guides.wknd.core.servlet;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletException;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingSafeMethodsServlet;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import com.example.services.HelloWorldService;

@Component(service = Servlet.class, property = {
    ServletResolverConstants.SLING_SERVLET_PATHS + "=/bin/sayhello",
    ServletResolverConstants.SLING_SERVLET_METHODS + "=" + HttpConstants.METHOD_GET
})
public class SayHello extends SlingSafeMethodsServlet {
    
        private static final long serialVersionUID = 1L;

        // Injecting the HelloWorldService from the `my-example-bundle` bundle
        @Reference
        private HelloWorldService helloWorldService;
    
        @Override
        protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
            // Invoking the HelloWorldService's `sayHello` method
            response.getWriter().write("My-Example-Bundle service says: " + helloWorldService.sayHello());
        }
}
package com.adobe.aem.guides.wknd.core.components;

import org.apache.sling.api.resource.Resource;

import javax.jcr.RepositoryException;

public interface ListItemImage {
    Resource getResource() throws RepositoryException;
}

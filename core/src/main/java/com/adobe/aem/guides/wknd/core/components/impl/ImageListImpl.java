package com.adobe.aem.guides.wknd.core.components.impl;

import com.adobe.aem.guides.wknd.core.components.ImageList;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.via.ResourceSuperType;
import org.apache.sling.models.factory.ModelFactory;

@Model(
        adaptables = {SlingHttpServletRequest.class},
        adapters = {ImageList.class},
        resourceType = {ImageListImpl.RESOURCE_TYPE},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ImageListImpl implements ImageList {
    protected static final String RESOURCE_TYPE = "wknd/components/content/image-list";

    @Self
    private SlingHttpServletRequest request;

    @OSGiService
    private ModelFactory modelFactory;

    @Self
    @Via(type = ResourceSuperType.class)
    private String coreList;
}

/*
 *  Copyright 2019 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.adobe.aem.guides.wknd.core.models.impl;

import com.adobe.aem.guides.wknd.core.models.ImageList;
import com.adobe.cq.wcm.core.components.models.Image;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.search.Predicate;
import com.day.cq.search.PredicateConverter;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.eval.JcrPropertyPredicateEvaluator;
import com.day.cq.search.eval.PathPredicateEvaluator;
import com.day.cq.search.eval.TypePredicateEvaluator;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceWrapper;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.jcr.resource.api.JcrResourceConstants;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Required;
import org.apache.sling.models.annotations.Via;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.via.ResourceSuperType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jcr.Session;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Model(
        adaptables = {SlingHttpServletRequest.class},
        adapters = {ImageList.class},
        resourceType = {ImageListImpl.RESOURCE_TYPE},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ImageListImpl implements ImageList {
    private static final Logger log = LoggerFactory.getLogger(ImageListImpl.class);

    protected static final String RESOURCE_TYPE = "wknd/components/content/image-list";

    @Self
    @Required
    private SlingHttpServletRequest request;

    @OSGiService
    @Required
    private QueryBuilder queryBuilder;

    /**
     * The Image List Component extends the AEM WCM Core Component.
     * This injection creates an instance of the Core Components List component, and allows its methods to be called by
     * the WKND Image List component, following the delegation pattern.
     *
     * Note this is made @Optional to allow for Unit Testing of this Sling Model.
     */
    @Self
    @Via(type = ResourceSuperType.class)
    private com.adobe.cq.wcm.core.components.models.List coreList;

    private List<ImageList.ListItem> imageListItems;

    @Override
    public final Collection<ImageList.ListItem> getListItems() {
        if (imageListItems == null) {
            if (coreList == null) {
                log.warn("Could not locate the AEM WCM Core Components List SlingModel via this component's ResourceSuperType. Returning an empty list.");
                imageListItems = Collections.EMPTY_LIST;
            } else {
                // Calls the AEM WCM Core Components List component's `getListItems()` methods, transforms them into ImageListItem objects.
                imageListItems = coreList.getListItems().stream()
                        .map(listItem -> new ImageListItemImpl(request.getResourceResolver(), listItem))
                        .filter(imageListItem -> !imageListItem.isEmpty())
                        .collect(Collectors.toList());
            }
        }

        return ImmutableList.copyOf(imageListItems);
    }

    @Override
    public final boolean isEmpty() {
        return getListItems().isEmpty();
    }

    private class ImageListItemImpl implements ImageList.ListItem {
        private static final String IMAGE_RESOURCE_TYPE = "wknd/components/content/image";

        private final com.adobe.cq.wcm.core.components.models.ListItem wrappedListItem;
        private final Resource image;
        private final Page page;

        public ImageListItemImpl(final ResourceResolver resourceResolver,
                                 final com.adobe.cq.wcm.core.components.models.ListItem listItem) {

            final PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
            this.wrappedListItem = listItem;
            this.page = pageManager.getContainingPage(wrappedListItem.getPath());          

            image = findPageComponentResources(this.page, IMAGE_RESOURCE_TYPE, 1).stream()
                    .map(r -> new SimpleImageComponentResource(r, getTitle()))
                    .findFirst()
                    .orElse(null);
        }

        public final Resource getImage() {
            return image;
        }

        @Override
        public String getTitle() {
            return wrappedListItem.getTitle();
        }

        @Override
        public String getDescription() {
            return this.page.getProperties().get("shortDescription", this.page.getDescription());
        }

        @Override
        public String getURL() {
            return wrappedListItem.getURL();
        }

        public boolean isEmpty() {
            return getImage() == null;
        }
    }

    /**
     * Helper method that searches an AEM Page for 1 or more resources that are of a specified sling:resourceType.
     *
     * Note the order is by JCR Path, ascending.
     *
     * @param page the AEM Page to search
     * @param slingResourceType The sling:resourceType too look for
     * @param limit the max number of resources to return
     * @return A list of resources that have a match sling:resourceType value
     */
    protected java.util.List<Resource> findPageComponentResources(final Page page, final String slingResourceType, int limit) {
        final java.util.List<Resource> componentResources = new ArrayList<>();

        if (page == null) {
            // If page is null, there is no where to search
            return componentResources;
        }

        final Map<String, String> params = ImmutableMap.<String, String>builder().
                put(PathPredicateEvaluator.PATH, page.getContentResource().getPath()).
                put(TypePredicateEvaluator.TYPE, JcrConstants.NT_UNSTRUCTURED).
                put(JcrPropertyPredicateEvaluator.PROPERTY, JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY).
                put(JcrPropertyPredicateEvaluator.PROPERTY + "." + JcrPropertyPredicateEvaluator.VALUE, slingResourceType).
                put(PredicateConverter.GROUP_PARAMETER_PREFIX + "." + PredicateGroup.PARAM_LIMIT, String.valueOf(limit)).
                put(PredicateConverter.GROUP_PARAMETER_PREFIX + "." +  PredicateGroup.PARAM_GUESS_TOTAL, "true").
                put(Predicate.ORDER_BY, "@jcr:path").
                put(Predicate.ORDER_BY + "." + Predicate.PARAM_SORT , Predicate.SORT_ASCENDING).
                build();

        final long start = System.currentTimeMillis();

        final Iterator<Resource> resources = queryBuilder.createQuery(PredicateGroup.create(params),
                request.getResourceResolver().adaptTo(Session.class)).getResult().getResources();

        // Handle QueryBuilder's leakingResourceResolver; Make sure to close it manually.
        ResourceResolver leakingResourceResolver = null;

        while(resources.hasNext()) {
            final Resource resource = resources.next();

            if (leakingResourceResolver == null) {
                leakingResourceResolver = resource.getResourceResolver();
            }

            componentResources.add(request.getResourceResolver().getResource(resource.getPath()));
        }

        if (leakingResourceResolver != null) {
            leakingResourceResolver.close();
        }

        log.debug("Query searching for component of type [ {} ] over [ {} ] took [ {} ms ]", slingResourceType, page.getContentResource().getPath(), System.currentTimeMillis() - start);

        return componentResources;
    }

    /**
     * ResourceWrapper which is used to include an Image Component content resource, and ensure it's authored configuration does not conflict with the desired renditioning for the Image List component.
     * The Image List component should ONLY display the image without captions, titles, etc.
     *
     * Note that this resource wrapper will not effect the Image Component's style, which is dictated at the policy level.
     */
    protected static class SimpleImageComponentResource extends ResourceWrapper {
        private static final String PN_FILE_REFERENCE = "fileReference";
        private static final String PN_ALT = "alt";

        private ValueMap properties = new ValueMapDecorator(new HashMap<>());

        public SimpleImageComponentResource(final Resource resource, final String alt) {
            super(resource);

            // Expose the original fileReference
            properties.put(PN_FILE_REFERENCE,
                    resource.getValueMap().get(PN_FILE_REFERENCE));

            // Override the decorative configuration attributes
            properties.put(PN_ALT, alt);
            properties.put(Image.PN_IS_DECORATIVE, false);
            properties.put(Image.PN_DISPLAY_POPUP_TITLE, false);
            properties.put(Image.PN_TITLE_VALUE_FROM_DAM, false);
        }

        @Override
        public ValueMap getValueMap() {
            return properties;
        }

        @Override
        public <AdapterType> AdapterType adaptTo(Class<AdapterType> type) {
            if (type != ValueMap.class) {
                return super.adaptTo(type);
            }

            return (AdapterType) getValueMap();
        }
    }
}
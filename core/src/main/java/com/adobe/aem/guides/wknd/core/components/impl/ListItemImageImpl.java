package com.adobe.aem.guides.wknd.core.components.impl;

import com.adobe.aem.guides.wknd.core.components.ListItemImage;
import com.adobe.cq.wcm.core.components.models.Image;
import com.day.cq.commons.jcr.JcrConstants;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.eval.JcrPropertyPredicateEvaluator;
import com.day.cq.search.eval.PathPredicateEvaluator;
import com.day.cq.search.eval.TypePredicateEvaluator;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import com.google.common.collect.ImmutableMap;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceWrapper;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.api.wrappers.ValueMapDecorator;
import org.apache.sling.jcr.resource.api.JcrResourceConstants;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.RequestAttribute;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.jcr.RepositoryException;
import javax.jcr.Session;
import java.util.HashMap;
import java.util.Map;

@Model(
        adaptables = {SlingHttpServletRequest.class},
        adapters = {ListItemImage.class},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ListItemImageImpl implements ListItemImage {
    private static final Logger log = LoggerFactory.getLogger(ListItemImageImpl.class);

    @Self
    private SlingHttpServletRequest request;

    @RequestAttribute
    private String path;

    @OSGiService
    private QueryBuilder queryBuilder;

    private Page page;
    private Resource imageResource;

    @PostConstruct
    protected void init() {
        page = request.getResourceResolver().adaptTo(PageManager.class).getContainingPage(path);
    }

    public Resource getResource() throws RepositoryException {
       if (imageResource == null) {

           final Map<String, String> params = ImmutableMap.<String, String>builder().
                   put(PathPredicateEvaluator.PATH, page.getContentResource().getPath()).
                   put(TypePredicateEvaluator.TYPE, JcrConstants.NT_UNSTRUCTURED).
                   put(JcrPropertyPredicateEvaluator.PROPERTY, JcrResourceConstants.SLING_RESOURCE_TYPE_PROPERTY).
                   put(JcrPropertyPredicateEvaluator.PROPERTY + "." + JcrPropertyPredicateEvaluator.VALUE, "wknd/components/content/image").
                   put("p." + PredicateGroup.PARAM_LIMIT, "1").
                   build();

           final SearchResult searchResult = queryBuilder.createQuery(PredicateGroup.create(params),
                           request.getResourceResolver().adaptTo(Session.class)).getResult();

           if (searchResult.getHits().size() > 0) {
               imageResource = new SimpleImageComponentResource(request.getResourceResolver().getResource(searchResult.getHits().get(0).getPath()));

               // Handle bug in QueryBuilder that leaks ResourceResolvers
               searchResult.getHits().get(0).getResource().getResourceResolver().close();
           }
       }

       return imageResource;
    }

    private class SimpleImageComponentResource extends ResourceWrapper {
        private static final String PN_FILE_REFERENCE = "fileReference";

        private ValueMap properties = new ValueMapDecorator(new HashMap<>());

        public SimpleImageComponentResource(final Resource resource) {
            super(resource);

            properties.put(PN_FILE_REFERENCE,
                    resource.getValueMap().get(PN_FILE_REFERENCE));
            properties.put(Image.PN_IS_DECORATIVE, false);
            properties.put(Image.PN_DISPLAY_POPUP_TITLE, false);
            properties.put(Image.PN_TITLE_VALUE_FROM_DAM, false);
        }

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

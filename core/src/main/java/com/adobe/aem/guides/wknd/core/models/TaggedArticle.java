package com.adobe.aem.guides.wknd.core.models;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.jcr.Session;
import java.util.HashMap;
import java.util.Map;

@Model(
    adaptables = Resource.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class TaggedArticle {

    @ValueMapValue
    private String articleTag;

    @OSGiService
    private QueryBuilder queryBuilder;

    @SlingObject
    private ResourceResolver resourceResolver;

    private String eyebrow;
    private String imagePath;
    private String imageAlt;
    private String title;
    private String description;
    private String articlePath;

    @PostConstruct
    protected void init() {
        if (articleTag != null && !articleTag.isEmpty()) {
            fetchLatestArticle();
        }
    }

    private void fetchLatestArticle() {
        try {
            Session session = resourceResolver.adaptTo(Session.class);
            Map<String, String> predicates = new HashMap<>();
            
            // Set up search predicates
            predicates.put("path", "/content/wknd/us/en");
            predicates.put("type", "cq:Page");
            predicates.put("tagid", articleTag);
            predicates.put("orderby", "@jcr:content/cq:lastModified");
            predicates.put("orderby.sort", "desc");
            predicates.put("p.limit", "1");

            Query query = queryBuilder.createQuery(PredicateGroup.create(predicates), session);
            SearchResult result = query.getResult();

            if (result.getHits().size() > 0) {
                Hit hit = result.getHits().get(0);
                Resource articleResource = hit.getResource();
                Page articlePage = articleResource.adaptTo(Page.class);

                if (articlePage != null) {
                    // Get the article content
                    ValueMap properties = articlePage.getProperties();
                    Resource contentResource = articlePage.getContentResource();
                    
                    // Set the properties
                    this.title = properties.get("jcr:title", String.class);
                    this.description = properties.get("jcr:description", String.class);
                    this.eyebrow = properties.get("eyebrow", String.class);
                    this.articlePath = articlePage.getPath();

                    // Handle featured image
                    Resource imageResource = contentResource.getChild("root/container/image");
                    if (imageResource != null) {
                        ValueMap imageProperties = imageResource.getValueMap();
                        this.imagePath = imageProperties.get("fileReference", String.class);
                        this.imageAlt = imageProperties.get("alt", String.class);
                    }
                }
            }
        } catch (Exception e) {
            // Log error appropriately
            e.printStackTrace();
        }
    }

    // Getter methods
    public String getEyebrow() {
        return eyebrow;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getImageAlt() {
        return imageAlt;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getArticlePath() {
        return articlePath;
    }

    public boolean hasContent() {
        return title != null && !title.isEmpty();
    }
}
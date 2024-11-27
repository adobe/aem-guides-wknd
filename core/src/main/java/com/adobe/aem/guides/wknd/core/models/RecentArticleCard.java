package com.adobe.aem.guides.wknd.core.models;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;
import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import javax.jcr.Session;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

@Model(
    adaptables = Resource.class,
    resourceType = "wknd/components/recentarticlecard",
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class RecentArticleCard {

    @ValueMapValue(name = "articleTag")
    private String articleTag;

    @SlingObject
    private ResourceResolver resourceResolver;

    @OSGiService
    private QueryBuilder queryBuilder;

    private String articleTitle;
    private String articleDescription;
    private String articleImagePath;
    private String articlePath;
    private String articleEyebrow;
    private Calendar articleLastModified;

    @PostConstruct
    private void init() {
        if (articleTag != null && !articleTag.isEmpty()) {
            findMostRecentArticle();
        }
    }

    private void findMostRecentArticle() {
        try {
            // Create query predicates
            Map<String, String> predicates = new HashMap<>();
            predicates.put("path", "/content/wknd/us/en");
            predicates.put("type", "cq:Page");
            predicates.put("tagid", articleTag);
            predicates.put("orderby", "@jcr:content/cq:lastModified");
            predicates.put("orderby.sort", "desc");
            predicates.put("p.limit", "1"); // Get only the most recent article

            // Create the query
            Query query = queryBuilder.createQuery(PredicateGroup.create(predicates), 
                resourceResolver.adaptTo(Session.class));

            // Execute the query
            SearchResult result = query.getResult();

            // Process the result
            if (result.getHits().size() > 0) {
                Hit hit = result.getHits().get(0);
                Page articlePage = hit.getResource().adaptTo(Page.class);

                if (articlePage != null) {
                    // Set basic article properties
                    articleTitle = articlePage.getTitle();
                    articleDescription = articlePage.getDescription();
                    articlePath = articlePage.getPath();
                    articleLastModified = articlePage.getLastModified();

                    // Get featured image
                    Resource contentResource = articlePage.getContentResource();
                    if (contentResource != null) {
                        Resource featuredImage = contentResource.getChild("root/container/featured-image");
                        if (featuredImage != null) {
                            articleImagePath = featuredImage.getValueMap().get("fileReference", String.class);
                        }
                    }

                    // Get eyebrow text (assuming it's stored in page properties)
                    articleEyebrow = articlePage.getProperties().get("eyebrow", String.class);
                    if (articleEyebrow == null) {
                        // Fallback to article category or section
                        articleEyebrow = articlePage.getProperties().get("category", "Article");
                    }
                }
            }
        } catch (Exception e) {
            // Log error appropriately
            articleTitle = "Error loading article";
        }
    }

    // Getters
    public String getArticleTitle() {
        return articleTitle;
    }

    public String getArticleDescription() {
        return articleDescription;
    }

    public String getArticleImagePath() {
        return articleImagePath;
    }

    public String getArticlePath() {
        return articlePath;
    }

    public String getArticleEyebrow() {
        return articleEyebrow;
    }

    public Calendar getArticleLastModified() {
        return articleLastModified;
    }

    // Helper method to check if content exists
    public boolean hasContent() {
        return articleTitle != null && !articleTitle.isEmpty();
    }
}
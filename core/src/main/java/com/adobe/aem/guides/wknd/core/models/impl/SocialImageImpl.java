/*
 *  Copyright 2024 Adobe Systems Incorporated
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

import javax.annotation.PostConstruct;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ScriptVariable;
import org.apache.sling.models.annotations.injectorspecific.Self;

import com.adobe.aem.guides.wknd.core.models.SocialImage;
import com.day.cq.wcm.api.Page;

@Model(
        adaptables = SlingHttpServletRequest.class,
        adapters = SocialImage.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class SocialImageImpl implements SocialImage {

    private static final String NN_FEATURED_IMAGE = "cq:featuredimage";
    private static final String PN_FILE_REFERENCE = "fileReference";
    private static final String PN_ALT = "alt";
    private static final String PN_ALT_TEXT = "altText";
    private static final String DAM_PATH_PREFIX = "/content/dam/";

    /** Skip JCR/CQ housekeeping child nodes (policies, dialogs, responsive config, etc.) when walking the content tree. */
    private static final String[] SKIPPED_NODE_NAME_PREFIXES = {"jcr:", "cq:", "rep:"};

    /** Bound the content-tree walk so a pathological page structure can't cause excessive traversal per request. */
    private static final int MAX_NODES_TO_VISIT = 500;
    private static final int MAX_DEPTH = 15;

    @Self
    private SlingHttpServletRequest request;

    @ScriptVariable
    private Page currentPage;

    private String url;
    private String alternateText;

    @PostConstruct
    protected void init() {
        if (currentPage == null) {
            return;
        }

        if (resolveFromFeaturedImage()) {
            return;
        }

        resolveFromFirstImageOnPage();
    }

    /**
     * Prefers an author-curated image set via Page Properties &gt; Thumbnail ({@code cq:featuredimage}),
     * when present.
     */
    private boolean resolveFromFeaturedImage() {
        Resource featuredImage = currentPage.getContentResource(NN_FEATURED_IMAGE);
        if (featuredImage == null) {
            return false;
        }

        ValueMap properties = featuredImage.getValueMap();
        String fileReference = properties.get(PN_FILE_REFERENCE, String.class);
        if (StringUtils.isBlank(fileReference)) {
            return false;
        }

        url = toAbsoluteUrl(fileReference);
        alternateText = StringUtils.defaultIfBlank(properties.get(PN_ALT, String.class), currentPage.getTitle());
        return true;
    }

    /**
     * Falls back to the first DAM image referenced anywhere in the page's content tree (in rendered/document
     * order), covering pages that don't have a Thumbnail explicitly set, e.g. Image/Teaser components.
     */
    private void resolveFromFirstImageOnPage() {
        Resource contentResource = currentPage.getContentResource();
        if (contentResource == null) {
            return;
        }

        Resource firstImage = findFirstImage(contentResource, 0, new int[]{0});
        if (firstImage == null) {
            return;
        }

        ValueMap properties = firstImage.getValueMap();
        String fileReference = properties.get(PN_FILE_REFERENCE, String.class);
        url = toAbsoluteUrl(fileReference);
        String alt = StringUtils.defaultIfBlank(properties.get(PN_ALT, String.class),
                properties.get(PN_ALT_TEXT, String.class));
        alternateText = StringUtils.defaultIfBlank(alt, currentPage.getTitle());
    }

    /**
     * Depth-first, document-order search for the first descendant resource with a DAM image {@code fileReference}.
     */
    private Resource findFirstImage(Resource resource, int depth, int[] visitedCount) {
        if (depth > MAX_DEPTH || visitedCount[0] > MAX_NODES_TO_VISIT) {
            return null;
        }

        for (Resource child : resource.getChildren()) {
            visitedCount[0]++;
            if (visitedCount[0] > MAX_NODES_TO_VISIT) {
                return null;
            }

            if (StringUtils.startsWithAny(child.getName(), SKIPPED_NODE_NAME_PREFIXES)) {
                continue;
            }

            String fileReference = child.getValueMap().get(PN_FILE_REFERENCE, String.class);
            if (isDamImageReference(fileReference)) {
                return child;
            }

            Resource match = findFirstImage(child, depth + 1, visitedCount);
            if (match != null) {
                return match;
            }
        }

        return null;
    }

    private boolean isDamImageReference(String fileReference) {
        return StringUtils.isNotBlank(fileReference)
                && fileReference.startsWith(DAM_PATH_PREFIX)
                && !StringUtils.containsIgnoreCase(fileReference, "favicon");
    }

    /**
     * Resolves a repository/DAM path to an absolute URL using the current request's scheme and host,
     * so the resulting value is valid for {@code og:image}/{@code twitter:image} meta tags, which
     * require an absolute URL.
     */
    private String toAbsoluteUrl(String path) {
        if (StringUtils.startsWithAny(path, "http://", "https://")) {
            return path;
        }

        ResourceResolver resourceResolver = request.getResourceResolver();
        String mappedPath = resourceResolver.map(request, path);

        StringBuilder absoluteUrl = new StringBuilder();
        absoluteUrl.append(request.getScheme()).append("://").append(request.getServerName());

        int port = request.getServerPort();
        boolean isDefaultPort = ("http".equals(request.getScheme()) && port == 80)
                || ("https".equals(request.getScheme()) && port == 443);
        if (port > 0 && !isDefaultPort) {
            absoluteUrl.append(':').append(port);
        }

        if (!mappedPath.startsWith("/")) {
            absoluteUrl.append('/');
        }
        absoluteUrl.append(mappedPath);

        return absoluteUrl.toString();
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public String getAlternateText() {
        return alternateText;
    }

    @Override
    public boolean isPresent() {
        return StringUtils.isNotBlank(url);
    }
}

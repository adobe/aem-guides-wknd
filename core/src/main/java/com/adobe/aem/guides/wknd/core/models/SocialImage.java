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
package com.adobe.aem.guides.wknd.core.models;

/**
 * Resolves the current page's featured image (Page Properties &gt; Thumbnail tab, stored at
 * {@code jcr:content/cq:featuredimage}) as an absolute URL suitable for {@code og:image} /
 * {@code twitter:image} meta tags, so that crawlers (including Content AI Search's web-acquisition
 * crawler) have a single, canonical representative image for the page.
 **/
public interface SocialImage {

    /**
     * @return the absolute URL of the page's featured image, or {@code null} if none is set.
     */
    String getUrl();

    /**
     * @return the alt text/alternate text for the featured image, falling back to the page title.
     */
    String getAlternateText();

    /**
     * @return {@code true} if a featured image is available for the current page.
     */
    boolean isPresent();
}

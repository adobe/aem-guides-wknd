/*
 *  Copyright 2026 Adobe Systems Incorporated
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

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.adobe.aem.guides.wknd.core.models.SocialImage;
import com.adobe.aem.guides.wknd.core.telemetry.WkndTelemetryExtension;
import com.day.cq.wcm.api.Page;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.context.Scope;
import io.opentelemetry.sdk.trace.data.SpanData;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith({AemContextExtension.class, WkndTelemetryExtension.class})
class SocialImageImplTest {

    private static final AttributeKey<String> IMAGE_SOURCE = AttributeKey.stringKey("wknd.social_image.source");

    /** Stands in for the span that is current while AEM renders the HTL script that loads the model. */
    private static final String RENDERING_SPAN = "HTL: customheaderlibs.html";

    private final AemContext ctx = new AemContext();

    @BeforeEach
    void setUp() {
        ctx.addModelsForClasses(SocialImageImpl.class);
    }

    @Test
    void featuredImage_isRecordedOnTheCurrentSpan() {
        Page page = ctx.create().page("/content/wknd/us/en/featured");
        ctx.create().resource(page.getContentResource().getPath() + "/cq:featuredimage",
                "fileReference", "/content/dam/wknd/featured.jpg");

        SocialImage socialImage = adaptWhileRendering(page);

        assertTrue(socialImage.getUrl().endsWith("/content/dam/wknd/featured.jpg"));
        assertEquals("featured_image", imageSourceOnRenderingSpan());
    }

    @Test
    void firstImageOnPage_isRecordedOnTheCurrentSpan() {
        Page page = ctx.create().page("/content/wknd/us/en/no-thumbnail");
        ctx.create().resource(page.getContentResource().getPath() + "/root/container/image",
                "sling:resourceType", "wknd/components/image",
                "fileReference", "/content/dam/wknd/first.jpg");

        SocialImage socialImage = adaptWhileRendering(page);

        assertTrue(socialImage.getUrl().endsWith("/content/dam/wknd/first.jpg"));
        // Without the OpenTelemetry agent, @WithSpan on resolveFromFirstImageOnPage() creates no span of its own.
        assertEquals("first_image_on_page", imageSourceOnRenderingSpan());
    }

    @Test
    void noImage_isRecordedOnTheCurrentSpan() {
        Page page = ctx.create().page("/content/wknd/us/en/no-image");
        ctx.create().resource(page.getContentResource().getPath() + "/root/container/text",
                "sling:resourceType", "wknd/components/text",
                "text", "<p>No images here</p>");

        SocialImage socialImage = adaptWhileRendering(page);

        assertFalse(socialImage.isPresent());
        assertEquals("none", imageSourceOnRenderingSpan());
    }

    private SocialImage adaptWhileRendering(Page page) {
        ctx.currentPage(page);
        Span renderingSpan = GlobalOpenTelemetry.getTracer("test").spanBuilder(RENDERING_SPAN).startSpan();
        try (Scope ignored = renderingSpan.makeCurrent()) {
            return ctx.request().adaptTo(SocialImage.class);
        } finally {
            renderingSpan.end();
        }
    }

    private static String imageSourceOnRenderingSpan() {
        List<SpanData> spans = WkndTelemetryExtension.spans(RENDERING_SPAN);
        assertEquals(1, spans.size());
        return spans.get(0).getAttributes().get(IMAGE_SOURCE);
    }
}

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
package com.adobe.aem.guides.wknd.core.models.contentai;

/**
 * Reports whether AEM Core WCM Components' {@code ContentAIClientImpl} has an {@code apiKey}
 * configured on this instance, so a {@code contentaisearch} component instance can render a
 * clear "not configured" message instead of failing at query time.
 *
 * <p>This deliberately does not depend on {@code ContentAIClient} itself (that service is scoped
 * {@code com.adobe.aem.internal} and unreachable from application-tier code) - it only reads the
 * {@code ContentAIClientImpl} OSGi configuration's own {@code apiKey} property via
 * {@code ConfigurationAdmin}, which is a standard, publicly-accessible OSGi service.</p>
 */
public interface ContentAIAvailability {

    /**
     * @return {@code true} if a non-blank {@code apiKey} is configured for
     * {@code com.adobe.cq.wcm.core.components.internal.services.contentai.ContentAIClientImpl}
     * on this instance (author or publish, whichever this is running on), {@code false} otherwise.
     */
    boolean isConfigured();
}

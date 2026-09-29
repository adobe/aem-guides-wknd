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
package com.adobe.aem.guides.wknd.core.models.impl.contentai;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.io.IOException;

import com.adobe.aem.guides.wknd.core.models.contentai.ContentAIAvailability;

@Model(
        adaptables = {SlingHttpServletRequest.class},
        adapters = {ContentAIAvailability.class},
        resourceType = {ContentAIAvailabilityImpl.RESOURCE_TYPE},
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ContentAIAvailabilityImpl implements ContentAIAvailability {

    protected static final String RESOURCE_TYPE = "wknd/components/contentaisearch";

    /**
     * Same PID AEM Core WCM Components' {@code ContentAIClientImpl} is configured under
     * (see {@code ui.config/.../config.author|publish/com.adobe.cq.wcm.core.components.internal.services.contentai.ContentAIClientImpl.cfg.json}).
     */
    private static final String CONTENT_AI_CLIENT_PID =
            "com.adobe.cq.wcm.core.components.internal.services.contentai.ContentAIClientImpl";

    private static final String API_KEY_PROPERTY = "apiKey";

    private static final Logger LOGGER = LoggerFactory.getLogger(ContentAIAvailabilityImpl.class);

    @OSGiService
    private ConfigurationAdmin configurationAdmin;

    private boolean configured;

    @PostConstruct
    protected void init() {
        this.configured = resolveConfigured();
    }

    @Override
    public boolean isConfigured() {
        return configured;
    }

    /**
     * Looks up the {@code ContentAIClientImpl} configuration and reads its {@code apiKey}, going
     * out of its way to never influence that shared PID's OSGi bundle-location binding along the
     * way - see {@link #lookupConfiguration()} for why that binding matters here at all.
     */
    private boolean resolveConfigured() {
        if (configurationAdmin == null) {
            return false;
        }
        try {
            Configuration configuration = lookupConfiguration();
            Object apiKey = configuration.getProperties() == null
                    ? null
                    : configuration.getProperties().get(API_KEY_PROPERTY);
            return apiKey instanceof String && StringUtils.isNotBlank((String) apiKey);
        } catch (IOException e) {
            LOGGER.warn("Unable to determine whether Content AI is configured (PID {})", CONTENT_AI_CLIENT_PID, e);
            return false;
        } catch (RuntimeException e) {
            // Defensive: an unexpected ConfigurationAdmin implementation failure should degrade
            // to "not configured", never break the whole component's rendering via @PostConstruct.
            LOGGER.warn("Unexpected failure determining whether Content AI is configured (PID {})", CONTENT_AI_CLIENT_PID, e);
            return false;
        }
    }

    /**
     * Per OSGi/Felix semantics, {@code ConfigurationAdmin.getConfiguration(pid)} (single-argument
     * form) has a real side effect beyond "creates an empty in-memory object if none exists yet":
     * if no {@code ManagedService} is currently registered for the PID at the moment of the call
     * - whether because none exists yet, or because the real one is mid-restart during a bundle
     * refresh - it dynamically binds that PID to whichever bundle called it. Since this model can
     * be instantiated on almost every page render, it can win that race and bind the shared
     * {@code ContentAIClientImpl} PID to this bundle instead of the one that actually implements
     * the service - after which Felix stops delivering configuration updates to the real service
     * entirely, silently breaking Content AI regardless of what's configured.
     * <p>
     * The two-argument {@code getConfiguration(pid, location)} form doesn't have that problem:
     * passing an explicit location - even {@code null}, meaning "not yet bound" - means the call
     * itself never dynamically binds anything to the caller. That's what this method tries first,
     * so the production code path never touches bundle-location state at all.
     * <p>
     * The only reason for the fallback below is that Sling Mocks' {@code MockConfigurationAdmin}
     * (which this class's own unit test relies on) doesn't implement the two-argument form -  it
     * throws {@code UnsupportedOperationException} unconditionally, as does {@code
     * listConfigurations}, which is why neither is used as the primary lookup. The fallback uses
     * the single-argument form instead, immediately calling {@code setBundleLocation(null)} to
     * undo any binding side effect - but only when {@code getProperties() == null}, i.e. only for
     * a phantom this very call might have just created. A real, already-populated configuration is
     * never touched there either: unconditionally unbinding it on every render would repeatedly
     * kick an already-correctly-bound configuration back to unbound, forcing Felix to redo dynamic
     * bind resolution and redeliver {@code updated(props)} to the real service on every single
     * page render - on AEMaaCS publish, under real traffic across many pods, that would reopen the
     * exact kind of churn/race this method exists to avoid, just recurring instead of one-time.
     */
    private Configuration lookupConfiguration() throws IOException {
        try {
            return configurationAdmin.getConfiguration(CONTENT_AI_CLIENT_PID, null);
        } catch (UnsupportedOperationException e) {
            Configuration configuration = configurationAdmin.getConfiguration(CONTENT_AI_CLIENT_PID);
            if (configuration.getProperties() == null) {
                try {
                    configuration.setBundleLocation(null);
                } catch (UnsupportedOperationException ignored) {
                    // Expected on ConfigurationAdmin mocks that don't model bundle binding at all.
                }
            }
            return configuration;
        }
    }
}

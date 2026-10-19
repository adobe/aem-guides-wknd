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

import java.io.IOException;
import java.util.Hashtable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.framework.Constants;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;

import io.opentelemetry.api.common.AttributeKey;
import io.opentelemetry.api.trace.StatusCode;
import io.opentelemetry.sdk.trace.data.EventData;
import io.opentelemetry.sdk.trace.data.SpanData;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

import com.adobe.aem.guides.wknd.core.models.contentai.ContentAIAvailability;
import com.adobe.aem.guides.wknd.core.telemetry.WkndTelemetryExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Uses Sling Mocks' own {@code MockConfigurationAdmin} (rather than a raw Mockito mock
 * registered over it) - seeded through its real, supported {@code getConfiguration(pid).update(...)}
 * API, matching how {@link ContentAIAvailabilityImpl} itself reads configuration.
 * <p>
 * This suite cannot verify {@link ContentAIAvailabilityImpl#lookupConfiguration()}'s primary,
 * production path at all - the two-argument {@code getConfiguration(pid, null)} that avoids
 * dynamically binding the PID to the caller. {@code MockConfigurationAdmin} doesn't model bundle
 * binding, so there's nothing here to observe even if it were exercised. That path's correctness
 * (that it structurally avoids the bind race the single-argument fallback below only narrows) was
 * confirmed by live testing against a real AEM instance, not by any test in this class - a green
 * run of this suite says nothing about whether that path still behaves correctly. What this suite
 * *does* verify is: (a) the fallback path's own unbind-guard logic, and (b)
 * {@link #lookupConfiguration_fallsBackToSingleArgumentForm_whenTwoArgumentFormUnsupported()} below,
 * which pins down the assumption the fallback's existence depends on - that
 * {@code MockConfigurationAdmin} really does throw on the two-argument form - so an
 * osgi-mock upgrade that silently changes that (as a newer 3.x already does; see that test's
 * javadoc) fails loudly here instead of just quietly leaving the fallback path uncovered.
 */
@ExtendWith({AemContextExtension.class, WkndTelemetryExtension.class})
class ContentAIAvailabilityImplTest {

    private static final String CONTENT_AI_CLIENT_PID =
            "com.adobe.cq.wcm.core.components.internal.services.contentai.ContentAIClientImpl";

    private final AemContext ctx = new AemContext();

    @BeforeEach
    void setUp() {
        ctx.addModelsForClasses(ContentAIAvailabilityImpl.class);
        ctx.create().resource("/content/ai-powered-search",
                "sling:resourceType", "wknd/components/contentaisearch");
    }

    private ContentAIAvailability adapt() {
        ctx.currentResource("/content/ai-powered-search");
        return ctx.request().adaptTo(ContentAIAvailability.class);
    }

    private void seedApiKey(String apiKey) throws Exception {
        ConfigurationAdmin configurationAdmin = ctx.getService(ConfigurationAdmin.class);
        Configuration configuration = configurationAdmin.getConfiguration(CONTENT_AI_CLIENT_PID);
        Hashtable<String, Object> props = new Hashtable<>();
        if (apiKey != null) {
            props.put("apiKey", apiKey);
        }
        configuration.update(props);
    }

    @Test
    void isConfigured_returnsFalse_whenNoConfigurationExists() {
        assertFalse(adapt().isConfigured());
    }

    @Test
    void isConfigured_returnsFalse_whenApiKeyMissing() throws Exception {
        seedApiKey(null);

        assertFalse(adapt().isConfigured());
    }

    @Test
    void isConfigured_returnsFalse_whenApiKeyBlank() throws Exception {
        seedApiKey("   ");

        assertFalse(adapt().isConfigured());
    }

    @Test
    void isConfigured_returnsTrue_whenApiKeyPresent() throws Exception {
        seedApiKey("3a2ede9fd93a47c1b41fa4d73a97b926");

        assertTrue(adapt().isConfigured());

        final SpanData span = lookupSpan();
        assertEquals(Boolean.TRUE, span.getAttributes().get(AttributeKey.booleanKey("wknd.content_ai.configured")));
        assertEquals(StatusCode.UNSET, span.getStatus().getStatusCode());
    }

    @Test
    void resolveConfigured_recordsLookupFailureOnSpan() throws Exception {
        registerConfigurationAdminThrowing(new IOException("Configuration store unavailable"));

        assertFalse(adapt().isConfigured());

        assertFailureRecorded(lookupSpan(), "java.io.IOException", "Configuration store unavailable");
    }

    @Test
    void resolveConfigured_recordsUnexpectedFailureOnSpan() throws Exception {
        registerConfigurationAdminThrowing(new IllegalStateException("Unexpected failure"));

        assertFalse(adapt().isConfigured());

        assertFailureRecorded(lookupSpan(), "java.lang.IllegalStateException", "Unexpected failure");
    }

    private void registerConfigurationAdminThrowing(Exception failure) throws Exception {
        // Spy the Sling mock, so the OSGi mocks keep working, and throw from the two-argument lookup the model uses.
        // doAnswer rather than doThrow: the Sling mock's override does not declare IOException.
        ConfigurationAdmin spyConfigurationAdmin = spy(ctx.getService(ConfigurationAdmin.class));
        doAnswer(invocation -> {
            throw failure;
        }).when(spyConfigurationAdmin).getConfiguration(CONTENT_AI_CLIENT_PID, null);
        ctx.registerService(ConfigurationAdmin.class, spyConfigurationAdmin,
                Constants.SERVICE_RANKING, Integer.MAX_VALUE);
    }

    private static SpanData lookupSpan() {
        final java.util.List<SpanData> spans = WkndTelemetryExtension.spans("ContentAIAvailability.resolveConfigured");
        assertEquals(1, spans.size());
        return spans.get(0);
    }

    private static void assertFailureRecorded(SpanData span, String exceptionType, String exceptionMessage) {
        assertEquals(StatusCode.ERROR, span.getStatus().getStatusCode());
        assertEquals("Content AI configuration lookup failed", span.getStatus().getDescription());
        final EventData exception = span.getEvents().get(0);
        assertEquals("exception", exception.getName());
        assertEquals(exceptionType, exception.getAttributes().get(AttributeKey.stringKey("exception.type")));
        assertEquals(exceptionMessage, exception.getAttributes().get(AttributeKey.stringKey("exception.message")));
    }

    /**
     * {@code MockConfigurationAdmin} doesn't model bundle-location binding at all (its
     * {@code setBundleLocation} throws {@code UnsupportedOperationException}, swallowed by
     * production code), so it can't observe whether {@link ContentAIAvailabilityImpl} actually
     * calls it - or, just as important, whether it wrongly calls it on an already-populated
     * configuration. These two tests {@code spy()} on the context's own {@code ConfigurationAdmin}
     * (rather than replacing it outright with a bare mock) so unrelated internal lookups for
     * other PIDs - which a full replacement wouldn't know how to answer - keep working normally,
     * and only stub {@code getConfiguration} for this test's own PID to return a mock
     * {@code Configuration}, purely to observe the one call under test.
     */
    @Test
    void resolveConfigured_unbindsOnlyThePhantomConfiguration_whenNoneExistedYet() throws Exception {
        ConfigurationAdmin spyConfigurationAdmin = spy(ctx.getService(ConfigurationAdmin.class));
        Configuration mockConfiguration = mock(Configuration.class);
        when(mockConfiguration.getProperties()).thenReturn(null);
        doReturn(mockConfiguration).when(spyConfigurationAdmin).getConfiguration(CONTENT_AI_CLIENT_PID);
        ctx.registerService(ConfigurationAdmin.class, spyConfigurationAdmin,
                Constants.SERVICE_RANKING, Integer.MAX_VALUE);

        assertFalse(adapt().isConfigured());

        verify(mockConfiguration).setBundleLocation(null);
    }

    @Test
    void resolveConfigured_neverUnbindsAnAlreadyPopulatedConfiguration() throws Exception {
        ConfigurationAdmin spyConfigurationAdmin = spy(ctx.getService(ConfigurationAdmin.class));
        Configuration mockConfiguration = mock(Configuration.class);
        Hashtable<String, Object> props = new Hashtable<>();
        props.put("apiKey", "3a2ede9fd93a47c1b41fa4d73a97b926");
        when(mockConfiguration.getProperties()).thenReturn(props);
        doReturn(mockConfiguration).when(spyConfigurationAdmin).getConfiguration(CONTENT_AI_CLIENT_PID);
        ctx.registerService(ConfigurationAdmin.class, spyConfigurationAdmin,
                Constants.SERVICE_RANKING, Integer.MAX_VALUE);

        assertTrue(adapt().isConfigured());

        verify(mockConfiguration, never()).setBundleLocation(any());
    }

    /**
     * Pins down the assumption {@code lookupConfiguration()}'s fallback branch depends on:
     * that {@code MockConfigurationAdmin} throws {@code UnsupportedOperationException} for the
     * two-argument {@code getConfiguration(pid, location)}, so it never dynamically binds the PID
     * (which is why production code tries that form first). Confirmed against the version this
     * project currently resolves (Apache Sling OSGi Mocks 2.4.x, via
     * {@code io.wcm.testing.aem-mock.junit5}). A newer osgi-mock 3.5.x instead makes the
     * two-argument form delegate straight to the single-argument one without throwing - if a
     * future dependency bump pulls that in, this test fails here, loudly, rather than silently
     * leaving the single-argument fallback path (and its unbind-guard, verified by the two tests
     * above) unexercised by anything in this suite.
     */
    @Test
    void mockConfigurationAdmin_stillThrowsOnTwoArgumentGetConfiguration() {
        ConfigurationAdmin configurationAdmin = ctx.getService(ConfigurationAdmin.class);

        assertThrows(UnsupportedOperationException.class,
                () -> configurationAdmin.getConfiguration(CONTENT_AI_CLIENT_PID, null));
    }
}

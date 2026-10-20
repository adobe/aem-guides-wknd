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
package com.adobe.aem.guides.wknd.core.telemetry;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.sdk.OpenTelemetrySdk;
import io.opentelemetry.sdk.testing.exporter.InMemorySpanExporter;
import io.opentelemetry.sdk.trace.SdkTracerProvider;
import io.opentelemetry.sdk.trace.data.SpanData;
import io.opentelemetry.sdk.trace.export.SimpleSpanProcessor;

/**
 * Installs an in-memory OpenTelemetry SDK as {@link GlobalOpenTelemetry}, so unit tests can assert on the spans
 * that WKND code creates. On AEM as a Cloud Service, the OpenTelemetry Java agent plays this role.
 * <p>
 * {@link WkndTelemetry#TRACER} is resolved once, when {@link WkndTelemetry} is first loaded, so the SDK must be
 * installed before any test loads a model that uses it. Add {@code @ExtendWith(WkndTelemetryExtension.class)} to
 * every test class that exercises such a model. The SDK is installed once per JVM, and the captured spans are
 * cleared before each test.
 * <p>
 * {@code @WithSpan} has no effect here: the agent implements it, so methods annotated with it do not create spans
 * in unit tests.
 */
public final class WkndTelemetryExtension implements BeforeAllCallback, BeforeEachCallback {

    private static final InMemorySpanExporter SPANS = InMemorySpanExporter.create();

    private static boolean installed;

    @Override
    public void beforeAll(ExtensionContext context) {
        install();
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        SPANS.reset();
    }

    /** @return the spans ended since the current test started. */
    public static List<SpanData> spans() {
        return SPANS.getFinishedSpanItems();
    }

    /** @return the spans with the given name ended since the current test started, in the order they ended. */
    public static List<SpanData> spans(String name) {
        return spans().stream().filter(span -> name.equals(span.getName())).collect(Collectors.toList());
    }

    private static synchronized void install() {
        if (installed) {
            return;
        }
        SdkTracerProvider tracerProvider = SdkTracerProvider.builder()
                .addSpanProcessor(SimpleSpanProcessor.create(SPANS))
                .build();
        GlobalOpenTelemetry.resetForTest();
        GlobalOpenTelemetry.set(OpenTelemetrySdk.builder().setTracerProvider(tracerProvider).build());

        // Loads WkndTelemetry now, if no test has yet, so its TRACER comes from this SDK. If an earlier test loaded
        // it without this extension, TRACER is the no-op tracer: fail clearly rather than find no spans later.
        Span probe = WkndTelemetry.TRACER.spanBuilder("probe").startSpan();
        probe.end();
        if (!probe.getSpanContext().isValid()) {
            throw new IllegalStateException("WkndTelemetry was loaded before the test OpenTelemetry SDK was installed. "
                    + "Add @ExtendWith(WkndTelemetryExtension.class) to every test class that uses a WKND model "
                    + "instrumented with WkndTelemetry.");
        }
        SPANS.reset();
        installed = true;
    }
}

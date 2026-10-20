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

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.trace.Tracer;

/**
 * One place to get the WKND {@link Tracer}.
 * <p>
 * On AEM as a Cloud Service, the OpenTelemetry Java agent provides the implementation behind
 * {@link GlobalOpenTelemetry}, so spans created here are exported with the agent's own
 * telemetry. Where there is no agent (the local AEM SDK, unit tests), every call is a no-op.
 * <p>
 * Create the tracer once and reuse it. Do not create them per request.
 */
public final class WkndTelemetry {

    /** Instrumentation scope: identifies telemetry produced by WKND code in your APM backend. */
    public static final String SCOPE = "com.adobe.aem.guides.wknd";

    public static final Tracer TRACER = GlobalOpenTelemetry.getTracer(SCOPE);

    private WkndTelemetry() {
    }
}

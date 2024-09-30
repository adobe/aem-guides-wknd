package com.adobe.aem.guides.wknd.core.service.impl;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.adobe.aem.guides.wknd.core.service.HelloWorldService;

@Component(service = HelloWorldService.class, configurationPolicy = ConfigurationPolicy.REQUIRE, immediate = true)
@Designate(ocd = HelloWorldServiceImpl.Cfg.class)
public class HelloWorldServiceImpl implements HelloWorldService {
    private static final Logger log = LoggerFactory.getLogger(HelloWorldServiceImpl.class);

    private Cfg cfg;

    /* Service Methods */

    @Override
    public final String sayHello() {
        return String.format("Hello %s!", cfg.planet());
    }

    /* OSGi Component Methods */

    @Activate
    protected final void activate(final Cfg cfg) throws Exception {
        log.info("Activating Hello World Service with planet '{}'", cfg.planet().trim());
        this.cfg = cfg;
        /*
         * int constInt = 0;
         * int constInt2 = 0;
         * int constInt3 = constInt / constInt2;
         * log.info("constInt3: {}", constInt3);
         */
    }

    @Deactivate
    protected final void deactivate(final Cfg cfg) {
    }

    @ObjectClassDefinition(name = "WKND - Hello World Service - Basic OSGi Service Configuration")
    public @interface Cfg {
        @AttributeDefinition(name = "Planet", description = "The planet from which to greet from.")
        String planet() default "Local World!!";
    }
}
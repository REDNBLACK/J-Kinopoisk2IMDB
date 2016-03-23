package org.f0w.k2i.core.providers;

import com.google.inject.AbstractModule;
import com.typesafe.config.Config;

public class ConfigurationProvider extends AbstractModule {
    private final Config config;

    public ConfigurationProvider(Config config) {
        this.config = config;
    }

    @Override
    protected void configure() {
        bind(Config.class).toInstance(config);
    }
}

package org.f0w.k2i.core;

import com.google.inject.AbstractModule;
import org.f0w.k2i.core.configuration.Configuration;
import org.f0w.k2i.core.configuration.NativeConfiguration;

public class ServiceProvider extends AbstractModule {
    @Override
    protected void configure() {
        bind(Configuration.class).to(NativeConfiguration.class);
    }
}

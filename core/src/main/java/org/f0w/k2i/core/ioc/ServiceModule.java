package org.f0w.k2i.core.ioc;

import com.google.inject.AbstractModule;
import org.f0w.k2i.core.model.service.*;

/**
 * Guice module providing binds for service layer
 */
public class ServiceModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(MovieService.class).to(MovieServiceImpl.class);
        bind(KinopoiskFileService.class).to(KinopoiskFileServiceImpl.class);
        bind(ImportProgressService.class).to(ImportProgressServiceImpl.class);
    }
}

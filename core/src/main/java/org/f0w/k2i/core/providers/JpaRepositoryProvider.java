package org.f0w.k2i.core.providers;

import com.google.inject.AbstractModule;
import org.f0w.k2i.core.model.repository.*;

public class JpaRepositoryProvider extends AbstractModule {
    @Override
    protected void configure() {
        bind(ImportProgressRepository.class).to(ImportProgressRepositoryImpl.class);
        bind(KinopoiskFileRepository.class).to(KinopoiskFileRepositoryImpl.class);
        bind(MovieRepository.class).to(MovieRepositoryImpl.class);
    }
}

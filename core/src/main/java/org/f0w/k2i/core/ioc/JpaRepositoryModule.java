package org.f0w.k2i.core.ioc;

import com.google.inject.AbstractModule;
import org.f0w.k2i.core.model.repository.ImportProgressRepository;
import org.f0w.k2i.core.model.repository.KinopoiskFileRepository;
import org.f0w.k2i.core.model.repository.MovieRepository;
import org.f0w.k2i.core.model.repository.jpa.JpaImportProgressRepositoryImpl;
import org.f0w.k2i.core.model.repository.jpa.JpaKinopoiskFileRepositoryImpl;
import org.f0w.k2i.core.model.repository.jpa.JpaMovieRepositoryImpl;

/**
 * Guice module providing binds repositories layer
 */
public class JpaRepositoryModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ImportProgressRepository.class).to(JpaImportProgressRepositoryImpl.class);
        bind(KinopoiskFileRepository.class).to(JpaKinopoiskFileRepositoryImpl.class);
        bind(MovieRepository.class).to(JpaMovieRepositoryImpl.class);
    }
}

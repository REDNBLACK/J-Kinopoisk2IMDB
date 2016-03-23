package org.f0w.k2i.core.handler;

import com.google.inject.Inject;
import org.f0w.k2i.core.MovieManager;
import org.f0w.k2i.core.model.entity.KinopoiskFile;
import org.f0w.k2i.core.model.repository.ImportProgressRepository;

import static com.google.common.base.Preconditions.*;

abstract class AbstractMovieHandler implements MovieHandler {
    protected ImportProgressRepository importProgressRepository;
    protected MovieManager movieManager;
    protected KinopoiskFile kinopoiskFile;

    @Inject
    public AbstractMovieHandler(ImportProgressRepository importProgressRepository, MovieManager movieManager) {
        this.importProgressRepository = importProgressRepository;
        this.movieManager = movieManager;
    }

    @Override
    public void setKinopoiskFile(KinopoiskFile kinopoiskFile) {
        this.kinopoiskFile = checkNotNull(kinopoiskFile);
    }
}

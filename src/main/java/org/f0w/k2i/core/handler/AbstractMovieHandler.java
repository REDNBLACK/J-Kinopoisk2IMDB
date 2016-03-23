package org.f0w.k2i.core.handler;

import org.f0w.k2i.core.MovieManager;
import org.f0w.k2i.core.model.repository.ImportProgressRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class AbstractMovieHandler implements MovieHandler {
    protected static final Logger LOG = LoggerFactory.getLogger(AbstractMovieHandler.class);
    
    protected ImportProgressRepository importProgressRepository;
    protected MovieManager movieManager;

    public AbstractMovieHandler(ImportProgressRepository importProgressRepository, MovieManager movieManager) {
        this.importProgressRepository = importProgressRepository;
        this.movieManager = movieManager;
    }
}

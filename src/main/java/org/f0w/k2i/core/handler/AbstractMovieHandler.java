package org.f0w.k2i.core.handler;

import org.f0w.k2i.core.MovieManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

abstract class AbstractMovieHandler implements MovieHandler {
    protected static final Logger LOG = LoggerFactory.getLogger(AbstractMovieHandler.class);

    protected MovieManager movieManager;

    public AbstractMovieHandler(MovieManager movieManager) {
        this.movieManager = movieManager;
    }
}

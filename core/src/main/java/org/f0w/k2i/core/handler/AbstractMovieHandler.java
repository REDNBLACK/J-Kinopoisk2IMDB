package org.f0w.k2i.core.handler;

import org.f0w.k2i.core.model.entity.ImportProgress;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

abstract class AbstractMovieHandler implements MovieHandler {
    protected static final Logger LOG = LoggerFactory.getLogger(AbstractMovieHandler.class);
    protected Set<Type> types;
    protected MovieHandler next;

    @Override
    public MovieHandler setNext(MovieHandler handler) {
        next = handler;

        return handler;
    }

    @Override
    public void handle(ImportProgress importProgress, List<Error> errors, MovieHandler.Type type) {
        if (types.contains(type)) {
            handleMovie(importProgress, errors);
        }

        if (next != null) {
            next.handle(importProgress, errors, type);
        }
    }

    protected abstract void handleMovie(ImportProgress importProgress, List<Error> errors);
}

package org.f0w.k2i.core.handler;

import org.f0w.k2i.core.model.entity.ImportProgress;
import org.f0w.k2i.core.model.entity.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Set;

/**
 * Chain of responsibility implementation executed for each ImportProgress entity
 */
public abstract class MovieHandler {
    protected static final Logger LOG = LoggerFactory.getLogger(MovieHandler.class);
    protected Set<Type> types;
    protected MovieHandler next;

    /**
     * Sets the next handler in chain
     * @param handler Handler to set
     * @return Handler which was just set
     */
    public MovieHandler setNext(MovieHandler handler) {
        next = handler;

        return handler;
    }

    /**
     * Executes handler
     * @param importProgress ImportProgress entity
     * @param errors List of errors
     * @param type Type of MovieHandler
     */
    public void handle(ImportProgress importProgress, List<Error> errors, MovieHandler.Type type) {
        if (types.contains(type)) {
            handleMovie(importProgress, errors);
        }

        if (next != null) {
            next.handle(importProgress, errors, type);
        }
    }

    /**
     * Handles ImportProgress entity. Implementation specific to each handler.
     * @param importProgress Entity to handle
     * @param errors List which fill with errors if occured
     */
    protected abstract void handleMovie(ImportProgress importProgress, List<Error> errors);

    /**
     * Available types of MovieHandler
     */
    public enum Type {
        ADD_TO_WATCHLIST,
        SET_RATING,
        COMBINED
    }

    /**
     * MovieHandler error
     */
    public class Error {
        private final Movie movie;
        private final String message;

        public Error(ImportProgress importProgress, String message) {
            this.movie = importProgress.getMovie();
            this.message = message;
        }

        public Movie getMovie() {
            return movie;
        }

        public String getMessage() {
            return message;
        }
    }
}

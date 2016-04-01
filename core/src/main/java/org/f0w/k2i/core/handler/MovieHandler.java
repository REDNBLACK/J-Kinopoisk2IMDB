package org.f0w.k2i.core.handler;

import org.f0w.k2i.core.model.entity.ImportProgress;
import org.f0w.k2i.core.model.entity.Movie;

import java.util.List;

public interface MovieHandler {
    MovieHandler setNext(MovieHandler handler);

    void handle(ImportProgress importProgress, List<Error> errors, MovieHandler.Type type);

    enum Type {
        ADD_TO_WATCHLIST,
        SET_RATING,
        COMBINED
    }

    class Error {
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

package org.f0w.k2i.core.handler;

import lombok.NonNull;
import lombok.Value;
import org.f0w.k2i.core.model.entity.ImportProgress;
import org.f0w.k2i.core.model.entity.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.EnumSet;
import java.util.List;
import java.util.Set;

/**
 * Chain of responsibility implementation executed for each ImportProgress entity
 */
public abstract class MovieHandler {
    protected static final Logger LOG = LoggerFactory.getLogger(MovieHandler.class);

    @NonNull
    private Set<Type> types;

    @NonNull
    private MovieHandler next;

    /**
     * Set types of which current handler will be executed
     *
     * @param first First Type
     * @param rest  Rest of Types
     * @return this
     */
    public MovieHandler setTypes(Type first, Type... rest) {
        this.types = EnumSet.of(first, rest);

        return this;
    }

    /**
     * Set types of which current handler will be executed
     *
     * @param type Type
     * @return this
     */
    public MovieHandler setTypes(Type type) {
        this.types = EnumSet.of(type);

        return this;
    }

    /**
     * Sets the next handler in chain
     *
     * @param handler Handler to set
     * @return Handler which was just set
     */
    public MovieHandler setNext(MovieHandler handler) {
        next = handler;

        return handler;
    }

    /**
     * Executes handler
     *
     * @param importProgress ImportProgress entity
     * @param errors         List of errors
     * @param type           Type of MovieHandler
     */
    public void handle(@NonNull ImportProgress importProgress, @NonNull List<Error> errors, @NonNull Type type) {
        if (types.contains(type) || isCombinedType(type)) {
            handleMovie(importProgress, errors);
        }

        if (next != null) {
            next.handle(importProgress, errors, type);
        }
    }

    /**
     * Checks that given type is COMBINED
     *
     * @param type Type to check
     * @return is combined type
     */
    private boolean isCombinedType(Type type) {
        return types.contains(Type.COMBINED) || Type.COMBINED.equals(type);
    }

    /**
     * Handles ImportProgress entity. Implementation specific to each handler.
     *
     * @param importProgress Entity to handle
     * @param errors         List which fill with errors if occured
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
    @Value
    public static final class Error {
        @NonNull
        private Movie movie;

        @NonNull
        private String message;
    }
}

package org.f0w.k2i.core.comparator;

import lombok.NonNull;
import org.f0w.k2i.core.model.entity.Movie;

/**
 * Interface for comparing equality of two movies.
 */
@FunctionalInterface
public interface MovieComparator {
    /**
     * Check equality of two movies
     *
     * @param movie1 First movie
     * @param movie2 Second movie
     * @return Are two movie objects equals to each other
     */
    boolean areEqual(@NonNull Movie movie1, @NonNull Movie movie2);

    /**
     * Available MovieComparator types
     */
    enum Type {
        TITLE_CONTAINS,
        TITLE_ENDS,
        TITLE_STARTS,
        TITLE_EQUALS,
        TITLE_SMART,
        YEAR_DEVIATION,
        YEAR_EQUALS
    }
}

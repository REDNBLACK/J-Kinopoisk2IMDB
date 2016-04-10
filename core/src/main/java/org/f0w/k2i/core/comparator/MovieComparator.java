package org.f0w.k2i.core.comparator;

import org.f0w.k2i.core.model.entity.Movie;

@FunctionalInterface
public interface MovieComparator {
    boolean areEqual(Movie movie1, Movie movie2);

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

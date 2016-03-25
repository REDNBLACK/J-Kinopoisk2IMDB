package org.f0w.k2i.core.comparator;

import org.f0w.k2i.core.model.entity.Movie;

@FunctionalInterface
public interface MovieComparator {
    boolean areEqual(Movie movie1, Movie movie2);
}

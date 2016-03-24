package org.f0w.k2i.core.comparator;

import org.f0w.k2i.core.model.entity.Movie;

import java.util.List;

public final class MovieComparatorContainer implements MovieComparator {
    protected final List<MovieComparator> comparators;

    public MovieComparatorContainer(List<MovieComparator> comparators) {
        this.comparators = comparators;
    }

    @Override
    public boolean areEqual(Movie movie1, Movie movie2) {
        return comparators.stream()
                .map(c -> c.areEqual(movie1, movie2))
                .noneMatch(c -> false);
    }
}

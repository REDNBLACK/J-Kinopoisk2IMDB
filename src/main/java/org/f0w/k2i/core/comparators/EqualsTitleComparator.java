package org.f0w.k2i.core.comparators;

import org.f0w.k2i.core.model.entity.Movie;

class EqualsTitleComparator implements EqualityComparator<Movie> {
    @Override
    public boolean areEqual(Movie movie1, Movie movie2) {
        return movie1.getTitle().equals(movie2.getTitle());
    }
}
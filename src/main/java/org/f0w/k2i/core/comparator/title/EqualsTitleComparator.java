package org.f0w.k2i.core.comparator.title;

import org.f0w.k2i.core.comparator.MovieComparator;
import org.f0w.k2i.core.model.entity.Movie;

public class EqualsTitleComparator implements MovieComparator {
    @Override
    public boolean areEqual(Movie movie1, Movie movie2) {
        return movie1.getTitle().equals(movie2.getTitle());
    }
}
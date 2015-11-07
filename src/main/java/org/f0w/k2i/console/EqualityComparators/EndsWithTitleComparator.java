package org.f0w.k2i.console.EqualityComparators;

import org.f0w.k2i.console.Models.Movie;

public class EndsWithTitleComparator implements EqualityComparator<Movie> {
    @Override
    public boolean areEqual(Movie obj1, Movie obj2) {
        return obj1.getTitle().endsWith(obj2.getTitle());
    }
}
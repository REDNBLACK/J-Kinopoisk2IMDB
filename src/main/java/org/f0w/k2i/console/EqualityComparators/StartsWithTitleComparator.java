package org.f0w.k2i.console.EqualityComparators;

import org.f0w.k2i.console.Models.Movie;

public class StartsWithTitleComparator implements EqualityComparator<Movie> {
    @Override
    public boolean areEqual(Movie obj1, Movie obj2) {
        return obj1.getTitle().startsWith(obj2.getTitle());
    }
}
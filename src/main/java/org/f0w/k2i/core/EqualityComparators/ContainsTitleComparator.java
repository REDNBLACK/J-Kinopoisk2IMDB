package org.f0w.k2i.core.EqualityComparators;

import org.f0w.k2i.core.Models.Movie;

public class ContainsTitleComparator implements EqualityComparator<Movie> {
    @Override
    public boolean areEqual(Movie movie1, Movie movie2) {
        String title1 = movie1.getTitle();
        String title2 = movie2.getTitle();

        return title1.contains(title2) || title2.contains(title1);
    }
}

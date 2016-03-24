package org.f0w.k2i.core.comparator.title;

import org.f0w.k2i.core.comparator.MovieComparator;
import org.f0w.k2i.core.model.entity.Movie;

public class ContainsTitleComparator implements MovieComparator {
    @Override
    public boolean areEqual(Movie movie1, Movie movie2) {
        String title1 = movie1.getTitle();
        String title2 = movie2.getTitle();

        return title1.contains(title2) || title2.contains(title1);
    }
}

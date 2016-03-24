package org.f0w.k2i.core.comparator.title;

import org.f0w.k2i.core.comparator.AbstractMovieComparator;
import org.f0w.k2i.core.model.entity.Movie;

public class EndsWithTitleComparator extends AbstractMovieComparator {
    @Override
    public boolean areEqual(Movie movie1, Movie movie2) {
        String title1 = movie1.getTitle();
        String title2 = movie2.getTitle();

        boolean result = title1.endsWith(title2) || title2.endsWith(title1);

        LOG.debug("Comparing title '{}' with title '{}', matches = '{}'", title1, title2, result);

        return result;
    }
}
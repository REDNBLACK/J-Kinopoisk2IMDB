package org.f0w.k2i.core.comparator.title;

import org.f0w.k2i.core.comparator.AbstractMovieComparator;
import org.f0w.k2i.core.model.entity.Movie;

/**
 * Checks that one movie title string contains another movie title string at the start.
 */
public final class StartsWithTitleComparator extends AbstractMovieComparator {
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean areEqual(Movie movie1, Movie movie2) {
        String title1 = movie1.getTitle().toLowerCase();
        String title2 = movie2.getTitle().toLowerCase();

        boolean result = title1.startsWith(title2) || title2.startsWith(title1);

        LOG.debug("Comparing title '{}' with title '{}', matches = '{}'", title1, title2, result);

        return result;
    }
}
package org.f0w.k2i.core.comparator.year;

import org.f0w.k2i.core.comparator.AbstractMovieComparator;
import org.f0w.k2i.core.model.entity.Movie;

/**
 * Check that one movie year equals to another movie year.
 */
public final class EqualsYearComparator extends AbstractMovieComparator {
    /** {@inheritDoc} */
    @Override
    public boolean areEqual(Movie movie1, Movie movie2) {
        boolean result = movie1.getYear() == movie2.getYear();

        LOG.debug(
                "Comparing year '{}' with year '{}', matches = '{}'",
                movie1.getYear(),
                movie2.getYear(),
                result
        );

        return result;
    }
}

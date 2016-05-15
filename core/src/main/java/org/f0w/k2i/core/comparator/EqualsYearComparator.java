package org.f0w.k2i.core.comparator;

import lombok.NonNull;
import org.f0w.k2i.core.model.entity.Movie;

/**
 * Check that one movie year equals to another movie year.
 */
final class EqualsYearComparator extends AbstractMovieComparator {
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean areEqual(@NonNull Movie movie1, @NonNull Movie movie2) {
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

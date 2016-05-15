package org.f0w.k2i.core.comparator;

import lombok.NonNull;
import org.f0w.k2i.core.model.entity.Movie;

final class EqualsTypeComparator extends AbstractMovieComparator {
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean areEqual(@NonNull Movie movie1, @NonNull Movie movie2) {
        boolean result = movie1.getType().equals(movie2.getType());

        LOG.debug(
                "Comparing type '{}' with type '{}', matches = '{}'",
                movie1.getType(),
                movie2.getType(),
                result
        );

        return result;
    }
}

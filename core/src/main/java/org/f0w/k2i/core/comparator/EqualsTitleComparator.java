package org.f0w.k2i.core.comparator;

import lombok.NonNull;
import org.f0w.k2i.core.model.entity.Movie;

/**
 * Checks that one movie title string equals another movie title string.
 */
final class EqualsTitleComparator extends AbstractMovieComparator {
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean areEqual(@NonNull Movie movie1, @NonNull Movie movie2) {
        String title1 = movie1.getTitle();
        String title2 = movie2.getTitle();

        boolean result = title1.equalsIgnoreCase(title2);

        LOG.debug("Comparing title '{}' with title '{}', matches = '{}'", title1, title2, result);

        return result;
    }
}
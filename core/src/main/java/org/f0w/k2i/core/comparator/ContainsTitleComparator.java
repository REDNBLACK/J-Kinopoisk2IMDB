package org.f0w.k2i.core.comparator;

import lombok.NonNull;
import org.f0w.k2i.core.model.entity.Movie;

import static org.apache.commons.lang3.StringUtils.containsIgnoreCase;

/**
 * Checks that one movie title string contains another movie title string in any position.
 */
final class ContainsTitleComparator extends AbstractMovieComparator {
    /**
     * {@inheritDoc}
     */
    @Override
    public boolean areEqual(@NonNull Movie movie1, @NonNull Movie movie2) {
        String title1 = movie1.getTitle();
        String title2 = movie2.getTitle();

        boolean result = containsIgnoreCase(title1, title2) || containsIgnoreCase(title2, title1);

        LOG.debug("Comparing title '{}' with title '{}', matches = '{}'", title1, title2, result);

        return result;
    }
}

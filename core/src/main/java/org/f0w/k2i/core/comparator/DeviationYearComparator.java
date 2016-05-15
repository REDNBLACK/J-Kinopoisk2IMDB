package org.f0w.k2i.core.comparator;

import com.google.common.collect.Range;
import com.typesafe.config.Config;
import lombok.NonNull;
import org.f0w.k2i.core.model.entity.Movie;

/**
 * Checks that one movie year is in range of second movie year +/- yearDeviation.
 */
final class DeviationYearComparator extends AbstractMovieComparator {
    private final Config config;

    public DeviationYearComparator(@NonNull Config config) {
        this.config = config;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean areEqual(@NonNull Movie movie1, @NonNull Movie movie2) {
        int yearDeviation = config.getInt("year_deviation");
        int movie2Year = movie2.getYear();

        boolean result = Range.closed(movie2Year - yearDeviation, movie2Year + yearDeviation).contains(movie1.getYear());

        LOG.debug(
                "Comparing year '{}' with year '{}', with deviation '{}', matches = '{}'",
                movie1.getYear(),
                movie2Year,
                yearDeviation,
                result
        );

        return result;
    }
}

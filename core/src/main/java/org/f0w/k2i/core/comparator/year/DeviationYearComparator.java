package org.f0w.k2i.core.comparator.year;

import com.google.common.collect.Range;
import com.google.inject.Inject;
import com.typesafe.config.Config;
import org.f0w.k2i.core.comparator.AbstractMovieComparator;
import org.f0w.k2i.core.model.entity.Movie;

public final class DeviationYearComparator extends AbstractMovieComparator {
    private final Config config;

    @Inject
    public DeviationYearComparator(Config config) {
        this.config = config;
    }

    @Override
    public boolean areEqual(Movie movie1, Movie movie2) {
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

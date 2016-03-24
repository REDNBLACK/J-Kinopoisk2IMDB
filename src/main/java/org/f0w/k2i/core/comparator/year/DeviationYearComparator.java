package org.f0w.k2i.core.comparator.year;

import com.google.common.collect.Range;
import org.f0w.k2i.core.comparator.MovieComparator;
import org.f0w.k2i.core.model.entity.Movie;

public class DeviationYearComparator implements MovieComparator {
    private final int yearDeviation;

    public DeviationYearComparator(int yearDeviation) {
        this.yearDeviation = yearDeviation;
    }

    @Override
    public boolean areEqual(Movie movie1, Movie movie2) {
        int movie2Year = movie2.getYear();

        return Range.closed(movie2Year - yearDeviation, movie2Year + yearDeviation).contains(movie1.getYear());
    }
}

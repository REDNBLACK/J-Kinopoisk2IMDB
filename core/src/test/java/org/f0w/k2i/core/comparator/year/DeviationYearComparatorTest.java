package org.f0w.k2i.core.comparator.year;

import com.google.common.collect.ImmutableMap;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.f0w.k2i.MovieTestData;
import org.f0w.k2i.core.comparator.MovieComparator;
import org.f0w.k2i.core.model.entity.Movie;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.f0w.k2i.MovieTestData.TestMovie;

public class DeviationYearComparatorTest {
    private final List<Integer> deviations = Arrays.asList(1, 2, 3, 4, 5);

    private MovieComparator initComparator(Integer deviation) {
        Config config = ConfigFactory.parseMap(ImmutableMap.of("year_deviation", deviation));

        return new DeviationYearComparator(config);
    }

    @Test
    public void testAreEqualWithinDeviation() throws Exception {
        deviations.forEach(deviation -> {
            MovieComparator comparator = initComparator(deviation);

            MovieTestData.MOVIE_LIST.forEach(movie -> {
                Movie movieWithForwardDeviation = TestMovie.copyOf(movie);
                movieWithForwardDeviation.setYear(movieWithForwardDeviation.getYear() + deviation);

                assertTrue(comparator.areEqual(movieWithForwardDeviation, movie));

                Movie movieWithBackwardDeviation = TestMovie.copyOf(movie);
                movieWithBackwardDeviation.setYear(movieWithBackwardDeviation.getYear() - deviation);

                assertTrue(comparator.areEqual(movieWithBackwardDeviation, movie));
            });
        });
    }

    @Test
    public void testAreEqualWithDeviationOverflow() throws Exception {
        deviations.forEach(deviation -> {
            MovieComparator comparator = initComparator(deviation);

            MovieTestData.MOVIE_LIST.forEach(movie -> {
                Movie deviatedMovie = TestMovie.copyOf(movie);
                deviatedMovie.setYear(deviatedMovie.getYear() + deviation + 1);

                assertFalse(comparator.areEqual(deviatedMovie, movie));
            });
        });
    }

    @Test
    public void testAreEqualWithDeviationUnderflow() throws Exception {
        deviations.forEach(deviation -> {
            MovieComparator comparator = initComparator(deviation);

            MovieTestData.MOVIE_LIST.forEach(movie -> {
                Movie deviatedMovie = TestMovie.copyOf(movie);
                deviatedMovie.setYear(deviatedMovie.getYear() - deviation);

                assertTrue(comparator.areEqual(deviatedMovie, movie));
            });
        });
    }
}
package org.f0w.k2i.core.comparator;

import com.google.common.collect.ImmutableMap;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.f0w.k2i.MovieTestData;
import org.f0w.k2i.core.model.entity.Movie;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class DeviationYearComparatorTest {
    private List<Integer> deviations = IntStream.range(0, 5).boxed().collect(Collectors.toList());

    private MovieComparator makeComparator(Integer deviation) {
        Config config = ConfigFactory.parseMap(ImmutableMap.of("year_deviation", deviation));

        return new MovieComparatorFactory(config).make(MovieComparator.Type.YEAR_DEVIATION);
    }

    @Test
    public void areEqualWithinDeviation() throws Exception {
        deviations.forEach(deviation -> {
            MovieComparator comparator = makeComparator(deviation);

            MovieTestData.MOVIES_LIST.forEach(movie -> {
                Movie movieWithForwardDeviation = new Movie(movie);
                movieWithForwardDeviation.setYear(movieWithForwardDeviation.getYear() + deviation);

                assertTrue(comparator.areEqual(movieWithForwardDeviation, movie));

                Movie movieWithBackwardDeviation = new Movie(movie);
                movieWithBackwardDeviation.setYear(movieWithBackwardDeviation.getYear() - deviation);

                assertTrue(comparator.areEqual(movieWithBackwardDeviation, movie));
            });
        });
    }

    @Test
    public void areEqualWithDeviationOverflow() throws Exception {
        deviations.forEach(deviation -> {
            MovieComparator comparator = makeComparator(deviation);

            MovieTestData.MOVIES_LIST.forEach(movie -> {
                Movie deviatedMovie = new Movie(movie);
                deviatedMovie.setYear(deviatedMovie.getYear() + deviation + 1);

                assertFalse(comparator.areEqual(deviatedMovie, movie));
            });
        });
    }

    @Test
    public void areEqualWithDeviationUnderflow() throws Exception {
        deviations.forEach(deviation -> {
            MovieComparator comparator = makeComparator(deviation);

            MovieTestData.MOVIES_LIST.forEach(movie -> {
                Movie deviatedMovie = new Movie(movie);
                deviatedMovie.setYear(deviatedMovie.getYear() - deviation);

                assertTrue(comparator.areEqual(deviatedMovie, movie));
            });
        });
    }
}
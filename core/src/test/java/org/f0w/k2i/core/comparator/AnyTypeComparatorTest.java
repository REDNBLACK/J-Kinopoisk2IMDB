package org.f0w.k2i.core.comparator;

import com.typesafe.config.ConfigFactory;
import org.f0w.k2i.core.model.entity.Movie;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class AnyTypeComparatorTest {
    private MovieComparator comparator = new MovieComparatorFactory(ConfigFactory.load())
            .make(MovieComparator.Type.TYPE_ANY);

    @Test
    public void areAllEqual() throws Exception {
        List<Movie> equalMovies = Arrays.asList(
                new Movie("Inception", 2010),
                new Movie("Inception", 2010).setType(Movie.Type.DOCUMENTARY),
                new Movie("Inception", 2010).setType(Movie.Type.SERIES),
                new Movie("Inception", 2010).setType(Movie.Type.SHORT),
                new Movie("Inception", 2010).setType(Movie.Type.VIDEO_GAME)
        );

        equalMovies.forEach(m1 -> equalMovies.forEach(m2 -> assertTrue(comparator.areEqual(m1, m2))));
    }
}
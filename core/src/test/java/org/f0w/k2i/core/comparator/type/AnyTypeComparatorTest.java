package org.f0w.k2i.core.comparator.type;

import org.f0w.k2i.core.comparator.MovieComparator;
import org.f0w.k2i.core.model.entity.Movie;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class AnyTypeComparatorTest {
    private MovieComparator comparator = new AnyTypeComparator();

    @Test
    public void areAllEqual() throws Exception {
        List<Movie> equalMovies = Arrays.asList(
                new Movie("Inception", 2010, Movie.Type.MOVIE),
                new Movie("Inception", 2010, Movie.Type.DOCUMENTARY),
                new Movie("Inception", 2010, Movie.Type.SERIES),
                new Movie("Inception", 2010, Movie.Type.SHORT),
                new Movie("Inception", 2010, Movie.Type.VIDEO_GAME)
        );

        equalMovies.forEach(m1 -> equalMovies.forEach(m2 -> assertTrue(comparator.areEqual(m1, m2))));
    }
}
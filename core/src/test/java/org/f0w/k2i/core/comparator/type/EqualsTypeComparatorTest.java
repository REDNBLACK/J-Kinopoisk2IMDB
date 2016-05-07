package org.f0w.k2i.core.comparator.type;

import org.f0w.k2i.core.comparator.MovieComparator;
import org.f0w.k2i.core.model.entity.Movie;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EqualsTypeComparatorTest {
    private MovieComparator comparator = new EqualsTypeComparator();

    @Test
    public void areEqualEqual() throws Exception {
        for (Movie.Type type : Movie.Type.values()) {
            assertTrue(comparator.areEqual(
                    new Movie("Inception", 2010, type),
                    new Movie("Inception", 2010, type)
            ));
        }
    }

    @Test
    public void areEqualNotEqual() throws Exception {
        assertFalse(comparator.areEqual(
                new Movie("Inception", 2010, Movie.Type.MOVIE),
                new Movie("Inception", 2010, Movie.Type.DOCUMENTARY)
        ));

        assertFalse(comparator.areEqual(
                new Movie("Inception", 2010, Movie.Type.MOVIE),
                new Movie("Inception", 2010, Movie.Type.SHORT)
        ));

        assertFalse(comparator.areEqual(
                new Movie("Inception", 2010, Movie.Type.MOVIE),
                new Movie("Inception", 2010, Movie.Type.SERIES)
        ));

        assertFalse(comparator.areEqual(
                new Movie("Inception", 2010, Movie.Type.MOVIE),
                new Movie("Inception", 2010, Movie.Type.VIDEO_GAME)
        ));
    }
}
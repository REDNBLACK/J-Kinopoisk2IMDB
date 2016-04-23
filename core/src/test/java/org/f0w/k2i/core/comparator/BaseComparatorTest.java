package org.f0w.k2i.core.comparator;

import org.f0w.k2i.core.comparator.MovieComparator;
import org.f0w.k2i.core.model.entity.Movie;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public abstract class BaseComparatorTest {
    protected MovieComparator comparator;
    protected Map<Movie, Movie> equalMovies;
    protected Map<Movie, Movie> notEqualMovies;

    @Test
    public void testAreEqual() throws Exception {
        equalMovies.forEach((firstMovie, secondMovie) -> assertTrue(comparator.areEqual(firstMovie, secondMovie)));
    }

    @Test
    public void testAreNotEqual() throws Exception {
        notEqualMovies.forEach((firstMovie, secondMovie) -> assertFalse(comparator.areEqual(firstMovie, secondMovie)));
    }
}

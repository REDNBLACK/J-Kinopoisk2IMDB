package org.f0w.k2i.core.comparator;

import com.typesafe.config.ConfigFactory;
import org.f0w.k2i.core.model.entity.Movie;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class EqualsTypeComparatorTest {
    private MovieComparator comparator = new MovieComparatorFactory(ConfigFactory.load())
            .make(MovieComparator.Type.TYPE_EQUALS);

    @Test
    public void areEqualEqual() throws Exception {
        for (Movie.Type type : Movie.Type.values()) {
            assertTrue(comparator.areEqual(
                    new Movie("Inception", 2010).setType(type),
                    new Movie("Inception", 2010).setType(type)
            ));
        }
    }

    @Test
    public void areEqualNotEqual() throws Exception {
        assertFalse(comparator.areEqual(
                new Movie("Inception", 2010),
                new Movie("Inception", 2010).setType(Movie.Type.DOCUMENTARY)
        ));

        assertFalse(comparator.areEqual(
                new Movie("Inception", 2010),
                new Movie("Inception", 2010).setType(Movie.Type.SHORT)
        ));

        assertFalse(comparator.areEqual(
                new Movie("Inception", 2010),
                new Movie("Inception", 2010).setType(Movie.Type.SERIES)
        ));

        assertFalse(comparator.areEqual(
                new Movie("Inception", 2010),
                new Movie("Inception", 2010).setType(Movie.Type.VIDEO_GAME)
        ));
    }
}
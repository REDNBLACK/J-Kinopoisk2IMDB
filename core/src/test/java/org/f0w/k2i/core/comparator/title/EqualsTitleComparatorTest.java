package org.f0w.k2i.core.comparator.title;

import com.google.common.collect.ImmutableMap;
import org.f0w.k2i.core.comparator.BaseComparatorTest;
import org.f0w.k2i.core.model.entity.Movie;
import org.junit.Before;

public class EqualsTitleComparatorTest extends BaseComparatorTest {
    @Before
    public void setUp() throws Exception {
        comparator = new EqualsTitleComparator();
        equalMovies = new ImmutableMap.Builder<Movie, Movie>()
                .put(new Movie("inception", 2010), new Movie("Inception", 2005))
                .put(new Movie("Super cool movie", 2000), new Movie("SUPER COOL MOVIE", 2002))
                .build();
        notEqualMovies = new ImmutableMap.Builder<Movie, Movie>()
                .put(new Movie("Super cool movie", 2010), new Movie("Inception", 2010))
                .put(new Movie("Inception 2", 2018), new Movie("Inception", 2010))
                .build();
    }
}
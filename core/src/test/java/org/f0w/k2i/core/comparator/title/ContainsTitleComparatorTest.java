package org.f0w.k2i.core.comparator.title;

import com.google.common.collect.ImmutableMap;
import org.f0w.k2i.core.comparator.BaseComparatorTest;
import org.f0w.k2i.core.model.entity.Movie;
import org.junit.Before;

public class ContainsTitleComparatorTest extends BaseComparatorTest {
    @Before
    public void setUp() throws Exception {
        comparator = new ContainsTitleComparator();
        equalMovies = new ImmutableMap.Builder<Movie, Movie>()
                .put(new Movie("Super cool movie", 2010), new Movie("Cool", 2005))
                .put(new Movie("Super cool movie", 2002), new Movie("Super", 2005))
                .build();
        notEqualMovies = new ImmutableMap.Builder<Movie, Movie>()
                .put(new Movie("Super cool movie", 2010), new Movie("Inception", 2010))
                .build();
    }
}
package org.f0w.k2i.core.comparator.title;

import com.google.common.collect.ImmutableMap;
import org.f0w.k2i.core.comparator.BaseComparatorTest;
import org.f0w.k2i.core.model.entity.Movie;
import org.junit.Before;

public class StartsWithTitleComparatorTest extends BaseComparatorTest {
    @Before
    public void setUp() throws Exception {
        comparator = new StartsWithTitleComparator();
        equalMovies = new ImmutableMap.Builder<Movie, Movie>()
                .put(new Movie("Super cool movie", 2010), new Movie("super", 2005))
                .build();
        notEqualMovies = new ImmutableMap.Builder<Movie, Movie>()
                .put(new Movie("Super cool movie", 2010), new Movie("Movie", 2002))
                .build();
    }
}
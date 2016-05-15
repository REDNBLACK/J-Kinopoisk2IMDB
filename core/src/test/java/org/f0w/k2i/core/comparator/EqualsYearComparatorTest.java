package org.f0w.k2i.core.comparator;

import com.google.common.collect.ImmutableMap;
import com.typesafe.config.ConfigFactory;
import org.f0w.k2i.core.model.entity.Movie;
import org.junit.Before;

public class EqualsYearComparatorTest extends BaseComparatorTest {
    @Before
    public void setUp() throws Exception {
        comparator = new MovieComparatorFactory(ConfigFactory.load()).make(MovieComparator.Type.YEAR_EQUALS);
        equalMovies = new ImmutableMap.Builder<Movie, Movie>()
                .put(new Movie("Super cool movie", 2010), new Movie("Not so cool movie", 2010))
                .build();
        notEqualMovies = new ImmutableMap.Builder<Movie, Movie>()
                .put(new Movie("Super cool movie", 2002), new Movie("Not so cool movie", 2010))
                .put(new Movie("Super cool movie", 2010), new Movie("Not so cool movie", 2002))
                .build();
    }
}
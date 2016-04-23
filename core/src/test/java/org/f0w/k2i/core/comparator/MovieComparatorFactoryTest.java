package org.f0w.k2i.core.comparator;

import com.google.common.collect.ImmutableMap;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.f0w.k2i.core.comparator.title.*;
import org.f0w.k2i.core.comparator.year.DeviationYearComparator;
import org.f0w.k2i.core.comparator.year.EqualsYearComparator;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;
import static org.f0w.k2i.core.comparator.MovieComparator.Type;
import static org.f0w.k2i.core.comparator.MovieComparator.Type.*;

public class MovieComparatorFactoryTest {
    private MovieComparatorFactory factory;

    @Before
    public void setUp() throws Exception {
        Config config = ConfigFactory.parseMap(ImmutableMap.of("year_deviation", 1));
        factory = new MovieComparatorFactory(config);
    }

    @Test
    public void testMake() throws Exception {
        final Map<Type, Class<? extends MovieComparator>> classMap =
                new ImmutableMap.Builder<Type, Class<? extends MovieComparator>>()
                        .put(YEAR_EQUALS, EqualsYearComparator.class)
                        .put(YEAR_DEVIATION, DeviationYearComparator.class)
                        .put(TITLE_CONTAINS, ContainsTitleComparator.class)
                        .put(TITLE_ENDS, EndsWithTitleComparator.class)
                        .put(TITLE_STARTS, StartsWithTitleComparator.class)
                        .put(TITLE_EQUALS, EqualsTitleComparator.class)
                        .put(TITLE_SMART, SmartTitleComparator.class)
                        .build();

        classMap.forEach((type, clazz) -> {
            MovieComparator instance = factory.make(type);

            assertTrue(clazz.isInstance(instance));
        });
    }
}
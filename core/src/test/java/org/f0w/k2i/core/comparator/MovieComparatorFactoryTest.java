package org.f0w.k2i.core.comparator;

import com.google.common.collect.ImmutableMap;
import com.typesafe.config.ConfigFactory;
import lombok.val;
import org.f0w.k2i.core.comparator.title.*;
import org.f0w.k2i.core.comparator.year.DeviationYearComparator;
import org.f0w.k2i.core.comparator.year.EqualsYearComparator;
import org.junit.Test;

import static org.f0w.k2i.core.comparator.MovieComparator.Type;
import static org.f0w.k2i.core.comparator.MovieComparator.Type.*;
import static org.junit.Assert.assertTrue;

public class MovieComparatorFactoryTest {
    private MovieComparatorFactory factory = new MovieComparatorFactory(ConfigFactory.load());

    @Test
    public void make() throws Exception {
        val classMap = new ImmutableMap.Builder<Type, Class<? extends MovieComparator>>()
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

    @Test(expected = NullPointerException.class)
    public void makeWithNull() throws Exception {
        factory.make(null);
    }
}
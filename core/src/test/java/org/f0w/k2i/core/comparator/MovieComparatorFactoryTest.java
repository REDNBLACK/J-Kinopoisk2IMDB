package org.f0w.k2i.core.comparator;

import com.google.common.collect.ImmutableMap;
import com.typesafe.config.ConfigFactory;
import lombok.val;
import org.junit.Test;

import static org.f0w.k2i.core.comparator.MovieComparator.Type;
import static org.f0w.k2i.core.comparator.MovieComparator.Type.*;
import static org.junit.Assert.assertTrue;

public class MovieComparatorFactoryTest {
    private MovieComparatorFactory factory = new MovieComparatorFactory(ConfigFactory.load());

    @Test
    public void makeSingle() throws Exception {
        val classMap = new ImmutableMap.Builder<Type, Class<? extends MovieComparator>>()
                .put(YEAR_EQUALS, EqualsYearComparator.class)
                .put(YEAR_DEVIATION, DeviationYearComparator.class)
                .put(TYPE_EQUALS, EqualsTypeComparator.class)
                .put(TYPE_ANY, AnyTypeComparator.class)
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

    @Test
    public void makeMultiple() throws Exception {
        factory.make(TYPE_ANY, TITLE_SMART, YEAR_DEVIATION);

        assertTrue(factory.make(new Type[]{TYPE_ANY}) instanceof AnyTypeComparator);
    }
}
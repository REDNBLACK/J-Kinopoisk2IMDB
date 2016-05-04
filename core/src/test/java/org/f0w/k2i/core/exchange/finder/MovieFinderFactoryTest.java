package org.f0w.k2i.core.exchange.finder;

import com.google.common.collect.ImmutableMap;
import com.typesafe.config.ConfigFactory;
import lombok.val;
import org.junit.Test;

import static org.f0w.k2i.core.exchange.finder.MovieFinder.Type;
import static org.f0w.k2i.core.exchange.finder.MovieFinder.Type.*;
import static org.junit.Assert.assertTrue;

public class MovieFinderFactoryTest {
    private final MovieFinderFactory factory = new MovieFinderFactory(ConfigFactory.load());

    @Test
    public void testMake() throws Exception {
        val classMap = new ImmutableMap.Builder<Type, Class<? extends MovieFinder>>()
                .put(MIXED, MixedMovieFinder.class)
                .put(XML, BasicMovieFinder.class)
                .put(JSON, BasicMovieFinder.class)
                .put(HTML, BasicMovieFinder.class)
                .build();

        classMap.forEach((type, clazz) -> {
            MovieFinder instance = factory.make(type);

            assertTrue(clazz.isInstance(instance));
            assertTrue(instance.getType().equals(type));
        });
    }

    @Test(expected = NullPointerException.class)
    public void testMakeWithNull() throws Exception {
        factory.make(null);
    }
}
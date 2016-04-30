package org.f0w.k2i.core.exchange.finder;

import com.google.common.collect.ImmutableMap;
import com.typesafe.config.ConfigFactory;
import org.junit.Test;

import java.util.Map;

import static org.f0w.k2i.core.exchange.finder.MovieFinder.Type;
import static org.f0w.k2i.core.exchange.finder.MovieFinder.Type.*;
import static org.junit.Assert.assertTrue;

public class MovieFinderFactoryTest {
    private static final MovieFinderFactory FACTORY = new MovieFinderFactory(ConfigFactory.load());

    @Test
    public void testMake() throws Exception {
        final Map<Type, Class<? extends MovieFinder>> classMap =
                new ImmutableMap.Builder<Type, Class<? extends MovieFinder>>()
                        .put(MIXED, MixedMovieFinder.class)
                        .put(XML, BasicMovieFinder.class)
                        .put(JSON, BasicMovieFinder.class)
                        .put(HTML, BasicMovieFinder.class)
                        .build();

        classMap.forEach((type, clazz) -> {
            MovieFinder instance = FACTORY.make(type);

            assertTrue(clazz.isInstance(instance));
            assertTrue(instance.getType().equals(type));
        });
    }
}
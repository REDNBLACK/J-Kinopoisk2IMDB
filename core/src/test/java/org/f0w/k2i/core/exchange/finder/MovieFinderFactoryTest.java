package org.f0w.k2i.core.exchange.finder;

import com.google.common.collect.ImmutableMap;
import com.typesafe.config.ConfigFactory;
import lombok.val;
import org.f0w.k2i.core.DocumentSourceType;
import org.junit.Test;

import static org.f0w.k2i.core.DocumentSourceType.*;
import static org.junit.Assert.assertTrue;

public class MovieFinderFactoryTest {
    private MovieFinderFactory factory = new MovieFinderFactory(ConfigFactory.load());

    @Test
    public void makeSingle() throws Exception {
        val classMap = new ImmutableMap.Builder<DocumentSourceType, Class<? extends MovieFinder>>()
                .put(XML, BasicMovieFinder.class)
                .put(JSON, BasicMovieFinder.class)
                .put(HTML, BasicMovieFinder.class)
                .build();

        classMap.forEach((type, clazz) -> {
            MovieFinder instance = factory.make(type);

            assertTrue(clazz.isInstance(instance));
            assertTrue(instance.getDocumentSourceType().equals(type));
        });
    }

    @Test
    public void makeMultiple() throws Exception {
        assertTrue(factory.make(XML, JSON, HTML) instanceof MixedMovieFinder);

        assertTrue(factory.make(new DocumentSourceType[]{XML}) instanceof BasicMovieFinder);
    }
}
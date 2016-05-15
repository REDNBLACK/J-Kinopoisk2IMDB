package org.f0w.k2i.core.util.parser;

import com.google.common.collect.ImmutableMap;
import lombok.val;
import org.f0w.k2i.TestHelper;
import org.f0w.k2i.core.DocumentSourceType;
import org.junit.Test;

import static org.f0w.k2i.core.DocumentSourceType.*;
import static org.junit.Assert.assertTrue;

public class MovieParsersTest {
    @Test
    public void isConstructorPrivate() throws Exception {
        assertTrue(TestHelper.isConstructorPrivate(MovieParsers.class));

        TestHelper.callPrivateConstructor(MovieParsers.class);
    }

    @Test
    public void ofFormat() throws Exception {
        val classMap = new ImmutableMap.Builder<DocumentSourceType, Class<? extends MovieParser>>()
                .put(XML, XMLMovieParser.class)
                .put(JSON, JSONMovieParser.class)
                .put(HTML, HTMLMovieParser.class)
                .build();

        classMap.forEach((format, clazz) -> {
            MovieParser instance = MovieParsers.ofSourceType(format);

            assertTrue(clazz.isInstance(instance));
        });
    }

    @Test
    public void fileParser() throws Exception {
        assertTrue(FileMovieParser.class.isInstance(MovieParsers.fileParser()));
    }
}
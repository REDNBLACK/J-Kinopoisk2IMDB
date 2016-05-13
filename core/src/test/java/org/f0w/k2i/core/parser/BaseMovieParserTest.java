package org.f0w.k2i.core.parser;

import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public abstract class BaseMovieParserTest {
    protected MovieParser parser;

    @Test
    public void parseWithNullData() throws Exception {
        assertEquals(parser.parse(null), Collections.emptyList());
    }

    @Test
    public void parseWithEmptyData() throws Exception {
        assertEquals(parser.parse(""), Collections.emptyList());
    }
}
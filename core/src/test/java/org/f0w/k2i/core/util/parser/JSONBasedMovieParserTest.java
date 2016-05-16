package org.f0w.k2i.core.util.parser;

import org.junit.Test;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

public abstract class JSONBasedMovieParserTest extends BaseMovieParserTest {
    @Test
    public void parseWithEmptyJSONData() throws Exception {
        assertEquals(Collections.emptyList(), parser.parse("{}"));
    }
}

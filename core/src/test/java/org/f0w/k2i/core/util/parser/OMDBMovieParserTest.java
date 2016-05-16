package org.f0w.k2i.core.util.parser;

import org.f0w.k2i.core.DocumentSourceType;
import org.f0w.k2i.core.model.entity.Movie;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.f0w.k2i.TestHelper.getResourceContents;
import static org.junit.Assert.assertEquals;

public class OMDBMovieParserTest extends JSONBasedMovieParserTest {
    @Before
    public void setUp() throws Exception {
        parser = MovieParsers.ofSourceType(DocumentSourceType.OMDB);
    }

    @Test
    public void parseWithValidData() throws Exception {
        assertEquals(
                Collections.singletonList(new Movie("Inception", 2010, Movie.Type.MOVIE, null, "tt1375666")),
                parser.parse(getResourceContents("parser/test_data_omdb1.json"))
        );

        assertEquals(
                Collections.singletonList(new Movie("Breaking Bad", 2008, Movie.Type.SERIES, null, "tt0903747")),
                parser.parse(getResourceContents("parser/test_data_omdb2.json"))
        );

        assertEquals(
                Collections.singletonList(new Movie("Jackass Number Two", 2006, Movie.Type.DOCUMENTARY, null, "tt0493430")),
                parser.parse(getResourceContents("parser/test_data_omdb3.json"))
        );

        assertEquals(
                Collections.singletonList(new Movie("Kung Fury", 2015, Movie.Type.SHORT, null, "tt3472226")),
                parser.parse(getResourceContents("parser/test_data_omdb4.json"))
        );
    }

    @Test
    public void parseWithInvalidJSONData() throws Exception {
        assertEquals(Collections.emptyList(), parser.parse("{\"Response\":\"False\",\"Error\":\"Movie not found!\"}"));
    }
}
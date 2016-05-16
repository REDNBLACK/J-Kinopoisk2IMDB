package org.f0w.k2i.core.util.parser;

import lombok.val;
import org.f0w.k2i.core.DocumentSourceType;
import org.f0w.k2i.core.model.entity.Movie;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.f0w.k2i.TestHelper.getResourceContents;
import static org.junit.Assert.assertEquals;

public class IMDBXMLMovieParserTest extends BaseMovieParserTest {
    @Before
    public void setUp() throws Exception {
        parser = MovieParsers.ofSourceType(DocumentSourceType.IMDB_XML);
    }

    @Test
    public void parseWithValidData() throws Exception {
        val expected = Arrays.asList(
                new Movie("Inception: The IMAX Experience", 2010, Movie.Type.MOVIE, null, "tt1375666"),
                new Movie("Inception: Motion Comics", 2010, Movie.Type.SERIES, null, "tt1790736"),
                new Movie("Inception: The Cobol Job", 2010, Movie.Type.SHORT, null, "tt5295894"),
                new Movie("Inception: 4Movie Premiere Special", 2010, Movie.Type.DOCUMENTARY, null, "tt1686778"),
                new Movie("Inception: In 60 Seconds", 2013, Movie.Type.SHORT, null, "tt3262402"),
                new Movie("On Inception (TOI and MOI)", 2011, Movie.Type.SHORT, null, "tt4341988"),
                new Movie("WWA: The Inception", 2001, Movie.Type.MOVIE, null, "tt0311992"),
                new Movie("Fraud in the Inception", 2013, Movie.Type.DOCUMENTARY, null, "tt2278951"),
                new Movie("Inception: Jump Right Into the Action", 2010, Movie.Type.DOCUMENTARY, null, "tt5295990"),
                new Movie("Inception of a lost Art", 2013, Movie.Type.MOVIE, null, "tt3563778"),
                new Movie("Inception of Chaos", 2012, Movie.Type.SHORT, null, "tt2762020"),
                new Movie("Inception (Sweded)", 2011, Movie.Type.SHORT, null, "tt2251371"),
                new Movie("Needle Drop: Inception", 2016, Movie.Type.SHORT, null, "tt4650070"),
                new Movie("The Inception", 2016, Movie.Type.SHORT, null, "tt5526902"),
                new Movie("The Inception", 2006, Movie.Type.MOVIE, null, "tt0864796"),
                new Movie("Miss Conception", 2008, Movie.Type.MOVIE, null, "tt0985593"),
                new Movie("Conception", 2005, Movie.Type.MOVIE, null, "tt0492950"),
                new Movie("The Immaculate Conception of Little Dizzle", 2009, Movie.Type.MOVIE, null, "tt1039786"),
                new Movie("Conception", 2011, Movie.Type.MOVIE, null, "tt1619277"),
                new Movie("Immaculate Conception", 1992, Movie.Type.MOVIE, null, "tt0104489")
        );

        assertEquals(expected, parser.parse(getResourceContents("parser/test_data_imdb.xml")));
    }
}
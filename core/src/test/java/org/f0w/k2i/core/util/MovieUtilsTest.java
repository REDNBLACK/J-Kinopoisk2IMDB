package org.f0w.k2i.core.util;

import org.f0w.k2i.TestHelper;
import org.junit.Test;

import static org.f0w.k2i.core.util.MovieUtils.*;
import static org.junit.Assert.*;

public class MovieUtilsTest {
    @Test
    public void testPrivateConstructor() throws Exception {
        assertTrue(TestHelper.isConstructorPrivate(MovieUtils.class));

        TestHelper.callPrivateConstructor(MovieUtils.class);
    }

    @Test
    public void testParseTitle() throws Exception {
        assertEquals("Inception", parseTitle("Inception"));
        assertEquals("Inception", parseTitle("    Inception    "));
        assertEquals("Inception", parseTitle("Inception    "));
        assertEquals("Inception", parseTitle("    Inception"));
        assertEquals(
                "Операция Ы и другие приключения Шурика", parseTitle("Операция «Ы» и другие «приключения» Шурика")
        );
        assertEquals("null", parseTitle(null));
        assertEquals("null", parseTitle(""));
        assertEquals("null", parseTitle(" "));
        assertEquals("null", parseTitle("    "));
        assertEquals("fallback title", parseTitle("", "fallback title"));
        assertEquals("original title", parseTitle("original title", "fallback title"));
        assertEquals("null", parseTitle(null, null));
    }

    @Test
    public void testParseYear() throws Exception {
        assertEquals(2005, parseYear("2005"));
        assertEquals(2005, parseYear("    2005    "));
        assertEquals(2005, parseYear("    2005"));
        assertEquals(2005, parseYear("2005    "));
        assertEquals(2010, parseYear("201000"));
        assertEquals(1, parseYear("0001"));
        assertEquals(0, parseYear(null));
        assertEquals(0, parseYear("0"));
        assertEquals(0, parseYear("not a year"));
        assertEquals(2015, parseYear("2015, year with text"));
        assertEquals(0, parseYear("year with text before 2015"));
    }

    @Test
    public void testParseIMDBId() throws Exception {
        assertEquals("tt123456", parseIMDBId("tt123456    "));
        assertEquals("tt123456", parseIMDBId("    tt123456    "));
        assertEquals("tt123456", parseIMDBId("    tt123456"));
        assertEquals(null, parseIMDBId(null));
        assertEquals(null, parseIMDBId(""));
        assertEquals(null, parseIMDBId("    "));
        assertEquals(null, parseIMDBId("tt"));
        assertEquals("tt1", parseIMDBId("tt1"));
    }

    @Test
    public void testParseRating() throws Exception {
        assertEquals((Integer) 8, parseRating("8"));
        assertEquals((Integer) 8, parseRating("8    "));
        assertEquals((Integer) 8, parseRating("    8    "));
        assertEquals((Integer) 8, parseRating("    8"));
        assertEquals(null, parseRating("11"));
        assertEquals(null, parseRating("123123"));
        assertEquals(null, parseRating(null));
        assertEquals(null, parseRating(""));
        assertEquals(null, parseRating("0"));
        assertEquals(null, parseRating("null"));
        assertEquals(null, parseRating("zero"));
        assertEquals(null, parseRating("not a rating"));
    }

    @Test
    public void testIsEmptyTitle() throws Exception {
        assertFalse(isEmptyTitle("Inception"));
        assertTrue(isEmptyTitle("null"));
    }

    @Test
    public void testIsEmptyYear() throws Exception {
        assertFalse(isEmptyYear(2010));
        assertTrue(isEmptyYear(0));
    }

    @Test
    public void testIsEmptyIMDBId() throws Exception {
        assertFalse(isEmptyIMDBId("tt210240"));
        assertTrue(isEmptyIMDBId(null));
    }

    @Test
    public void testIsEmptyRating() throws Exception {
        assertFalse(isEmptyRating(10));
        assertTrue(isEmptyRating(null));
    }
}
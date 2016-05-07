package org.f0w.k2i.core.util;

import org.f0w.k2i.TestHelper;
import org.f0w.k2i.core.model.entity.Movie;
import org.f0w.k2i.core.util.exception.KinopoiskToIMDBException;
import org.junit.Test;

import java.net.URL;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.f0w.k2i.core.util.MovieUtils.*;
import static org.junit.Assert.*;

public class MovieUtilsTest {
    @Test
    public void isConstructorPrivate() throws Exception {
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
                "Операция Ы и другие приключения Шурика", parseTitle(null, "Операция «Ы» и другие «приключения» Шурика")
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

    @Test(expected = KinopoiskToIMDBException.class)
    public void parseMoviesFromEmptyFile() throws Exception {
        URL resource = getClass().getClassLoader().getResource("parse_movies_file_test_data/empty_file.xls");
        parseMovies(Paths.get(resource.toURI()));
    }

    @Test(expected = KinopoiskToIMDBException.class)
    public void parseMoviesFromInvalidFile() throws Exception {
        URL resource = getClass().getClassLoader().getResource("parse_movies_file_test_data/invalid_file.xls");
        parseMovies(Paths.get(resource.toURI()));
    }

    @Test
    public void parseMoviesFromValidFileExportedFromLists() throws Exception {
        URL resource = getClass().getClassLoader().getResource("parse_movies_file_test_data/exported_from_lists.xls");
        List<Movie> expected = Arrays.asList(
                new Movie("Law Abiding Citizen", 2009, 10),
                new Movie("Sin City", 2005, 10),
                new Movie("Kokaku kidotai: Stand Alone Complex", 2002, 10),
                new Movie("Hannibal", 2013, 10),
                new Movie("Intouchables", 2011, 10),
                new Movie("South Park", 1997, 10),
                new Movie("Breaking Bad", 2008, 10),
                new Movie("Они сражались за Родину", 1975, null, null),
                new Movie("Белый Бим Черное ухо", 1976, null, null),
                new Movie("Судьба человека", 1959, null, null),
                new Movie("Летят журавли", 1957, null, null),
                new Movie("Москва слезам не верит", 1979, null, null),
                new Movie("Офицеры", 1971, null, null),
                new Movie("Собачье сердце", 1988, 10),
                new Movie("Шерлок Холмс и доктор Ватсон: Знакомство", 1979, null, null),
                new Movie("Приключения Шерлока Холмса и доктора Ватсона: Собака Баскервилей", 1981, null, null),
                new Movie("...А зори здесь тихие", 1972, null, null),
                new Movie("Бриллиантовая рука", 1968, 8),
                new Movie("В бой идут одни старики", 1973, 8),
                new Movie("Иван Васильевич меняет профессию", 1973, 8),
                new Movie("Кавказская пленница, или Новые приключения Шурика", 1966, 8),
                new Movie("Операция Ы и другие приключения Шурика", 1965, 9),
                new Movie("Белое солнце пустыни", 1969, 7)
        );

        assertEquals(expected, parseMovies(Paths.get(resource.toURI())));
    }

    @Test
    public void parseMoviesFromValidFileExportedFromProfile() throws Exception {
        URL resource = getClass().getClassLoader().getResource("parse_movies_file_test_data/exported_from_profile.xls");
        List<Movie> expected = Arrays.asList(
                new Movie("Standoff", 2015, 6),
                new Movie("The Trust", 2016, 6),
                new Movie("Space Cop", 2016, 5),
                new Movie("Как поднять миллион. Исповедь Z@drota", 2014, 7),
                new Movie("Cellular", 2004, 7),
                new Movie("I Smile Back", 2015, 5),
                new Movie("Childhood's End", 2015, 5),
                new Movie("D'Ardennen", 2015, 6),
                new Movie("Joy", 2015, 6),
                new Movie("The Boy", 2016, 6),
                new Movie("The Merry Gentleman", 2008, 5),
                new Movie("Every Secret Thing", 2014, 4),
                new Movie("The 5th Wave", 2016, 5),
                new Movie("Triple 9", 2016, 7),
                new Movie("Чемпионы: Быстрее. Выше. Сильнее", 2016, 6),
                new Movie("An Inspector Calls", 2015, 7),
                new Movie("The Boy", 2015, 3),
                new Movie("Тряпичный союз", 2015, 4),
                new Movie("Crystal Fairy & the Magical Cactus and 2012", 2013, 4),
                new Movie("La fille de Monaco", 2008, 5),
                new Movie("The Danish Girl", 2015, 7),
                new Movie("Deadpool", 2016, 7),
                new Movie("День выборов 2", 2015, 6),
                new Movie("Kung Fu Panda 3", 2016, 6),
                new Movie("Вакантна жизнь шеф-повара", 2015, 5)
        );

        assertEquals(expected, parseMovies(Paths.get(resource.toURI())));
    }
}
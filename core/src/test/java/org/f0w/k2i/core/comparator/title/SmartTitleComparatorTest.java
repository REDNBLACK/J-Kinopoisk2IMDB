package org.f0w.k2i.core.comparator.title;

import org.f0w.k2i.core.comparator.MovieComparator;
import org.f0w.k2i.core.model.entity.Movie;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SmartTitleComparatorTest {
    private final MovieComparator comparator = new SmartTitleComparator();

    @Test
    public void areEqualDifferentTitles() throws Exception {
        assertFalse(comparator.areEqual(
                new Movie("Inception", 2010),
                new Movie("Hannibal", 2013)
        ));
    }

    @Test
    public void areEqualEqualTitles() throws Exception {
        assertTrue(comparator.areEqual(
                new Movie("Inception", 2010),
                new Movie("Inception", 2010)
        ));
    }

    @Test
    public void areEqualWithoutCommas() throws Exception {
        assertTrue(comparator.areEqual(
                new Movie("The boy, who lived", 2010),
                new Movie("The boy who lived", 2010)
        ));
    }

    @Test
    public void areEqualWithoutColon() throws Exception {
        assertTrue(comparator.areEqual(
                new Movie("Independence Day: Resurgence", 2016),
                new Movie("Independence Day Resurgence", 2016)
        ));
    }

    @Test
    public void areEqualWithoutApostrophesAndQuotes() throws Exception {
        assertTrue(comparator.areEqual(
                new Movie("Операция «Ы» и другие «приключения» Шурика", 1965),
                new Movie("Операция Ы и другие приключения Шурика", 1965)
        ));

        assertTrue(comparator.areEqual(
                new Movie("Белый Бим Черное ухо", 2010),
                new Movie("Belyy Bim Chernoe ukho", 2010)
        ));

        assertTrue(comparator.areEqual(
                new Movie("Childhood's 'End'", 2015),
                new Movie("Childhoods End", 2015)
        ));
    }

    @Test
    public void areEqualWithoutForeignAccents() {
        assertTrue(comparator.areEqual(
                new Movie("Kôkaku kidôtai: Stand Alone Complex", 2002),
                new Movie("Kokaku kidotai: Stand Alone Complex", 2002)
        ));
    }

    @Test
    public void areEqualWithPartBeforeDashSymbol() {
        assertTrue(comparator.areEqual(
                new Movie("Super Movie - Again", 2002),
                new Movie("Super Movie", 2002)
        ));
    }

    @Test
    public void areEqualWithPartAfterDashSymbol() {
        assertTrue(comparator.areEqual(
                new Movie("Super Movie - Again - And again", 2002),
                new Movie("And again", 2002)
        ));
    }

    @Test
    public void areEqualWithThePrefix() {
        assertTrue(comparator.areEqual(
                new Movie("The Godfather", 1972),
                new Movie("Godfather", 1972)
        ));
    }

    @Test
    public void areEqualWithWhitespaceNormalized() {
        assertTrue(comparator.areEqual(
                new Movie("Super Cool\tMovie So Good\r\nEven Better\nAnd more\rAnd moar", 2010),
                new Movie("Super Cool Movie So Good Even Better And more And moar", 2010)
        ));
    }

    @Test
    public void areEqualWeaklyTransliteratedAndCapitalized() {
        assertTrue(comparator.areEqual(
                new Movie("ЛУЧШИЙ фильм НА ВСЕМ свете", 2010),
                new Movie("Luchshii film na vsem svete", 2010)
        ));
    }

    @Test
    public void areEqualWithNumericRepresentedAsWords() {
        assertTrue(comparator.areEqual(
                new Movie("Iron Man 3", 2013),
                new Movie("Iron Man Three", 2013)
        ));

        assertTrue(comparator.areEqual(
                new Movie("Fantastic 4", 2015),
                new Movie("Fantastic Four", 2015)
        ));

        assertTrue(comparator.areEqual(
                new Movie("Fahrenheit 451", 2000),
                new Movie("Fahrenheit Four hundred Fifty One", 2000)
        ));
    }

    @Test
    public void areEqualWithReplacedSymbols() {
        assertTrue(comparator.areEqual(
                new Movie("Ernest et Célestine", 2012),
                new Movie("Ernest & Célestine", 2012)
        ));

        assertTrue(comparator.areEqual(
                new Movie("Mr. Peabody & Sherman", 2014),
                new Movie("Mr. Peabody and Sherman", 2014)
        ));
    }
}
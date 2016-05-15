package org.f0w.k2i.core.comparator;

import com.typesafe.config.ConfigFactory;
import org.f0w.k2i.core.model.entity.Movie;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SmartTitleComparatorTest {
    private MovieComparator comparator = new MovieComparatorFactory(ConfigFactory.load()).make(MovieComparator.Type.TITLE_SMART);

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

        assertTrue(comparator.areEqual(
                new Movie("inception", 2010),
                new Movie("Inception", 2010)
        ));

        assertTrue(comparator.areEqual(
                new Movie("Мы из будущего 2", 2010),
                new Movie("Мы из будущего 2", 2010)
        ));
    }

    @Test
    public void areEqualWithRemovedSymbols() throws Exception {
        assertTrue(comparator.areEqual(
                new Movie("The boy, who lived", 2010),
                new Movie("The boy who lived", 2010)
        ));

        assertTrue(comparator.areEqual(
                new Movie("Independence Day: Resurgence", 2016),
                new Movie("Independence Day Resurgence", 2016)
        ));

        assertTrue(comparator.areEqual(
                new Movie("Lock Out", 2012),
                new Movie("Lockout", 2012)
        ));

        assertTrue(comparator.areEqual(
                new Movie("The Incredibles 3-D", 2004),
                new Movie("The Incredibles", 2004)
        ));
    }

    @Test
    public void areEqualWithoutApostrophesAndQuotes() throws Exception {
        assertTrue(comparator.areEqual(
                new Movie("Операция «Ы» и другие «приключения» Шурика", 1965),
                new Movie("Операция Ы и другие приключения Шурика", 1965)
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
    public void areEqualWithPartBeforeOneOfSeparatingSymbols() {
        assertTrue(comparator.areEqual(
                new Movie("Super Movie - Again", 2002),
                new Movie("Super Movie", 2002)
        ));

        assertTrue(comparator.areEqual(
                new Movie("Taare Zameen Par: Every Child Is Special", 2007),
                new Movie("Taare Zameen Par", 2007)
        ));
    }

    @Test
    public void areEqualWithPartAfterOneOfSeparatingSymbols() {
        assertTrue(comparator.areEqual(
                new Movie("Super Movie - Again - And again", 2002),
                new Movie("And again", 2002)
        ));

        assertTrue(comparator.areEqual(
                new Movie("4: Rise of the Silver Surfer", 2007),
                new Movie("Rise of the Silver Surfer", 2007)
        ));
    }

    @Test
    public void areEqualWithThePrefixes() {
        assertTrue(comparator.areEqual(
                new Movie("The Godfather", 1972),
                new Movie("Godfather", 1972)
        ));
    }

    @Test
    public void areEqualWithThePostfixes() {
        assertTrue(comparator.areEqual(
                new Movie("Shaman kingu", 2001),
                new Movie("Shaman king", 2001)
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
    public void areEqualTranslit() {
        assertTrue(comparator.areEqual(
                new Movie("Остров сокровищ", 1988),
                new Movie("Ostrov sokrovishch", 1988)
        ));

        assertTrue(comparator.areEqual(
                 new Movie("Белый Бим Черное ухо", 1976),
                 new Movie("Belyy Bim Chernoe ukho", 1976)
        ));

        assertTrue(comparator.areEqual(
                new Movie("Шерлок Холмс и доктор Ватсон: Знакомство", 1979),
                new Movie("Sherlok Kholms i doktor Vatson: Znakomstvo", 1979)
        ));
    }

    @Test
    public void areEqualWithoutSpecialSymbols() {
        assertTrue(comparator.areEqual(
                new Movie("...А зори здесь тихие", 1975),
                new Movie("А зори здесь тихие", 1975)
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

        assertTrue(comparator.areEqual(
                new Movie("Легенда №17", 2013),
                new Movie("Легенда No. 17", 2013)
        ));

        assertTrue(comparator.areEqual(
                new Movie("Yip Man", 2008),
                new Movie("Ip Man", 2008)
        ));

        assertTrue(comparator.areEqual(
                new Movie("The Lion King 1½", 2004),
                new Movie("The Lion King 1 1/2", 2004)
        ));

        assertTrue(comparator.areEqual(
                new Movie("Nanny McPhee and the Big Bang", 2010),
                new Movie("Nanny McPhee et le Big Bang", 2010)
        ));
    }
}
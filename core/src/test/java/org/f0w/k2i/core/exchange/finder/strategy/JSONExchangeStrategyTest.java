package org.f0w.k2i.core.exchange.finder.strategy;

import com.google.common.io.Resources;
import org.f0w.k2i.core.model.entity.Movie;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class JSONExchangeStrategyTest extends BaseExchangeStrategyTest {
    @Before
    public void setUp() throws Exception {
        strategy = new JSONExchangeStrategy();
    }

    @Test
    public void parseSearchResultWithValidData() throws Exception {
        URL resource = getClass().getClassLoader().getResource("strategy_test_data/test_data.json");
        String data = Resources.toString(resource, StandardCharsets.UTF_8);
        List<Movie> expected = Arrays.asList(
                new Movie("Inception", 2010, "tt1375666"),
                new Movie("Inception: Motion Comics", 2010, "tt1790736"),
                new Movie("Inception: The Cobol Job", 2010, "tt5295894"),
                new Movie("Inception: 4Movie Premiere Special", 2010, "tt1686778"),
                new Movie("Inception: In 60 Seconds", 2013, "tt3262402"),
                new Movie("On Inception (TOI and MOI)", 2011, "tt4341988"),
                new Movie("WWA: The Inception", 2001, "tt0311992"),
                new Movie("Fraud in the Inception: Who killed Robert Hamlin and Dorothy Grega", 2013, "tt2278951"),
                new Movie("Inception: Jump Right Into the Action", 2010, "tt5295990"),
                new Movie("Inception of a lost Art", 2013, "tt3563778"),
                new Movie("Inception of Chaos", 2012, "tt2762020"),
                new Movie("Inception (Sweded)", 2011, "tt2251371"),
                new Movie("Needle Drop: Inception", 2016, "tt4650070"),
                new Movie("The Inception", 2016, "tt5526902"),
                new Movie("The Inception", 2006, "tt0864796"),
                new Movie("Miss Conception", 2008, "tt0985593"),
                new Movie("Conception", 2005, "tt0492950"),
                new Movie("The Immaculate Conception of Little Dizzle", 2009, "tt1039786"),
                new Movie("Conception", 2011, "tt1619277"),
                new Movie("Immaculate Conception", 1992, "tt0104489")
        );

        assertEquals(expected, strategy.parseSearchResult(data));
    }

    @Test
    public void parseSearchResultWithEmptyJSONData() throws Exception {
        assertEquals(strategy.parseSearchResult("{}"), Collections.emptyList());
    }
}
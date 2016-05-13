package org.f0w.k2i.core.exchange.finder.strategy;

import org.f0w.k2i.MovieTestData;
import org.f0w.k2i.ResponseMock;
import org.f0w.k2i.core.model.entity.Movie;
import org.junit.Test;

import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public abstract class BaseExchangeStrategyTest {
    protected ExchangeStrategy strategy;

    @Test
    public void buildSearchURLWithValidMovies() throws Exception {
        for (Movie movie : MovieTestData.MOVIES_LIST) {
            URL searchURL = strategy.buildSearchURL(movie);
            String decodedURL = URLDecoder.decode(searchURL.toString(), StandardCharsets.UTF_8.toString());

            assertTrue(decodedURL.contains("q=" + movie.getTitle()));
        }
    }

    @Test(expected = NullPointerException.class)
    public void buildSearchURLWithNullMovie() throws Exception {
        strategy.buildSearchURL(null);
    }

    @Test(expected = NullPointerException.class)
    public void parseSearchResultWithNullData() throws Exception {
        strategy.parseSearchResult(null);
    }

    @Test
    public void parseSearchResultWithEmpyData() throws Exception {
        assertEquals(Collections.emptyList(), strategy.parseSearchResult(new ResponseMock("")));
    }
}

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
    public void buildRequestWithValidMovies() throws Exception {
        for (Movie movie : MovieTestData.MOVIES_LIST) {
            URL searchURL = strategy.buildRequest(movie).url();
            String decodedURL = URLDecoder.decode(searchURL.toString(), StandardCharsets.UTF_8.toString());

            assertTrue(decodedURL.contains("q=" + movie.getTitle()));
        }
    }

    @Test(expected = NullPointerException.class)
    public void buildRequestWithNullMovie() throws Exception {
        strategy.buildRequest(null);
    }

    @Test(expected = NullPointerException.class)
    public void parseResponseWithNullData() throws Exception {
        strategy.parseResponse(null);
    }

    @Test
    public void parseResponseWithEmpyData() throws Exception {
        assertEquals(Collections.emptyList(), strategy.parseResponse(new ResponseMock("")));
    }
}

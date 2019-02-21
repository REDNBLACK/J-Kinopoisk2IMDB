package org.f0w.k2i.core.exchange.finder.strategy;

import com.google.common.collect.ImmutableMap;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.f0w.k2i.MovieTestData;
import org.f0w.k2i.core.model.entity.Movie;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.*;

public class OMDBExchangeStrategyTest extends BaseExchangeStrategyTest {
    @Before
    public void setUp() throws Exception {
        Config config = ConfigFactory.parseMap(ImmutableMap.of("omdbApiKey", "testApiKey"));
        strategy = new OMDBExchangeStrategy(config);
    }

    @Test
    @Override
    public void buildRequestWithValidMovies() throws Exception {
        for (Movie movie : MovieTestData.MOVIES_LIST) {
            URL searchURL = strategy.buildRequest(movie).url();
            String decodedURL = URLDecoder.decode(searchURL.toString(), StandardCharsets.UTF_8.toString());

            assertTrue(decodedURL.contains("t=" + movie.getTitle()));
        }
    }
}

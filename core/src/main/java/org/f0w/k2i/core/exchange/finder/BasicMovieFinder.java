package org.f0w.k2i.core.exchange.finder;

import com.typesafe.config.Config;
import org.f0w.k2i.core.exchange.AbstractExchangeable;
import org.f0w.k2i.core.exchange.finder.strategy.ExchangeStrategy;
import org.f0w.k2i.core.model.entity.Movie;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Deque;
import java.util.LinkedList;

public final class BasicMovieFinder extends AbstractExchangeable<Movie, Deque<Movie>> implements MovieFinder {
    protected static final Logger LOG = LoggerFactory.getLogger(BasicMovieFinder.class);

    private final Type type;

    private final ExchangeStrategy exchangeStrategy;

    private final Config config;

    public BasicMovieFinder(Type type, ExchangeStrategy exchangeStrategy, Config config) {
        this.type = type;
        this.exchangeStrategy = exchangeStrategy;
        this.config = config;
    }

    /**
     * Open movie search page using {@link Movie#year} and {@link Movie#title}.
     *
     * @param movie Movie to use
     * @throws IOException If an I/O error occurs
     */
    @Override
    public void sendRequest(Movie movie) throws IOException {
        final URL movieSearchLink = exchangeStrategy.buildSearchURL(movie);

        Connection request = Jsoup.connect(movieSearchLink.toString())
                .userAgent(config.getString("user_agent"))
                .timeout(config.getInt("timeout"));

        LOG.debug(
                "Sending request, to url: {}, with headers: {}", request.request().url(), request.request().headers()
        );

        response = request.execute();

        LOG.debug("Got response, status code: {}, headers: {}", response.statusCode(), response.headers());
    }

    /**
     * Parses search page using {@link ExchangeStrategy#parseSearchResult(String)}
     *
     * @return Deque of found movies
     */
    @Override
    public Deque<Movie> getProcessedResponse() {
        return new LinkedList<>(exchangeStrategy.parseSearchResult(response.body()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type getType() {
        return type;
    }
}

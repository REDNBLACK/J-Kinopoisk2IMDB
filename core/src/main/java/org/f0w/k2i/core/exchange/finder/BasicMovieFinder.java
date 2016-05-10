package org.f0w.k2i.core.exchange.finder;

import com.typesafe.config.Config;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.f0w.k2i.core.exchange.AbstractExchangeable;
import org.f0w.k2i.core.exchange.finder.strategy.ExchangeStrategy;
import org.f0w.k2i.core.model.entity.Movie;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;

@Slf4j
public final class BasicMovieFinder extends AbstractExchangeable<Movie, Deque<Movie>> implements MovieFinder {
    @NonNull
    private final Type type;

    @NonNull
    private final ExchangeStrategy exchangeStrategy;

    @NonNull
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
    public void sendRequest(@NonNull Movie movie) throws IOException {
        val movieSearchLink = exchangeStrategy.buildSearchURL(movie);

        Connection client = Jsoup.connect(movieSearchLink.toString())
                .userAgent(config.getString("user_agent"))
                .timeout(config.getInt("timeout"));

        log.debug(
                "Sending request, to url: {}, with headers: {}", client.request().url(), client.request().headers()
        );

        setResponse(client.execute());

        log.debug("Got response, status code: {}, headers: {}", response.statusCode(), response.headers());
    }

    /**
     * Parses search page using {@link ExchangeStrategy#parseSearchResult(String)}
     *
     * @return Deque of found movies
     */
    @Override
    public Deque<Movie> getProcessedResponse() {
        return new ArrayDeque<>(exchangeStrategy.parseSearchResult(getResponseBody()));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type getType() {
        return type;
    }
}

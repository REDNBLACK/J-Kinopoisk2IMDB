package org.f0w.k2i.core.exchange.finder;

import com.typesafe.config.Config;
import org.f0w.k2i.core.model.entity.Movie;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

abstract class AbstractMovieFinder implements MovieFinder {
    protected static final Logger LOG = LoggerFactory.getLogger(AbstractMovieFinder.class);

    protected Config config;

    protected Connection.Response response;

    public AbstractMovieFinder(Config config) {
        this.config = config;
    }

    @Override
    public void sendRequest(Movie movie) throws IOException {
        Connection request = Jsoup.connect(buildSearchQuery(movie))
                .userAgent(config.getString("user_agent"))
                .timeout(config.getInt("timeout"));

        LOG.debug(
                "Sending request, to url: {}, with headers: {}", request.request().url(), request.request().headers()
        );

        response = request.execute();

        LOG.debug("Got response, status code: {}, headers: {}", response.statusCode(), response.headers());
    }

    protected abstract String buildSearchQuery(Movie movie);

    protected abstract List<Movie> parseSearchResult(final String result);

    @Override
    public Connection.Response getRawResponse() {
        return response;
    }

    @Override
    public Deque<Movie> getProcessedResponse() {
        return new LinkedList<>(parseSearchResult(response.body()));
    }
}

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

/**
 * Base class implementing {@link MovieFinder} interface.
 */
abstract class AbstractMovieFinder implements MovieFinder {
    protected static final Logger LOG = LoggerFactory.getLogger(AbstractMovieFinder.class);

    protected Config config;

    protected Connection.Response response;

    public AbstractMovieFinder(Config config) {
        this.config = config;
    }

    /**
     * Open movie search page using {@link Movie#year} and {@link Movie#title}.
     * @param movie Movie to use
     * @throws IOException If an I/O error occurs
     */
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

    /**
     * Build search query URL based on movie fields, implementation specific to each MovieFinder.
     * @param movie Movie which fields to use
     * @return Search query string
     */
    protected abstract String buildSearchQuery(Movie movie);

    /**
     * Parses search result response, implementation specific to each MovieFinder
     * @param result Search result
     * @return List of movies
     */
    protected abstract List<Movie> parseSearchResult(final String result);

    /** {@inheritDoc} */
    @Override
    public Connection.Response getRawResponse() {
        return response;
    }

    /**
     * Parses search page using {@link this#parseSearchResult(String)}
     * @return Deque of found movies
     */
    @Override
    public Deque<Movie> getProcessedResponse() {
        return new LinkedList<>(parseSearchResult(response.body()));
    }
}

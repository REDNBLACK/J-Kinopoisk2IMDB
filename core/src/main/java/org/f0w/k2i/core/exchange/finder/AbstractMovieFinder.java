package org.f0w.k2i.core.exchange.finder;

import com.typesafe.config.Config;
import org.f0w.k2i.core.model.entity.Movie;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URL;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static org.f0w.k2i.core.util.MovieUtils.*;
import static org.f0w.k2i.core.util.HttpUtils.buildURL;

/**
 * Base class implementing {@link MovieFinder} interface.
 */
abstract class AbstractMovieFinder implements MovieFinder {
    protected static final Logger LOG = LoggerFactory.getLogger(AbstractMovieFinder.class);

    private final Config config;

    private final String searchLink;

    private Connection.Response response;

    public AbstractMovieFinder(Config config, String searchLink) {
        this.config = config;
        this.searchLink = searchLink;
    }

    /**
     * Open movie search page using {@link Movie#year} and {@link Movie#title}.
     *
     * @param movie Movie to use
     * @throws IOException If an I/O error occurs
     */
    @Override
    public void sendRequest(Movie movie) throws IOException {
        final URL movieSearchLink = buildURL(searchLink, buildSearchQuery(movie));

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
     * {@inheritDoc}
     */
    @Override
    public Connection.Response getRawResponse() {
        return response;
    }

    /**
     * Parses search page using {@link this#parseSearchResult(String)}
     *
     * @return Deque of found movies
     */
    @Override
    public Deque<Movie> getProcessedResponse() {
        return new LinkedList<>(parseSearchResult(response.body()));
    }

    /**
     * Build search query parameters map based on movie fields, implementation specific to each MovieFinder.
     *
     * @param movie Movie which fields to use
     * @return Search query parameters map
     */
    public abstract Map<String, String> buildSearchQuery(final Movie movie);

    /**
     * Parses search result response, implementation specific to each MovieFinder
     *
     * @param result Search result
     * @return List of movies
     */
    public abstract List<Movie> parseSearchResult(final String result);

    public static abstract class MovieParser<R, T, Y, I> {
        private final R root;

        public MovieParser(R root) {
            this.root = root;
        }

        public Movie parse(Function<R, T> title, Function<R, Y> year, Function<R, I> imdbID) {
            return new Movie(
                    parseTitle(prepareTitle(title.apply(root))),
                    parseYear(prepareYear(year.apply(root))),
                    parseIMDBId(prepareImdbId(imdbID.apply(root)))
            );
        }

        public abstract String prepareTitle(T element);

        public abstract String prepareYear(Y element);

        public abstract String prepareImdbId(I element);
    }
}

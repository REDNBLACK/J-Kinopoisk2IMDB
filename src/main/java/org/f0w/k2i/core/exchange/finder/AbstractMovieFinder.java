package org.f0w.k2i.core.exchange.finder;

import com.google.common.base.Joiner;
import com.typesafe.config.Config;
import org.f0w.k2i.core.model.entity.Movie;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import com.google.inject.Inject;
import java.io.IOException;
import java.util.List;
import java.util.Map;

abstract class AbstractMovieFinder implements MovieFinder {
    protected Config config;

    protected Connection.Response response;

    @Inject
    public AbstractMovieFinder(Config config) {
        this.config = config;
    }

    @Override
    public void sendRequest(Movie movie) throws IOException {
        Connection request = Jsoup.connect(buildSearchQuery(movie))
                .userAgent(config.getString("user_agent"))
                .timeout(config.getInt("timeout"));

        response = request.execute();
    }

    protected abstract String buildSearchQuery(Movie movie);

    protected abstract List<Movie> parseSearchResult(final String result);

    protected static String buildURL(final String url, Map<String, String> queryData) {
        return url + Joiner.on("&").withKeyValueSeparator("=").join(queryData);
    }

    @Override
    public Connection.Response getRawResponse() {
        return response;
    }

    @Override
    public List<Movie> getProcessedResponse() {
        return parseSearchResult(response.body());
    }
}

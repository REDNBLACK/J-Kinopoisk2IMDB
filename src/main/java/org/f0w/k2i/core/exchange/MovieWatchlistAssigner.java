package org.f0w.k2i.core.exchange;

import com.google.inject.Inject;
import com.typesafe.config.Config;
import org.f0w.k2i.core.model.entity.Movie;

import com.google.common.collect.ImmutableMap;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Map;

public class MovieWatchlistAssigner implements Exchangeable<Movie, Connection.Response> {
    private final Config config;

    private Connection.Response response;

    @Inject
    public MovieWatchlistAssigner(Config config) {
        this.config = config;
    }

    @Override
    public void sendRequest(Movie movie) throws IOException {
        final String movieAddToWatchlistLink = "http://www.imdb.com/list/_ajax/edit";

        Map<String, String> postData = new ImmutableMap.Builder<String, String>()
                .put("const", movie.getImdbId())          // ID фильма
                .put("list_id", config.getString("list")) // ID списка для добавления
                .put("ref_tag", "title")                  // Реферер не меняется
                .build();

        Connection request = Jsoup.connect(movieAddToWatchlistLink)
                .method(Connection.Method.POST)
                .userAgent(config.getString("user_agent"))
                .timeout(config.getInt("timeout"))
                .cookie("id", config.getString("auth"))
                .ignoreContentType(true)
                .data(postData);

        response = request.execute();
    }

    @Override
    public Connection.Response getRawResponse() {
        return response;
    }

    @Override
    public Connection.Response getProcessedResponse() {
        return getRawResponse();
    }
}

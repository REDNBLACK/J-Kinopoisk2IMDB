package org.f0w.k2i.core.exchange;

import com.typesafe.config.Config;
import org.f0w.k2i.core.model.entity.Movie;

import com.google.common.collect.ImmutableMap;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import com.google.inject.Inject;
import java.io.IOException;
import java.util.Map;

public class MovieWatchlistAssigner implements Exchangeable<Movie, Connection.Response> {
    @Inject
    private Config config;

    private Connection.Response response;

    @Override
    public void sendRequest(Movie movie) throws IOException {
        Map<String, String> postData = new ImmutableMap.Builder<String, String>()
                .put("const", movie.getImdbId())          // ID фильма
                .put("list_id", config.getString("list")) // ID списка для добавления
                .put("ref_tag", "title")                  // Реферер не меняется
                .build();

        Connection request = Jsoup.connect("http://www.imdb.com/list/_ajax/edit")
                .method(Connection.Method.POST)
                .userAgent(config.getString("user_agent"))
                .timeout(config.getInt("timeout"))
                .cookie("id", config.getString("auth"))
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

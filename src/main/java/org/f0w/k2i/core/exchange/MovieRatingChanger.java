package org.f0w.k2i.core.exchange;

import com.typesafe.config.Config;
import org.f0w.k2i.core.model.entity.Movie;

import com.google.common.collect.ImmutableMap;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import com.google.inject.Inject;
import java.io.IOException;
import java.util.Map;

public class MovieRatingChanger implements Exchangeable<Movie, Connection.Response> {
    @Inject
    private Config config;

    @Inject
    private MovieAuthStringFetcher fetcher;

    private Connection.Response response;

    @Override
    public void sendRequest(Movie movie) throws IOException {
        final String movieRatingChangeLink = "http://www.imdb.com/ratings/_ajax/title";

        final String authString = getAuthFetcherResponse(movie);

        Map<String, String> postData = new ImmutableMap.Builder<String, String>()
                .put("tconst", movie.getImdbId())                 // ID фильма
                .put("rating", String.valueOf(movie.getRating())) // Рейтинг
                .put("auth", authString)                          // ID авторизации фильма
                .put("pageId", movie.getImdbId())                 // ID страницы (совпадает с ID фильма)
                .put("tracking_tag", "title-maindetails")         // Тэг для трекинга не меняется
                .put("pageType", "title")                         // Реферер не меняется
                .put("subpageType", "main")                       // Тип страницы не меняется
                .build();

        Connection request = Jsoup.connect(movieRatingChangeLink)
                .method(Connection.Method.POST)
                .userAgent(config.getString("user_agent"))
                .timeout(config.getInt("timeout"))
                .cookie("id", config.getString("auth"))
                .ignoreContentType(true)
                .data(postData);

        response = request.execute();
    }

    private String getAuthFetcherResponse(Movie movie) throws IOException {
        fetcher.sendRequest(movie);

        return fetcher.getProcessedResponse();
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

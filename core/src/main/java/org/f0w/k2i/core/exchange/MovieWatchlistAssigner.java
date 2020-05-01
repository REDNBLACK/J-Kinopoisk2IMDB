package org.f0w.k2i.core.exchange;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.typesafe.config.Config;
import lombok.NonNull;
import lombok.val;
import org.f0w.k2i.core.exchange.processor.JSONPOSTResponseProcessor;
import org.f0w.k2i.core.model.entity.Movie;
import org.jsoup.Connection;
import org.jsoup.helper.HttpConnection;

import java.io.IOException;
import java.net.URL;


/**
 * Adds Movie to IMDB list.
 */
public final class MovieWatchlistAssigner implements Exchangeable<Movie, Integer> {
    private final Config config;

    @Inject
    public MovieWatchlistAssigner(Config config) {
        this.config = config;
    }

    /**
     * Sends POST request and adds Movie to IMDB list, using {@link Movie#imdbId}
     *
     * @param movie Movie which should be added to IMDB list
     * @throws IOException If an I/O error occurs
     */
    @Override
    public ExchangeObject<Integer> prepare(@NonNull Movie movie) throws IOException {
        val list = config.getString("list");
        String url;
        Connection.Method method = Connection.Method.POST;
        if (list.equals("watchlist")) {
            url = "https://www.imdb.com/watchlist/" + movie.getImdbId();
            method = Connection.Method.PUT;
        }
        else {
            url = "https://www.imdb.com/list/" + list + '/' + movie.getImdbId() + "/add";
        }
        val movieAddToWatchlistLink = new URL(url);

        val postData = new ImmutableMap.Builder<String, String>()
                .put(config.getString("authControlKey"), config.getString("authControlValue"))
                .build();

        val request = HttpConnection.connect(movieAddToWatchlistLink)
                .method(method)
                .userAgent(config.getString("user_agent"))
                .timeout(config.getInt("timeout"))
                .cookie("session-id", config.getString("authSessionId"))
                .cookie("at-main", config.getString("authAtMain"))
                .cookie("ubid-main", config.getString("authUbidMain"))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .ignoreContentType(true)
                .data(postData)
                .request();

        return new ExchangeObject<>(request, new JSONPOSTResponseProcessor());
    }
}

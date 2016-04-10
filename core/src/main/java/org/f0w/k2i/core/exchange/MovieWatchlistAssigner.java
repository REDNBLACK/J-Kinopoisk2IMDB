package org.f0w.k2i.core.exchange;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.typesafe.config.Config;
import org.f0w.k2i.core.model.entity.Movie;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Map;

/**
 * Adds Movie to IMDB list.
 */
public final class MovieWatchlistAssigner extends IMDBJSONExchange {
    private final Config config;

    @Inject
    public MovieWatchlistAssigner(Config config) {
        this.config = config;
    }

    /**
     * Sends POST request and adds Movie to IMDB list, using {@link Movie#imdbId}
     * @param movie Movie which should be added to IMDB list
     * @throws IOException If an I/O error occurs
     */
    @Override
    public void sendRequest(Movie movie) throws IOException {
        final String movieAddToWatchlistLink = "http://www.imdb.com/list/_ajax/edit";

        Map<String, String> postData = new ImmutableMap.Builder<String, String>()
                .put("const", movie.getImdbId())
                .put("list_id", config.getString("list"))
                .put("ref_tag", "title")
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
}

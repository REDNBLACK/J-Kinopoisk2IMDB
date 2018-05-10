package org.f0w.k2i.core.exchange;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.google.inject.assistedinject.AssistedInject;
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
 * Changes Movie rating on IMDB.
 */
public final class MovieRatingChanger implements Exchangeable<Movie, Integer> {
    private final Config config;
    private final String authString;

    @AssistedInject
    private MovieRatingChanger(Config config, @Assisted @NonNull String authString) {
        this.config = config;
        this.authString = authString;
    }

    /**
     * Sends POST request and changes Movie rating,
     * using using {@link MovieAuthStringFetcher#getResponseProcessor()} authorization string
     * and {@link Movie#imdbId}
     *
     * @param movie Movie which rating to change
     * @throws IOException If an I/O error occurs
     */
    @Override
    public ExchangeObject<Integer> prepare(@NonNull Movie movie) throws IOException {
        val movieRatingChangeLink = new URL("https://www.imdb.com/ratings/_ajax/title");

        val postData = new ImmutableMap.Builder<String, String>()
                .put("tconst", movie.getImdbId())
                .put("rating", String.valueOf(movie.getRating()))
                .put("auth", authString)
                .put("pageId", movie.getImdbId())
                .put("tracking_tag", "title-maindetails")
                .put("pageType", "title")
                .put("subpageType", "main")
                .build();

        val request = HttpConnection.connect(movieRatingChangeLink)
                .method(Connection.Method.POST)
                .userAgent(config.getString("user_agent"))
                .timeout(config.getInt("timeout"))
                .cookie("id", config.getString("auth"))
                .ignoreContentType(true)
                .data(postData)
                .request();

        return new ExchangeObject<>(request, new JSONPOSTResponseProcessor());
    }
}

package org.f0w.k2i.core.exchange;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.typesafe.config.Config;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.val;
import org.f0w.k2i.core.model.entity.Movie;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;

/**
 * Changes Movie rating on IMDB.
 */
public final class MovieRatingChanger extends JSONPostExchangeable<Movie> {
    private final Config config;

    @Getter
    @Setter
    @NonNull
    private String authString;

    @Inject
    public MovieRatingChanger(Config config) {
        this.config = config;
    }

    /**
     * Sends POST request and changes Movie rating,
     * using using {@link MovieAuthStringFetcher#getProcessedResponse()} authorization string
     * and {@link Movie#imdbId}
     *
     * @param movie Movie which rating to change
     * @throws IOException If an I/O error occurs
     */
    @Override
    public void sendRequest(Movie movie) throws IOException {
        val movieRatingChangeLink = "http://www.imdb.com/ratings/_ajax/title";
        val postData = new ImmutableMap.Builder<String, String>()
                .put("tconst", movie.getImdbId())
                .put("rating", String.valueOf(movie.getRating()))
                .put("auth", getAuthString())
                .put("pageId", movie.getImdbId())
                .put("tracking_tag", "title-maindetails")
                .put("pageType", "title")
                .put("subpageType", "main")
                .build();

        Connection client = Jsoup.connect(movieRatingChangeLink)
                .method(Connection.Method.POST)
                .userAgent(config.getString("user_agent"))
                .timeout(config.getInt("timeout"))
                .cookie("id", config.getString("auth"))
                .ignoreContentType(true)
                .data(postData);

        setResponse(client.execute());
    }
}

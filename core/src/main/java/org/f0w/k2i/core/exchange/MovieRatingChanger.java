package org.f0w.k2i.core.exchange;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;
import com.typesafe.config.Config;
import org.f0w.k2i.core.model.entity.Movie;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.Map;

/**
 * Changes Movie rating on IMDB.
 */
public final class MovieRatingChanger extends IMDBJSONExchange {
    private final Config config;

    private final MovieAuthStringFetcher fetcher;

    @Inject
    public MovieRatingChanger(Config config, MovieAuthStringFetcher fetcher) {
        this.config = config;
        this.fetcher = fetcher;
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
        final String movieRatingChangeLink = "http://www.imdb.com/ratings/_ajax/title";

        final String authString = getAuthFetcherResponse(movie);

        Map<String, String> postData = new ImmutableMap.Builder<String, String>()
                .put("tconst", movie.getImdbId())
                .put("rating", String.valueOf(movie.getRating()))
                .put("auth", authString)
                .put("pageId", movie.getImdbId())
                .put("tracking_tag", "title-maindetails")
                .put("pageType", "title")
                .put("subpageType", "main")
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

    /**
     * Executes {@link MovieAuthStringFetcher} and returns it's response
     *
     * @param movie Movie to get auth string for
     * @return {@link MovieAuthStringFetcher#getProcessedResponse()}
     * @throws IOException If an I/O error occurs or authorisation string is null or empty
     */
    private String getAuthFetcherResponse(Movie movie) throws IOException {
        fetcher.sendRequest(movie);

        String response = fetcher.getProcessedResponse();

        if (Strings.isNullOrEmpty(response)) {
            throw new IOException("Movie authorisation string is empty!");
        }

        return response;
    }
}

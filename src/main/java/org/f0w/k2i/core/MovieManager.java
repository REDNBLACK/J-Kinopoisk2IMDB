package org.f0w.k2i.core;

import com.google.inject.Inject;
import org.f0w.k2i.core.comparators.EqualityComparator;
import org.f0w.k2i.core.comparators.EqualityComparatorType;
import org.f0w.k2i.core.comparators.EqualityComparatorsFactory;
import org.f0w.k2i.core.configuration.Configuration;
import org.f0w.k2i.core.entities.Movie;
import org.f0w.k2i.core.exchange.MovieFinders.MovieFinder;
import org.f0w.k2i.core.exchange.MovieFinders.MovieFinderType;
import org.f0w.k2i.core.exchange.MovieFinders.MovieFindersFactory;
import org.f0w.k2i.core.exchange.MovieRatingChanger;
import org.f0w.k2i.core.exchange.MovieWatchlistAssigner;
import org.f0w.k2i.core.filters.EmptyMovieInfoFilter;
import org.f0w.k2i.core.filters.MovieYearDeviationFilter;
import org.f0w.k2i.core.net.Response;

import java.net.HttpURLConnection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MovieManager {
    private Boolean success;
    private Movie movie;
    private Configuration configuration;
    private MovieFinder movieFinder;
    private MovieWatchlistAssigner watchlistAssigner;
    private MovieRatingChanger ratingChanger;

    @Inject
    public MovieManager(Configuration configuration, MovieFindersFactory movieFindersFactory, MovieWatchlistAssigner watchlistAssigner, MovieRatingChanger ratingChanger) {
        this.configuration = configuration;
        this.movieFinder = movieFindersFactory.make(configuration.getEnum(MovieFinderType.class, "query_format"));
        this.watchlistAssigner = watchlistAssigner;
        this.ratingChanger = ratingChanger;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    private void loadMovieIMDBId() {
        if (isMovieIMDBIdLoaded()) {
            return;
        }

        movieFinder.sendRequest(movie);

        List<Movie> movies = movieFinder.getProcessedResponse();
        movies = new EmptyMovieInfoFilter().filter(movies);
        movies = new MovieYearDeviationFilter(movie, configuration.getInt("year_deviation")).filter(movies);

        EqualityComparator<Movie> comparator = EqualityComparatorsFactory.make(
                configuration.getEnum(EqualityComparatorType.class, "comparator")
        );

        for (Movie imdbMovie : movies) {
            if (comparator.areEqual(movie, imdbMovie)) {
                movie.setImdbId(imdbMovie.getImdbId());
                break;
            }
        }

        success = isMovieIMDBIdLoaded();
    }

    private boolean isMovieIMDBIdLoaded() {
        return movie.getImdbId() != null;
    }

    private Response assignMovieToWatchList() {
        watchlistAssigner.sendRequest(movie);

        return watchlistAssigner.getRawResponse();
    }

    private Response setMovieRating() {
        ratingChanger.sendRequest(movie);

        return ratingChanger.getRawResponse();
    }

    public void handleMovie() {
        loadMovieIMDBId();

        if (!isSuccessful()) {
            return;
        }

        Set<Integer> statusCodes = new HashSet<>();
        switch (configuration.getEnum(WorkingMode.class, "mode")) {
            case ALL:
                statusCodes.add(assignMovieToWatchList().getStatusCode());
                statusCodes.add(setMovieRating().getStatusCode());
                break;
            case ONLY_LIST:
                statusCodes.add(assignMovieToWatchList().getStatusCode());
                break;
            case ONLY_RATING:
                statusCodes.add(setMovieRating().getStatusCode());
                break;
        }

        statusCodes.remove(HttpURLConnection.HTTP_OK);

        success = !statusCodes.contains(HttpURLConnection.HTTP_OK);
    }

    public boolean isSuccessful() {
        return success;
    }

    enum WorkingMode {
        ONLY_RATING,
        ONLY_LIST,
        ALL
    }
}

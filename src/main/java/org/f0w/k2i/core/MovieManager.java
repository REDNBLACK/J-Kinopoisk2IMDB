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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MovieManager {
    private boolean success = true;
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

        List<Movie> movies = new MovieYearDeviationFilter(movie, configuration.getInt("year_deviation"))
                .filter(new EmptyMovieInfoFilter().filter(movieFinder.find(movie)))
        ;

        EqualityComparator<Movie> comparator = EqualityComparatorsFactory.make(
                configuration.getEnum(EqualityComparatorType.class, "comparator")
        );

        for (Movie imdbMovie : movies) {
            if (comparator.areEqual(movie, imdbMovie)) {
                movie.setImdbId(imdbMovie.getImdbId());
                break;
            }
        }

        if (!isMovieIMDBIdLoaded()) {
            success = false;
        }
    }

    private boolean isMovieIMDBIdLoaded() {
        return movie.getImdbId() != null;
    }

    private int assignMovieToWatchList() {
        return watchlistAssigner.handle(movie);
    }

    private int setMovieRating() {
        return ratingChanger.handle(movie);
    }

    public void handleMovie() {
        loadMovieIMDBId();

        Set<Integer> status = new HashSet<>();
        switch (configuration.getEnum(WorkingMode.class, "mode")) {
            case ALL:
                status.add(assignMovieToWatchList());
                status.add(setMovieRating());
                break;
            case ONLY_LIST:
                status.add(assignMovieToWatchList());
                break;
            case ONLY_RATING:
                status.add(setMovieRating());
                break;
        }

        status.remove(200);

        success = !status.contains(200);
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

package org.f0w.k2i.core;

import com.google.common.collect.Range;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.typesafe.config.Config;
import org.f0w.k2i.core.comparators.EqualityComparator;
import org.f0w.k2i.core.comparators.TitleComparatorType;
import org.f0w.k2i.core.comparators.TitleComparatorsFactory;
import org.f0w.k2i.core.exchange.finder.MovieFinder;
import org.f0w.k2i.core.exchange.finder.MovieFinderType;
import org.f0w.k2i.core.exchange.finder.MovieFindersFactory;
import org.f0w.k2i.core.model.entity.Movie;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.*;

public class MovieManager {
    private static final Logger LOG = LoggerFactory.getLogger(MovieManager.class);

    private Config config;

    private Movie movie;

    private Provider<MovieFindersFactory> movieFindersFactoryProvider;

    private MovieFinderType movieFinderType;

    private TitleComparatorType movieComparatorType;

    @Inject
    public MovieManager(Config config, Provider<MovieFindersFactory> movieFindersFactoryProvider) {
        this.config = config;
        this.movieFindersFactoryProvider = movieFindersFactoryProvider;
        movieFinderType = MovieFinderType.valueOf(config.getString("query_format"));
        movieComparatorType = TitleComparatorType.valueOf(config.getString("comparator"));
    }

    public MovieManager setMovie(Movie movie) {
        this.movie = checkNotNull(movie);

        return this;
    }

    public Movie getMovie() {
        return movie;
    }

    public MovieManager prepare() throws IOException {
        if (isPrepared()) {
            LOG.info("Movie already prepated {}", movie);

            return this;
        }

        LOG.info("Preparing movie {}", movie);

        MovieFinder movieFinder = movieFindersFactoryProvider.get().make(movieFinderType);
        movieFinder.sendRequest(movie);

        List<Movie> movies = filterMovies(movieFinder.getProcessedResponse());

        Optional<Movie> matchingMovie = findMatchingMovie(movies);
        matchingMovie.ifPresent(m -> {
            movie.setImdbId(m.getImdbId());
            LOG.info("Movie IMDB id found: {}", m.getImdbId());
        });
        matchingMovie.orElseThrow(() -> new IOException("Matching movie not found"));

        return this;
    }

    public boolean isPrepared() {
        return movie.getImdbId() != null;
    }

    private Optional<Movie> findMatchingMovie(List<Movie> movies) {
        EqualityComparator<Movie> comparator = TitleComparatorsFactory.make(movieComparatorType);

        return movies.stream()
                .filter(imdbMovie -> comparator.areEqual(movie, imdbMovie))
                .findFirst();
    }

    private List<Movie> filterMovies(List<Movie> movies) {
        return movies.stream()
                .filter(Objects::nonNull)
                .filter(movieFieldsIsSet())
                .filter(isBetweenYearDeviation(config.getInt("year_deviation")))
                .collect(Collectors.toList());
    }

    private Predicate<Movie> isBetweenYearDeviation(int deviation) {
        return imdbMovie -> Range.closed(imdbMovie.getYear() - deviation, imdbMovie.getYear() + deviation)
                .contains(movie.getYear());
    }

    private Predicate<Movie> movieFieldsIsSet() {
        return m -> m.getTitle() != null && m.getYear() != null && m.getImdbId() != null;
    }
}

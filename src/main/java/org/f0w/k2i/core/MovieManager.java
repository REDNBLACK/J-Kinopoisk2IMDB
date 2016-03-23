package org.f0w.k2i.core;

import com.google.common.collect.Range;
import com.google.inject.Inject;
import com.typesafe.config.Config;
import org.f0w.k2i.core.comparators.EqualityComparator;
import org.f0w.k2i.core.comparators.TitleComparatorType;
import org.f0w.k2i.core.comparators.TitleComparatorsFactory;
import org.f0w.k2i.core.exchange.finder.MovieFinder;
import org.f0w.k2i.core.exchange.finder.MovieFinderType;
import org.f0w.k2i.core.exchange.finder.MovieFindersFactory;
import org.f0w.k2i.core.model.entity.Movie;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.*;

public class MovieManager {
    @Inject
    private Config config;

    private Movie movie;

    private MovieFinderType movieFinderType = MovieFinderType.valueOf(config.getString("query_format"));

    private TitleComparatorType movieComparatorType = TitleComparatorType.valueOf(config.getString("comparator"));

    public MovieManager setMovie(Movie movie) {
        this.movie = checkNotNull(movie);

        return this;
    }

    public Movie getMovie() {
        return movie;
    }

    public MovieManager prepare() {
        if (!isPrepared()) {
            MovieFinder movieFinder = MovieFindersFactory.make(movieFinderType);

            try {
                movieFinder.sendRequest(movie);
            } catch (IOException e) {
                return this;
            }

            List<Movie> movies = filterMovies(movieFinder.getProcessedResponse());

            findMatchingMovie(movies).ifPresent(m -> movie.setImdbId(m.getImdbId()));
        }

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

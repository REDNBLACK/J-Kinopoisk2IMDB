package org.f0w.k2i.core.handler.command;

import com.google.common.collect.Range;
import com.google.inject.Inject;
import com.typesafe.config.Config;
import org.f0w.k2i.core.comparators.EqualityComparator;
import org.f0w.k2i.core.comparators.TitleComparatorType;
import org.f0w.k2i.core.comparators.TitleComparatorsFactory;
import org.f0w.k2i.core.exchange.finder.MovieFinder;
import org.f0w.k2i.core.exchange.finder.MovieFinderType;
import org.f0w.k2i.core.exchange.finder.MovieFindersFactory;
import org.f0w.k2i.core.model.entity.ImportProgress;
import org.f0w.k2i.core.model.entity.Movie;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ParseIMDBMovieIDCommand extends AbstractMovieCommand {
    private Config config;

    private MovieFindersFactory movieFindersFactory;

    @Inject
    public ParseIMDBMovieIDCommand(Config config, MovieFindersFactory movieFindersFactory) {
        this.config = config;
        this.movieFindersFactory = movieFindersFactory;
    }

    public void execute(ImportProgress importProgress) {
        Movie movie = importProgress.getMovie();

        if (movie.getImdbId() != null) {
            LOG.info("Movie is already prepared {}", movie);
            return;
        }

        LOG.info("Preparing movie {}", movie);

        try {
            MovieFinderType movieFinderType = MovieFinderType.valueOf(config.getString("query_format"));

            MovieFinder movieFinder = movieFindersFactory.make(movieFinderType);
            movieFinder.sendRequest(movie);

            List<Movie> movies = filterMovies(movie, movieFinder.getProcessedResponse());

            Optional<Movie> matchingMovie = findMatchingMovie(movie, movies);
            matchingMovie.ifPresent(m -> {
                movie.setImdbId(m.getImdbId());

                importProgress.setMovie(movie);

                LOG.info("Movie IMDB id found: {}", m.getImdbId());
            });
            matchingMovie.orElseThrow(() -> new IOException("Matching movie not found"));
        } catch (IOException e) {
            LOG.info("Can't prepare movie: {}", e);
        }
    }

    private Optional<Movie> findMatchingMovie(Movie movie, List<Movie> movies) {
        TitleComparatorType titleComparatorType = TitleComparatorType.valueOf(config.getString("comparator"));
        EqualityComparator<Movie> comparator = TitleComparatorsFactory.make(titleComparatorType);

        return movies.stream()
                .filter(imdbMovie -> comparator.areEqual(movie, imdbMovie))
                .findFirst();
    }

    private List<Movie> filterMovies(Movie movie, List<Movie> movies) {
        return movies.stream()
                .filter(Objects::nonNull)
                .filter(movieFieldsIsSet())
                .filter(isBetweenYearDeviation(movie, config.getInt("year_deviation")))
                .collect(Collectors.toList());
    }

    private Predicate<Movie> isBetweenYearDeviation(Movie movie, int deviation) {
        return imdbMovie -> Range.closed(imdbMovie.getYear() - deviation, imdbMovie.getYear() + deviation)
                .contains(movie.getYear());
    }

    private Predicate<Movie> movieFieldsIsSet() {
        return m -> m.getTitle() != null && m.getYear() != null && m.getImdbId() != null;
    }
}

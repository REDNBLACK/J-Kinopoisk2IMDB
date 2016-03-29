package org.f0w.k2i.core.command;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.f0w.k2i.core.comparator.MovieComparator;
import org.f0w.k2i.core.exchange.finder.MovieFinder;
import org.f0w.k2i.core.model.entity.ImportProgress;
import org.f0w.k2i.core.model.entity.Movie;

import java.io.IOException;
import java.util.*;

import static org.f0w.k2i.core.util.MovieFieldsUtils.*;

public class ParseIDCommand extends AbstractMovieCommand {
    private final Provider<MovieFinder> movieFinderProvider;

    private final MovieComparator movieComparator;

    @Inject
    public ParseIDCommand(Provider<MovieFinder> movieFinderProvider, MovieComparator movieComparator) {
        this.movieFinderProvider = movieFinderProvider;
        this.movieComparator = movieComparator;
    }

    @Override
    public Optional<MovieError> execute(ImportProgress importProgress) {
        Movie movie = importProgress.getMovie();

        LOG.info("Preparing movie {}", movie);

        if (!isEmptyIMDBId(movie.getImdbId())) {
            LOG.info("Movie is already prepared {}", movie);

            return Optional.empty();
        }

        try {
            MovieFinder movieFinder = movieFinderProvider.get();

            movieFinder.sendRequest(movie);

            Optional<Movie> matchingMovie = findMatchingMovie(movie, movieFinder.getProcessedResponse());

            if (!matchingMovie.isPresent()) {
                throw new IOException("Matching movie not found");
            }

            matchingMovie.ifPresent(m -> {
                movie.setImdbId(m.getImdbId());

                importProgress.setMovie(movie);

                LOG.info("Movie IMDB id successfully found: {}", m.getImdbId());
            });

            return Optional.empty();
        } catch (IOException e) {
            LOG.info("Can't prepare movie: {}", e);

            return Optional.of(new MovieError(importProgress.getMovie(), e.getMessage()));
        }
    }

    private Optional<Movie> findMatchingMovie(Movie movie, Deque<Movie> movies) {
        while (!movies.isEmpty()) {
            Movie imdbMovie = movies.poll();

            if (movieComparator.areEqual(movie, imdbMovie)) {
                return Optional.of(imdbMovie);
            }
        }

        return Optional.empty();
    }
}

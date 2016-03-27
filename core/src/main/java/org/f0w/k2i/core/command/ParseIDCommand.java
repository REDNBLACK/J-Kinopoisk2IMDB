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

class ParseIDCommand extends AbstractMovieCommand {
    private final Provider<MovieFinder> movieFinderProvider;

    private final MovieComparator movieComparator;

    @Inject
    public ParseIDCommand(Provider<MovieFinder> movieFinderProvider, MovieComparator movieComparator) {
        this.movieFinderProvider = movieFinderProvider;
        this.movieComparator = movieComparator;
    }

    @Override
    public void execute(ImportProgress importProgress) {
        Movie movie = importProgress.getMovie();

        if (!isEmptyIMDBId(movie.getImdbId())) {
            LOG.info("Movie is already prepared {}", movie);
            return;
        }

        LOG.info("Preparing movie {}", movie);

        try {
            MovieFinder movieFinder = movieFinderProvider.get();

            movieFinder.sendRequest(movie);

            Optional<Movie> matchingMovie = findMatchingMovie(movie, movieFinder.getProcessedResponse());

            if (!matchingMovie.isPresent()) {
                LOG.info("Matching movie not found");
                return;
            }

            matchingMovie.ifPresent(m -> {
                movie.setImdbId(m.getImdbId());

                importProgress.setMovie(movie);

                LOG.info("Movie IMDB id found: {}", m.getImdbId());
            });
        } catch (IOException e) {
            LOG.info("Can't prepare movie: {}", e);
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

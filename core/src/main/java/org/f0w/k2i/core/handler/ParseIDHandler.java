package org.f0w.k2i.core.handler;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import com.google.inject.Provider;
import org.f0w.k2i.core.comparator.MovieComparator;
import org.f0w.k2i.core.exchange.finder.MovieFinder;
import org.f0w.k2i.core.model.entity.ImportProgress;
import org.f0w.k2i.core.model.entity.Movie;

import java.io.IOException;
import java.util.Deque;
import java.util.List;
import java.util.Optional;

import static org.f0w.k2i.core.util.MovieUtils.isEmptyIMDBId;

public final class ParseIDHandler extends MovieHandler {
    private final Provider<MovieFinder> movieFinderProvider;
    private final MovieComparator movieComparator;

    @Inject
    public ParseIDHandler(Provider<MovieFinder> movieFinderProvider, MovieComparator movieComparator) {
        this.movieFinderProvider = movieFinderProvider;
        this.movieComparator = movieComparator;
        this.types = ImmutableSet.of(Type.SET_RATING, Type.ADD_TO_WATCHLIST, Type.COMBINED);
    }

    /**
     * Find and set {@link ImportProgress#movie} IMDB ID, or add error to list if occured.
     * @param importProgress Entity to handle
     * @param errors List which fill with errors if occured
     */
    @Override
    protected void handleMovie(ImportProgress importProgress, List<Error> errors) {
        Movie movie = importProgress.getMovie();

        LOG.info("Preparing movie {}", movie);

        if (!isEmptyIMDBId(movie.getImdbId())) {
            LOG.info("Movie is already prepared {}", movie);
            return;
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
        } catch (IOException e) {
            LOG.info("Can't prepare movie: {}", e);

            errors.add(new Error(importProgress, e.getMessage()));
        }
    }

    /**
     * Finds movie matching to movie argument using {@link MovieComparator} or {@link Optional#empty()} on failure
     * @param movie Movie similar to which should be found
     * @param movies Deque in which perform search
     * @return Optional of found matching movie
     */
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

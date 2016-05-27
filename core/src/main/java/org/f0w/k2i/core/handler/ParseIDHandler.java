package org.f0w.k2i.core.handler;

import com.google.inject.Inject;
import lombok.NonNull;
import lombok.val;
import org.f0w.k2i.core.DocumentSourceType;
import org.f0w.k2i.core.comparator.MovieComparator;
import org.f0w.k2i.core.comparator.MovieComparatorFactory;
import org.f0w.k2i.core.exchange.finder.MovieFinder;
import org.f0w.k2i.core.exchange.finder.MovieFinderFactory;
import org.f0w.k2i.core.model.entity.ImportProgress;
import org.f0w.k2i.core.model.entity.Movie;

import java.io.IOException;
import java.util.Deque;
import java.util.List;
import java.util.Optional;

public final class ParseIDHandler extends MovieHandler {
    private final MovieFinder movieFinder;
    private final MovieComparator movieComparator;

    @Inject
    public ParseIDHandler(
            DocumentSourceType[] documentSourceTypes,
            MovieFinderFactory movieFinderFactory,
            MovieComparator.Type[] movieComparatorTypes,
            MovieComparatorFactory movieComparatorFactory
    ) {
        this.movieFinder = movieFinderFactory.make(documentSourceTypes);
        this.movieComparator = movieComparatorFactory.make(movieComparatorTypes);
    }

    /**
     * Find and set {@link ImportProgress#movie} IMDB ID, or add error to list if occured.
     *
     * @param importProgress Entity to handle
     * @param errors         List which fill with errors if occured
     */
    @Override
    protected void handleMovie(ImportProgress importProgress, List<Error> errors) {
        Movie movie = importProgress.getMovie();

        LOG.info("Preparing movie {}", movie);

        if (!movie.isEmptyIMDBId()) {
            LOG.info("Movie is already prepared {}", movie);
            return;
        }

        try {
            val foundMovies = movieFinder.prepare(movie).getProcessedResponse();
            val matchingMovieIMDBId = findMatchingMovie(movie, foundMovies)
                    .map(Movie::getImdbId)
                    .orElseThrow(() -> new IOException("Matching movie not found"));

            movie.setImdbId(matchingMovieIMDBId);

            LOG.info("Movie IMDB id successfully found: {}", matchingMovieIMDBId);
        } catch (IOException e) {
            LOG.error("Can't prepare movie: {}", e);
            errors.add(new Error(importProgress.getMovie(), e.getMessage()));
        }
    }

    /**
     * Finds movie matching to movie argument using {@link MovieComparator} or {@link Optional#empty()} on failure
     *
     * @param movie  Movie similar to which should be found
     * @param movies Deque in which perform search
     * @return Optional of found matching movie
     */
    private Optional<Movie> findMatchingMovie(@NonNull final Movie movie, @NonNull final Deque<Movie> movies) {
        while (!movies.isEmpty()) {
            Movie imdbMovie = movies.poll();

            if (movieComparator.areEqual(movie, imdbMovie)) {
                movies.clear();
                return Optional.of(imdbMovie);
            }
        }

        return Optional.empty();
    }
}

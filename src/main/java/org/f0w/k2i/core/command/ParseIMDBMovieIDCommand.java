package org.f0w.k2i.core.command;

import com.google.inject.Inject;
import com.typesafe.config.Config;
import org.f0w.k2i.core.comparator.MovieComparator;
import org.f0w.k2i.core.comparator.MovieComparatorFactory;
import org.f0w.k2i.core.exchange.finder.MovieFinder;
import org.f0w.k2i.core.exchange.finder.MovieFindersFactory;
import org.f0w.k2i.core.model.entity.ImportProgress;
import org.f0w.k2i.core.model.entity.Movie;

import java.io.IOException;
import java.util.*;

import static org.f0w.k2i.core.util.MovieFieldsUtils.*;

public class ParseIMDBMovieIDCommand extends AbstractMovieCommand {
    private final Config config;

    private final MovieFindersFactory movieFindersFactory;

    private final MovieComparatorFactory movieComparatorFactory;

    @Inject
    public ParseIMDBMovieIDCommand(
            Config config,
            MovieFindersFactory movieFindersFactory,
            MovieComparatorFactory movieComparatorFactory
    ) {
        this.config = config;
        this.movieFindersFactory = movieFindersFactory;
        this.movieComparatorFactory = movieComparatorFactory;
    }

    @Override
    public void execute(ImportProgress importProgress) {
        Movie movie = importProgress.getMovie();

        if (!isEmptyIMDBId(movie.getImdbId())) {
            LOG.info("Movie is already prepared {}", movie);
            return;
        }

        LOG.info("Preparing movie {}", movie);

        MovieFinder movieFinder = movieFindersFactory.make(MovieFinder.Type.valueOf(config.getString("query_format")));

        try {
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
        MovieComparator comparator = movieComparatorFactory.make(config.getStringList("comparators"));

        while (!movies.isEmpty()) {
            Movie imdbMovie = movies.poll();

            if (comparator.areEqual(movie, imdbMovie)) {
                return Optional.of(imdbMovie);
            }
        }

        return Optional.empty();
    }
}

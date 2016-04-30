package org.f0w.k2i.core.exchange.finder;

import com.google.common.collect.ForwardingDeque;
import org.f0w.k2i.core.model.entity.Movie;
import org.jsoup.Connection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;

public final class MixedMovieFinder implements MovieFinder {
    private static final Logger LOG = LoggerFactory.getLogger(MixedMovieFinder.class);

    private final Type type;
    private final Deque<MovieFinder> movieFinders;
    private Movie movie;

    public MixedMovieFinder(Type type, List<MovieFinder> movieFinders) {
        this.type = type;
        this.movieFinders = new LinkedList<>(movieFinders);
    }

    @Override
    public void sendRequest(Movie movie) throws IOException {
        this.movie = movie;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Connection.Response getRawResponse() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the deque of movies
     * @return {@link MovieLazyLoadingDeque}
     */
    @Override
    public Deque<Movie> getProcessedResponse() {
        return new MovieLazyLoadingDeque();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Type getType() {
        return type;
    }

    /**
     * Movies deque with preloading, using list of {@link MovieFinder} if it is empty.
     */
    private class MovieLazyLoadingDeque extends ForwardingDeque<Movie> {
        private final Deque<Movie> delegate = new LinkedList<>();

        @Override
        protected Deque<Movie> delegate() {
            return delegate;
        }

        @Override
        public boolean isEmpty() {
            boolean isEmpty = super.isEmpty();

            if (isEmpty && !movieFinders.isEmpty()) {
                LOG.debug("Movies deque is empty, start loading...");

                try {
                    MovieFinder finder = movieFinders.poll();
                    LOG.debug("Loading using finder type: {}", finder.getType());

                    finder.sendRequest(MixedMovieFinder.this.movie);
                    Deque<Movie> movies = finder.getProcessedResponse();
                    super.addAll(movies);

                    LOG.debug("Successfully loaded: {}", movies);
                } catch (IOException ignore) {
                    LOG.debug("Loading error, trying next finder...");
                }

                return isEmpty();
            }

            return isEmpty;
        }
    }
}

package org.f0w.k2i.core.exchange.finder;

import com.google.common.collect.ForwardingDeque;
import com.google.common.collect.ImmutableList;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.f0w.k2i.core.exchange.ExchangeObject;
import org.f0w.k2i.core.model.entity.Movie;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

@Slf4j
public final class MixedMovieFinder implements MovieFinder {
    @NonNull
    private final Type type;

    @NonNull
    private final List<MovieFinder> originalFinders;

    private Deque<MovieFinder> findersDeque;
    private Movie movie;

    public MixedMovieFinder(Type type, List<MovieFinder> movieFinders) {
        this.type = type;
        this.originalFinders = ImmutableList.copyOf(movieFinders);
    }

    @Override
    public ExchangeObject<Deque<Movie>> prepare(@NonNull Movie movie) throws IOException {
        this.movie = movie;
        this.findersDeque = new ArrayDeque<>(originalFinders);

        return new ExchangeObject<>(response -> new MovieLazyLoadingDeque());
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
        private final Deque<Movie> delegate = new ArrayDeque<>();

        @Override
        protected Deque<Movie> delegate() {
            return delegate;
        }

        @Override
        public boolean isEmpty() {
            boolean isEmpty = super.isEmpty();

            if (isEmpty && !findersDeque.isEmpty()) {
                log.debug("Movies deque is empty, start loading...");

                try {
                    MovieFinder finder = findersDeque.poll();
                    log.debug("Loading using finder type: {}", finder.getType());

                    Deque<Movie> movies = finder.prepare(movie).getProcessedResponse();
                    super.addAll(movies);

                    log.debug("Successfully loaded: {}", movies);
                } catch (IOException ignore) {
                    log.debug("Loading error, trying next finder...");
                }

                return isEmpty();
            }

            return isEmpty;
        }
    }
}

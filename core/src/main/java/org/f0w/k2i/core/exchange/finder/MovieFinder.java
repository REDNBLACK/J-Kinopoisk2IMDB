package org.f0w.k2i.core.exchange.finder;

import org.f0w.k2i.core.exchange.Exchangeable;
import org.f0w.k2i.core.model.entity.Movie;

import java.util.Deque;

/**
 * Interface for finders of movie IMDB ID
 */
public interface MovieFinder extends Exchangeable<Movie, Deque<Movie>> {
    /**
     * Available movie finder types
     */
    enum Type {
        XML,
        JSON,
        HTML,
        MIXED
    }
}

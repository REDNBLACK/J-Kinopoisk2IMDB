package org.f0w.k2i.core.exchange.finder;

import org.f0w.k2i.core.model.entity.Movie;
import org.f0w.k2i.core.exchange.Exchangeable;

import java.util.Deque;
import java.util.List;

public interface MovieFinder extends Exchangeable<Movie, Deque<Movie>> {
    enum Type {
        XML,
        JSON,
        HTML,
        MIXED
    }
}

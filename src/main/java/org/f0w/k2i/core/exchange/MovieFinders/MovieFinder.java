package org.f0w.k2i.core.exchange.MovieFinders;

import org.f0w.k2i.core.entities.Movie;

import java.util.List;

public interface MovieFinder {
    List<Movie> find(Movie movie);
}

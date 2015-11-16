package org.f0w.k2i.core.Requests.MovieFinders;

import org.f0w.k2i.core.Models.Movie;

import java.util.List;

public interface MovieFinder {
    List<Movie> find(Movie movie);
}

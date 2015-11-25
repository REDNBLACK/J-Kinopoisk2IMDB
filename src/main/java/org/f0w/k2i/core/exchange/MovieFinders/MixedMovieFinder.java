package org.f0w.k2i.core.exchange.MovieFinders;

import org.f0w.k2i.core.entities.Movie;

import java.util.ArrayList;
import java.util.List;

class MixedMovieFinder implements MovieFinder {
    private List<MovieFinder> movieFinders;

    public MixedMovieFinder(List<MovieFinder> movieFinders) {
        this.movieFinders = movieFinders;
    }

    @Override
    public List<Movie> find(Movie movie) {
        List<Movie> movies = new ArrayList<>();

        for (MovieFinder movieFinder : movieFinders) {
            movies.addAll(movieFinder.find(movie));
        }

        return movies;
    }
}

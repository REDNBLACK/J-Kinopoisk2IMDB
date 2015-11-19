package org.f0w.k2i.core.exchange.MovieFinders;

import org.f0w.k2i.core.entities.Movie;

import java.util.ArrayList;
import java.util.List;

class MixedMovieFinder implements MovieFinder {
    @Override
    public List<Movie> find(Movie movie) {
        MovieFinderType[] usedTypes = {MovieFinderType.XML, MovieFinderType.JSON, MovieFinderType.HTML};
        List<Movie> movies = new ArrayList<>();

        for (MovieFinderType type : usedTypes) {
            MovieFinder finder = MovieFindersFactory.make(type);

            movies.addAll(finder.find(movie));
        }

        return movies;
    }
}

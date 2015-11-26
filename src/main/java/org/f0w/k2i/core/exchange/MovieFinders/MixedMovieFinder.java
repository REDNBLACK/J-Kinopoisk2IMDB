package org.f0w.k2i.core.exchange.MovieFinders;

import org.f0w.k2i.core.entities.Movie;
import org.f0w.k2i.core.net.Response;

import java.util.ArrayList;
import java.util.List;

class MixedMovieFinder implements MovieFinder {
    private List<MovieFinder> movieFinders;
    private List<Movie> movies = new ArrayList<>();

    public MixedMovieFinder(List<MovieFinder> movieFinders) {
        this.movieFinders = movieFinders;
    }

    @Override
    public void sendRequest(Movie movie) {
        for (MovieFinder movieFinder : movieFinders) {
            movieFinder.sendRequest(movie);
            movies.addAll(movieFinder.getProcessedResponse());
        }
    }

    @Override
    public Response getRawResponse() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Movie> getProcessedResponse() {
        return movies;
    }
}

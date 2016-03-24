package org.f0w.k2i.core.exchange.finder;

import org.f0w.k2i.core.model.entity.Movie;
import org.jsoup.Connection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

class MixedMovieFinder implements MovieFinder {
    private List<MovieFinder> movieFinders;
    private List<Movie> movies = new ArrayList<>();

    public MixedMovieFinder(List<MovieFinder> movieFinders) {
        this.movieFinders = movieFinders;
    }

    @Override
    public void sendRequest(Movie movie) throws IOException {
        for (MovieFinder finder : movieFinders) {
            try {
                finder.sendRequest(movie);
                movies.addAll(finder.getProcessedResponse());
            } catch (IOException e) {}
        }
    }

    @Override
    public Connection.Response getRawResponse() {
        throw new UnsupportedOperationException();
    }

    @Override
    public List<Movie> getProcessedResponse() {
        return new ArrayList<>(movies);
    }
}

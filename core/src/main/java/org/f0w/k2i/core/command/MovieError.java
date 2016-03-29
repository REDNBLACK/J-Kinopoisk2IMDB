package org.f0w.k2i.core.command;

import org.f0w.k2i.core.model.entity.Movie;

public class MovieError {
    private final Movie movie;
    private final String error;

    public MovieError(Movie movie, String error) {
        this.movie = movie;
        this.error = error;
    }

    public Movie getMovie() {
        return movie;
    }

    public String getError() {
        return error;
    }
}

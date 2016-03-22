package org.f0w.k2i.core.handler;

import org.f0w.k2i.core.exchange.MovieRatingChanger;

import com.google.inject.Inject;
import java.io.IOException;

class SetRatingMovieHandler extends AbstractMovieHandler {
    @Inject
    private MovieRatingChanger changer;

    @Override
    public boolean execute() {
        try {
            changer.sendRequest(movie);

            return true;
        } catch (IOException e) {
            return false;
        }
    }
}

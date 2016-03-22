package org.f0w.k2i.core.handler;

import com.google.common.base.Preconditions;
import org.f0w.k2i.core.model.entity.KinopoiskFile;
import org.f0w.k2i.core.model.entity.Movie;

abstract class AbstractMovieHandler implements MovieHandler {
    protected Movie movie;

    protected KinopoiskFile file;

    @Override
    public void setMovie(Movie movie) {
        this.movie = Preconditions.checkNotNull(movie);
    }

    @Override
    public void setKinopoiskFile(KinopoiskFile file) {
        this.file = Preconditions.checkNotNull(file);
    }
}

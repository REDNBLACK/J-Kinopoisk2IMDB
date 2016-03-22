package org.f0w.k2i.core.handler;

import org.f0w.k2i.core.model.entity.KinopoiskFile;
import org.f0w.k2i.core.model.entity.Movie;

public interface MovieHandler {
    boolean execute();

    void setMovie(Movie movie);

    void setKinopoiskFile(KinopoiskFile file);
}

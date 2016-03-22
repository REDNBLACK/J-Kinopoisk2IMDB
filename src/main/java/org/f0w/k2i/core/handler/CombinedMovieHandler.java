package org.f0w.k2i.core.handler;

import org.f0w.k2i.core.model.entity.KinopoiskFile;

import java.util.List;

class CombinedMovieHandler implements MovieHandler {
    private KinopoiskFile kinopoiskFile;
    private List<MovieHandler> handlers;

    public CombinedMovieHandler(List<MovieHandler> handlers) {
        this.handlers = handlers;
    }

    @Override
    public int execute() {
        return handlers.stream()
                .mapToInt(h -> {
                    h.setKinopoiskFile(kinopoiskFile);

                    return h.execute();
                })
                .sum();
    }

    @Override
    public void setKinopoiskFile(KinopoiskFile kinopoiskFile) {
        this.kinopoiskFile = kinopoiskFile;
    }
}

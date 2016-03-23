package org.f0w.k2i.core.handler;

import org.f0w.k2i.core.model.entity.ImportProgress;
import org.f0w.k2i.core.model.entity.KinopoiskFile;

import java.util.List;

class CombinedMovieHandler implements MovieHandler {
    private List<MovieHandler> handlers;

    public CombinedMovieHandler(List<MovieHandler> handlers) {
        this.handlers = handlers;
    }

    @Override
    public void execute(List<ImportProgress> importProgress) {
        handlers.forEach(h -> h.execute(importProgress));
    }
}

package org.f0w.k2i.core.handler;

import org.f0w.k2i.core.model.entity.ImportProgress;

import java.util.List;
import java.util.function.Consumer;

class CombinedMovieHandler implements MovieHandler {
    private List<MovieHandler> handlers;

    public CombinedMovieHandler(List<MovieHandler> handlers) {
        this.handlers = handlers;
    }

    @Override
    public void execute(List<ImportProgress> importProgress, Consumer<ImportProgress> consumer) {
        handlers.forEach(h -> h.execute(importProgress, consumer));
    }
}

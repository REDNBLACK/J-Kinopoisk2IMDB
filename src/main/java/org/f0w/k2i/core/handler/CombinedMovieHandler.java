package org.f0w.k2i.core.handler;

import java.util.List;

class CombinedMovieHandler extends AbstractMovieHandler {
    private List<MovieHandler> handlers;

    public CombinedMovieHandler(List<MovieHandler> handlers) {
        this.handlers = handlers;
    }

    @Override
    public boolean execute() {
        return handlers.stream()
                .map(h -> {
                    h.setImportProgress(importProgress);
                    return h.execute();
                })
                .distinct()
                .noneMatch(h -> false);
    }
}

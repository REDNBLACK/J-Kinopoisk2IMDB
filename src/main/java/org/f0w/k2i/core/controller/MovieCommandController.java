package org.f0w.k2i.core.controller;

import org.f0w.k2i.core.model.entity.ImportProgress;

@FunctionalInterface
public interface MovieCommandController {
    void execute(ImportProgress importProgress);

    enum Type {
        ADD_TO_WATCHLIST,
        SET_RATING,
        COMBINED
    }
}

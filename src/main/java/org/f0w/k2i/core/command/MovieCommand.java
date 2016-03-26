package org.f0w.k2i.core.command;

import org.f0w.k2i.core.model.entity.ImportProgress;

@FunctionalInterface
public interface MovieCommand {
    void execute(ImportProgress value);

    enum Type {
        ADD_TO_WATCHLIST,
        SET_RATING,
        COMBINED
    }
}

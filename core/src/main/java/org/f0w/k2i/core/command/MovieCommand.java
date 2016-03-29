package org.f0w.k2i.core.command;

import org.f0w.k2i.core.model.entity.ImportProgress;

import java.util.Optional;

@FunctionalInterface
public interface MovieCommand {
    Optional<MovieError> execute(ImportProgress value);

    enum Type {
        ADD_TO_WATCHLIST,
        SET_RATING,
        COMBINED
    }
}

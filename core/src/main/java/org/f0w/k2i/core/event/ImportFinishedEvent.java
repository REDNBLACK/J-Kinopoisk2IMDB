package org.f0w.k2i.core.event;

import org.f0w.k2i.core.command.MovieError;

import java.util.List;

public class ImportFinishedEvent implements Event {
    public final List<MovieError> errors;

    public ImportFinishedEvent(List<MovieError> errors) {
        this.errors = errors;
    }
}

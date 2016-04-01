package org.f0w.k2i.core.event;

import org.f0w.k2i.core.handler.MovieHandler;

import java.util.List;

public class ImportFinishedEvent implements Event {
    public final List<MovieHandler.Error> errors;

    public ImportFinishedEvent(List<MovieHandler.Error> errors) {
        this.errors = errors;
    }
}

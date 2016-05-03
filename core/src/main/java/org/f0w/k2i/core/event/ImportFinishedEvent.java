package org.f0w.k2i.core.event;

import com.google.common.collect.ImmutableList;
import org.f0w.k2i.core.handler.MovieHandler;

import java.util.List;

/**
 * Indicates that import of movies was finished
 */
public final class ImportFinishedEvent implements Event {
    public final List<MovieHandler.Error> errors;

    public ImportFinishedEvent(List<MovieHandler.Error> errors) {
        this.errors = ImmutableList.copyOf(errors);
    }
}

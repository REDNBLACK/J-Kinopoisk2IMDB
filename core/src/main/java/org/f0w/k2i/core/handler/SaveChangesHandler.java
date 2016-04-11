package org.f0w.k2i.core.handler;

import com.google.common.collect.ImmutableSet;
import com.google.inject.Inject;
import org.f0w.k2i.core.model.entity.ImportProgress;
import org.f0w.k2i.core.model.repository.ImportProgressRepository;

import java.util.List;
import java.util.Set;

public final class SaveChangesHandler extends AbstractMovieHandler {
    private final ImportProgressRepository importProgressRepository;

    @Inject
    public SaveChangesHandler(ImportProgressRepository importProgressRepository) {
        this.importProgressRepository = importProgressRepository;
        this.types = ImmutableSet.of(Type.SET_RATING, Type.ADD_TO_WATCHLIST, Type.COMBINED);
    }

    @Override
    protected void handleMovie(ImportProgress importProgress, List<Error> errors) {
        importProgressRepository.save(importProgress);

        LOG.info("Changes were successfully saved to storage");
    }
}

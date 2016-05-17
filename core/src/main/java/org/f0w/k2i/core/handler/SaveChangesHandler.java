package org.f0w.k2i.core.handler;

import com.google.inject.Inject;
import org.f0w.k2i.core.model.entity.ImportProgress;
import org.f0w.k2i.core.model.repository.ImportProgressRepository;
import org.f0w.k2i.core.model.service.ImportProgressService;

import java.util.List;

public final class SaveChangesHandler extends MovieHandler {
    private final ImportProgressService service;

    @Inject
    public SaveChangesHandler(ImportProgressService service) {
        this.service = service;
    }

    /**
     * Save changes to ImportProgress entity into {@link ImportProgressRepository}
     *
     * @param importProgress Entity to handle
     * @param errors         List which fill with errors if occured
     */
    @Override
    protected void handleMovie(ImportProgress importProgress, List<Error> errors) {
        service.save(importProgress);

        LOG.info("Changes were successfully saved to storage");
    }
}

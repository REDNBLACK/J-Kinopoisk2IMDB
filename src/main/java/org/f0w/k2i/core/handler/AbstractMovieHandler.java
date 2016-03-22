package org.f0w.k2i.core.handler;

import com.google.inject.Inject;
import org.f0w.k2i.core.model.entity.ImportProgress;
import org.f0w.k2i.core.model.repository.ImportProgressRepository;
import org.f0w.k2i.core.model.repository.MovieRepository;

import static com.google.common.base.Preconditions.*;

abstract class AbstractMovieHandler implements MovieHandler {
    @Inject
    protected MovieRepository movieRepository;

    @Inject
    protected ImportProgressRepository importProgressRepository;

    protected ImportProgress importProgress;

    @Override
    public void setImportProgress(ImportProgress importProgress) {
        this.importProgress = checkNotNull(importProgress);
    }
}

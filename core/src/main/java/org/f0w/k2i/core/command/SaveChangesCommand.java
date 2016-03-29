package org.f0w.k2i.core.command;

import com.google.inject.Inject;
import org.f0w.k2i.core.model.entity.ImportProgress;
import org.f0w.k2i.core.model.repository.ImportProgressRepository;

import java.util.Optional;

public class SaveChangesCommand extends AbstractMovieCommand {
    private final ImportProgressRepository importProgressRepository;

    @Inject
    public SaveChangesCommand(ImportProgressRepository importProgressRepository) {
        this.importProgressRepository = importProgressRepository;
    }

    @Override
    public Optional<MovieError> execute(ImportProgress importProgress) {
        importProgressRepository.save(importProgress);

        LOG.info("Changes were successfully saved to storage");

        return Optional.empty();
    }
}

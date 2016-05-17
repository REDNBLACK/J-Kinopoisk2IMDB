package org.f0w.k2i.core.model.service;

import org.f0w.k2i.core.model.entity.ImportProgress;

import java.nio.file.Path;
import java.util.List;

public interface ImportProgressService {
    default List<ImportProgress> findOrSaveAll(Path filePath) {
        List<ImportProgress> found = findAll(filePath);

        if (found.isEmpty()) {
            saveAll(filePath);

            return findAll(filePath);
        }

        return found;
    }

    List<ImportProgress> findAll(Path filePath);

    ImportProgress save(ImportProgress importProgress);

    void saveAll(Path filePath);

    void deleteAll(Path filePath);
}

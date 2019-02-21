package org.f0w.k2i.core.model.service;

import com.typesafe.config.Config;
import org.f0w.k2i.core.model.entity.ImportProgress;

import java.nio.file.Path;
import java.util.List;

public interface ImportProgressService {
    default List<ImportProgress> findOrSaveAll(Path filePath, Config config) {
        List<ImportProgress> found = findAll(filePath, config);

        if (found.isEmpty()) {
            saveAll(filePath, config);

            return findAll(filePath, config);
        }

        return found;
    }

    List<ImportProgress> findAll(Path filePath, Config config);

    ImportProgress save(ImportProgress importProgress);

    void saveAll(Path filePath, Config config);

    void deleteAll(Path filePath);
}

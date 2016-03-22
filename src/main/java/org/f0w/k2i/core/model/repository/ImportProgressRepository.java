package org.f0w.k2i.core.model.repository;

import org.f0w.k2i.core.model.entity.ImportProgress;

import java.util.List;

public interface ImportProgressRepository {
    ImportProgress save(ImportProgress importProgress);

    List<ImportProgress> findNotImportedByFileId(int listId);

    List<ImportProgress> findNotRatedByFileId(int listId);
}

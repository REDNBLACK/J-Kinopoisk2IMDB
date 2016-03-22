package org.f0w.k2i.core.model.repository;

import com.google.inject.persist.PersistService;
import com.google.inject.persist.Transactional;
import org.f0w.k2i.core.model.entity.ImportProgress;

import com.google.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

public class ImportProgressRepositoryImpl implements ImportProgressRepository {
    @Inject
    private EntityManager em;

    @Inject
    public ImportProgressRepositoryImpl(PersistService service) {
        service.start();
    }

    @Override
    @Transactional
    public ImportProgress save(ImportProgress importProgress) {
        em.persist(importProgress);

        return importProgress;
    }

    @Override
    public List<ImportProgress> findNotImportedByFileId(int listId) {
        return null;
    }

    @Override
    public List<ImportProgress> findNotRatedByFileId(int listId) {
        return null;
    }
}

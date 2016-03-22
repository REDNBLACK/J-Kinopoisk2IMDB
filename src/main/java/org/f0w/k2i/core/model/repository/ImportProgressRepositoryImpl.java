package org.f0w.k2i.core.model.repository;

import com.google.inject.persist.Transactional;
import org.f0w.k2i.core.model.entity.ImportProgress;

import com.google.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class ImportProgressRepositoryImpl implements ImportProgressRepository {
    @Inject
    private EntityManager em;

    @Override
    @Transactional
    public ImportProgress save(ImportProgress importProgress) {
        em.persist(importProgress);

        return importProgress;
    }

    @Override
    public List<ImportProgress> findNotImportedByFileId(long kinopoiskFileId) {
        TypedQuery<ImportProgress> query = em.createQuery(
                "FROM ImportProgress WHERE imported = :imported AND kinopoiskFileId = :kinopoiskFileId",
                ImportProgress.class
        );
        query.setParameter("imported", false);
        query.setParameter("kinopoiskFileId", kinopoiskFileId);

        return query.getResultList();
    }

    @Override
    public List<ImportProgress> findNotRatedByFileId(long kinopoiskFileId) {
        TypedQuery<ImportProgress> query = em.createQuery(
                "FROM ImportProgress WHERE rated = :rated AND kinopoiskFileId = :kinopoiskFileId",
                ImportProgress.class
        );
        query.setParameter("rated", false);
        query.setParameter("kinopoiskFileId", kinopoiskFileId);

        return query.getResultList();
    }
}

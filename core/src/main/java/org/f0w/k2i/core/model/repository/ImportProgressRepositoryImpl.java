package org.f0w.k2i.core.model.repository;

import com.google.inject.persist.Transactional;
import org.f0w.k2i.core.model.entity.ImportProgress;

import com.google.inject.Inject;
import org.f0w.k2i.core.model.entity.KinopoiskFile;
import org.f0w.k2i.core.model.entity.Movie;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class ImportProgressRepositoryImpl implements ImportProgressRepository {
    @Inject
    private EntityManager em;

    @Override
    @Transactional
    public ImportProgress save(ImportProgress importProgress) {
        em.getTransaction().begin();
        em.persist(importProgress);
        em.getTransaction().commit();

        return importProgress;
    }

    @Override
    public void saveAll(KinopoiskFile kinopoiskFile, List<Movie> movies) {
        em.getTransaction().begin();
        movies.forEach(m -> em.persist(new ImportProgress(kinopoiskFile, m, false, false)));
        em.getTransaction().commit();
    }

    @Override
    public List<ImportProgress> findNotImportedOrNotRatedByFile(KinopoiskFile kinopoiskFile) {
        TypedQuery<ImportProgress> query = em.createQuery(
                "FROM ImportProgress WHERE (imported = :imported OR rated = :rated) AND kinopoiskFile = :kinopoiskFile",
                ImportProgress.class
        );
        query.setParameter("rated", false);
        query.setParameter("imported", false);
        query.setParameter("kinopoiskFile", kinopoiskFile);

        return query.getResultList();
    }
}

package org.f0w.k2i.core.model.repository.jpa;

import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import org.f0w.k2i.core.model.entity.ImportProgress;

import com.google.inject.Inject;
import org.f0w.k2i.core.model.entity.KinopoiskFile;
import org.f0w.k2i.core.model.entity.Movie;
import org.f0w.k2i.core.model.repository.ImportProgressRepository;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

public class JpaImportProgressRepositoryImpl implements ImportProgressRepository {
    @Inject
    private Provider<EntityManager> emProvider;

    @Override
    @Transactional
    public ImportProgress save(ImportProgress importProgress) {
        emProvider.get().persist(importProgress);

        return importProgress;
    }

    @Override
    @Transactional
    public void saveAll(KinopoiskFile kinopoiskFile, List<Movie> movies) {
        EntityManager em = emProvider.get();

        movies.forEach(m -> em.persist(new ImportProgress(kinopoiskFile, m, false, false)));
    }

    @Override
    public List<ImportProgress> findNotImportedOrNotRatedByFile(KinopoiskFile kinopoiskFile) {
        TypedQuery<ImportProgress> query = emProvider.get().createQuery(
                "FROM ImportProgress WHERE (imported = :imported OR rated = :rated) AND kinopoiskFile = :kinopoiskFile",
                ImportProgress.class
        );
        query.setParameter("rated", false);
        query.setParameter("imported", false);
        query.setParameter("kinopoiskFile", kinopoiskFile);

        return query.getResultList();
    }
}

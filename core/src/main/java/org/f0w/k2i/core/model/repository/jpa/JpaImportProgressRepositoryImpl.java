package org.f0w.k2i.core.model.repository.jpa;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import org.f0w.k2i.core.model.entity.ImportProgress;
import org.f0w.k2i.core.model.entity.KinopoiskFile;
import org.f0w.k2i.core.model.entity.Movie;
import org.f0w.k2i.core.model.repository.ImportProgressRepository;

import javax.persistence.EntityManager;
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
    @Transactional
    public void deleteAll(KinopoiskFile kinopoiskFile) {
        emProvider.get()
                .createQuery("DELETE FROM ImportProgress WHERE kinopoiskFile.id = :kinopoiskFileId")
                .setParameter("kinopoiskFileId", kinopoiskFile.getId())
                .executeUpdate();
    }

    @Override
    public List<ImportProgress> findNotImportedOrNotRatedByFile(KinopoiskFile kinopoiskFile) {
        return emProvider.get()
                .createQuery(
                    "FROM ImportProgress WHERE (imported = :imported OR (rated = :rated AND movie.rating IS NOT NULL))"
                            + "AND kinopoiskFile = :kinopoiskFile",
                    ImportProgress.class
                )
                .setParameter("imported", false)
                .setParameter("rated", false)
                .setParameter("kinopoiskFile", kinopoiskFile)
                .getResultList();
    }

    @Override
    public List<ImportProgress> findNotImportedByFile(KinopoiskFile kinopoiskFile) {
        return emProvider.get()
                .createQuery(
                        "FROM ImportProgress WHERE imported = :imported AND kinopoiskFile = :kinopoiskFile",
                        ImportProgress.class
                )
                .setParameter("imported", false)
                .setParameter("kinopoiskFile", kinopoiskFile)
                .getResultList();
    }

    @Override
    public List<ImportProgress> findNotRatedByFile(KinopoiskFile kinopoiskFile) {
        return emProvider.get()
                .createQuery(
                        "FROM ImportProgress WHERE rated = :rated"
                                + " AND kinopoiskFile = :kinopoiskFile AND movie.rating IS NOT NULL",
                        ImportProgress.class
                )
                .setParameter("rated", false)
                .setParameter("kinopoiskFile", kinopoiskFile)
                .getResultList();
    }
}

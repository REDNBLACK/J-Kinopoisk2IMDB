package org.f0w.k2i.core.model.repository.jpa;

import com.google.inject.persist.Transactional;
import com.typesafe.config.Config;
import org.f0w.k2i.core.model.entity.ImportProgress;
import org.f0w.k2i.core.model.entity.KinopoiskFile;
import org.f0w.k2i.core.model.entity.Movie;
import org.f0w.k2i.core.model.repository.ImportProgressRepository;

import javax.persistence.EntityManager;
import java.util.List;

public class JpaImportProgressRepositoryImpl extends BaseJPARepository<ImportProgress, Long>
        implements ImportProgressRepository {
    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void saveAll(KinopoiskFile kinopoiskFile, List<Movie> movies, Config config) {
        EntityManager em = entityManagerProvider.get();
        String listId = config.getString("list");

        movies.forEach(m -> em.persist(new ImportProgress(kinopoiskFile, m, listId, false, false)));

    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public void deleteAll(KinopoiskFile kinopoiskFile) {
        entityManagerProvider.get()
                .createQuery("DELETE FROM ImportProgress WHERE kinopoiskFile.id = :kinopoiskFileId")
                .setParameter("kinopoiskFileId", kinopoiskFile.getId())
                .executeUpdate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ImportProgress> findNotImportedOrNotRatedByFile(KinopoiskFile kinopoiskFile, String listId) {
        return entityManagerProvider.get()
                .createQuery(
                        "FROM ImportProgress WHERE (imported = :imported OR (rated = :rated AND movie.rating IS NOT NULL))"
                                + "AND kinopoiskFile = :kinopoiskFile AND listId = :listId",
                        ImportProgress.class
                )
                .setParameter("imported", false)
                .setParameter("rated", false)
                .setParameter("kinopoiskFile", kinopoiskFile)
                .setParameter("listId", listId)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ImportProgress> findNotImportedByFile(KinopoiskFile kinopoiskFile, String listId) {
        return entityManagerProvider.get()
                .createQuery(
                        "FROM ImportProgress WHERE imported = :imported AND kinopoiskFile = :kinopoiskFile AND listId = :listId",
                        ImportProgress.class
                )
                .setParameter("imported", false)
                .setParameter("kinopoiskFile", kinopoiskFile)
                .setParameter("listId", listId)
                .getResultList();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ImportProgress> findNotRatedByFile(KinopoiskFile kinopoiskFile, String listId) {
        return entityManagerProvider.get()
                .createQuery(
                        "FROM ImportProgress WHERE rated = :rated"
                                + " AND kinopoiskFile = :kinopoiskFile AND movie.rating IS NOT NULL AND listId = :listId",
                        ImportProgress.class
                )
                .setParameter("rated", false)
                .setParameter("kinopoiskFile", kinopoiskFile)
                .setParameter("listId", listId)
                .getResultList();
    }
}

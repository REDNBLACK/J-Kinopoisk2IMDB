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

        movies.forEach(movie -> {
            ImportProgress existingImportProgress = this.find(kinopoiskFile, movie, listId);
            if (existingImportProgress == null) {
                em.persist(this.save(kinopoiskFile, movie, listId));
            }
        });
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    public ImportProgress save(KinopoiskFile kinopoiskFile, Movie movie, String listId) {
        return new ImportProgress(kinopoiskFile, movie, listId, false, false);
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

    public ImportProgress find(KinopoiskFile kinopoiskFile, Movie movie, String listId) {
        List<ImportProgress> resultList = entityManagerProvider.get()
                .createQuery(
                        "FROM ImportProgress WHERE movie.id = :movieId AND kinopoiskFile = :kinopoiskFile AND listId = :listId",
                        ImportProgress.class
                )
                .setParameter("kinopoiskFile", kinopoiskFile)
                .setParameter("movieId", movie.getId())
                .setParameter("listId", listId)
                .getResultList();

        if (resultList.isEmpty()) return null;

        return resultList.get(0);
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
    public List<ImportProgress> findNotRatedByFile(KinopoiskFile kinopoiskFile) {
        return entityManagerProvider.get()
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

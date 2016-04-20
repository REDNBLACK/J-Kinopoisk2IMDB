package org.f0w.k2i.core.model.repository.jpa;

import com.google.inject.persist.Transactional;
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
    public void saveAll(KinopoiskFile kinopoiskFile, List<Movie> movies) {
        EntityManager em = entityManagerProvider.get();

        movies.forEach(m -> em.persist(new ImportProgress(kinopoiskFile, m, false, false)));
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
    public List<ImportProgress> findNotImportedOrNotRatedByFile(KinopoiskFile kinopoiskFile) {
        return entityManagerProvider.get()
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

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ImportProgress> findNotImportedByFile(KinopoiskFile kinopoiskFile) {
        return entityManagerProvider.get()
                .createQuery(
                        "FROM ImportProgress WHERE imported = :imported AND kinopoiskFile = :kinopoiskFile",
                        ImportProgress.class
                )
                .setParameter("imported", false)
                .setParameter("kinopoiskFile", kinopoiskFile)
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

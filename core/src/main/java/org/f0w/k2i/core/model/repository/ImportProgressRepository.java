package org.f0w.k2i.core.model.repository;

import org.f0w.k2i.core.model.entity.ImportProgress;
import org.f0w.k2i.core.model.entity.KinopoiskFile;
import org.f0w.k2i.core.model.entity.Movie;

import java.util.List;

public interface ImportProgressRepository extends Repository<ImportProgress, Long> {
    /**
     * Associate all movies with kinopoisk file
     *
     * @param kinopoiskFile KinopoiskFile
     * @param movies        List of movies
     */
    void saveAll(KinopoiskFile kinopoiskFile, List<Movie> movies);

    /**
     * Delete all movies associated with KinopoiskFile
     *
     * @param kinopoiskFile KinopoiskFile
     */
    void deleteAll(KinopoiskFile kinopoiskFile);

    /**
     * Find all entities using KinopoiskFile,
     * which {@link ImportProgress#imported} or {@link ImportProgress#rated} equals to false
     *
     * @param kinopoiskFile KinopoiskFile
     * @return List of found entities
     */
    List<ImportProgress> findNotImportedOrNotRatedByFile(KinopoiskFile kinopoiskFile);

    /**
     * Find all entities using KinopoiskFile which {@link ImportProgress#imported} equals to false
     *
     * @param kinopoiskFile KinopoiskFile
     * @return List of found entities
     */
    List<ImportProgress> findNotImportedByFile(KinopoiskFile kinopoiskFile);

    /**
     * Find all entities using KinopoiskFile which {@link ImportProgress#rated} equals to false
     *
     * @param kinopoiskFile KinopoiskFile
     * @return List of found entities
     */
    List<ImportProgress> findNotRatedByFile(KinopoiskFile kinopoiskFile);

    /**
     * {@inheritDoc}
     */
    @Override
    default Class<ImportProgress> getType() {
        return ImportProgress.class;
    }
}

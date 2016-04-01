package org.f0w.k2i.core.model.repository;

import org.f0w.k2i.core.model.entity.ImportProgress;
import org.f0w.k2i.core.model.entity.KinopoiskFile;
import org.f0w.k2i.core.model.entity.Movie;

import java.util.List;

public interface ImportProgressRepository {
    ImportProgress save(ImportProgress importProgress);

    void saveAll(KinopoiskFile kinopoiskFile, List<Movie> movies);

    void deleteAll(KinopoiskFile kinopoiskFile);

    List<ImportProgress> findNotImportedOrNotRatedByFile(KinopoiskFile kinopoiskFile);

    List<ImportProgress> findNotImportedByFile(KinopoiskFile kinopoiskFile);

    List<ImportProgress> findNotRatedByFile(KinopoiskFile kinopoiskFile);
}

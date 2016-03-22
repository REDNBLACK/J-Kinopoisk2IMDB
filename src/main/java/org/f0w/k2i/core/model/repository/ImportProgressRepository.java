package org.f0w.k2i.core.model.repository;

import org.f0w.k2i.core.model.entity.KinopoiskFile;
import org.f0w.k2i.core.model.entity.Movie;

import java.util.List;

public interface ImportProgressRepository {
    boolean saveAll(KinopoiskFile file, List<Movie> movies);
}

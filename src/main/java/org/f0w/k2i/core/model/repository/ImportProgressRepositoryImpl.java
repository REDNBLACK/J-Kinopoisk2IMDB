package org.f0w.k2i.core.model.repository;

import org.f0w.k2i.core.model.entity.KinopoiskFile;
import org.f0w.k2i.core.model.entity.Movie;

import com.google.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

public class ImportProgressRepositoryImpl implements ImportProgressRepository {
    @Inject
    private EntityManager em;

    @Override
    public boolean saveAll(KinopoiskFile file, List<Movie> movies) {
        return false;
    }
}

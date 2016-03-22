package org.f0w.k2i.core.model.repository;

import org.f0w.k2i.core.model.entity.Movie;

import com.google.inject.Inject;
import javax.persistence.EntityManager;

public class MovieRepositoryImpl implements MovieRepository {
    @Inject
    private EntityManager em;

    @Override
    public Movie save(Movie movie) {
        return null;
    }

    @Override
    public Movie findByTitleAndYear(String title, int year) {
        return null;
    }
}

package org.f0w.k2i.core.model.repository;

import org.f0w.k2i.core.model.entity.Movie;

public interface MovieRepository {
    Movie save(Movie movie);

    Movie findByTitleAndYear(final String title, final int year);
}

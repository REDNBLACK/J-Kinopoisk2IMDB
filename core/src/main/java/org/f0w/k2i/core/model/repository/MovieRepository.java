package org.f0w.k2i.core.model.repository;

import org.f0w.k2i.core.model.entity.Movie;

public interface MovieRepository extends Repository<Movie, Long> {
    /**
     * Find movie by given title and year
     *
     * @param title Movie title
     * @param year  Movie year
     * @return Found movie or null
     */
    Movie findByTitleAndYear(final String title, final int year);

    /**
     * {@inheritDoc}
     */
    @Override
    default Class<Movie> getType() {
        return Movie.class;
    }
}

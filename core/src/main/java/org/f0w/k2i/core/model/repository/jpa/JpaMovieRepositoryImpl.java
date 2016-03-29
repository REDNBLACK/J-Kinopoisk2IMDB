package org.f0w.k2i.core.model.repository.jpa;

import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import org.f0w.k2i.core.model.entity.Movie;

import com.google.inject.Inject;
import org.f0w.k2i.core.model.repository.MovieRepository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class JpaMovieRepositoryImpl implements MovieRepository {
    @Inject
    private Provider<EntityManager> emProvider;

    @Override
    @Transactional
    public Movie save(Movie movie) {
        emProvider.get().persist(movie);

        return movie;
    }

    @Override
    public Movie findOrCreate(Movie movie) {
        Movie existingMovie = findByTitleAndYear(movie.getTitle(), movie.getYear());

        if (existingMovie == null) {
            return save(movie);
        } else {
            return existingMovie;
        }
    }

    @Override
    public Movie findByTitleAndYear(String title, int year) {
        TypedQuery<Movie> query = emProvider.get().createQuery(
                "FROM Movie m WHERE m.title = :title AND m.year = :year",
                Movie.class
        );
        query.setParameter("title", title);
        query.setParameter("year", year);

        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }
}

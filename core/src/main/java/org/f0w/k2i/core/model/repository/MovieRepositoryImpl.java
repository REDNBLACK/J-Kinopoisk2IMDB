package org.f0w.k2i.core.model.repository;

import org.f0w.k2i.core.model.entity.Movie;

import com.google.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;

public class MovieRepositoryImpl implements MovieRepository {
    @Inject
    private EntityManager em;

    @Override
    public Movie findOrCreate(Movie movie) {
        Movie existingMovie = findByTitleAndYear(movie.getTitle(), movie.getYear());

        if (existingMovie == null) {
            em.getTransaction().begin();
            em.persist(movie);
            em.getTransaction().commit();

            return movie;
        } else {
            return existingMovie;
        }
    }

    @Override
    public Movie findByTitleAndYear(String title, int year) {
        TypedQuery<Movie> query = em.createQuery(
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

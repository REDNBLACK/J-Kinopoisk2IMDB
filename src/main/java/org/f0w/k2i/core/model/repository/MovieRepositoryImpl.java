package org.f0w.k2i.core.model.repository;

import com.google.inject.persist.PersistService;
import com.google.inject.persist.Transactional;
import org.f0w.k2i.core.model.entity.Movie;

import com.google.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

public class MovieRepositoryImpl implements MovieRepository {
    @Inject
    private EntityManager em;

    @Inject
    public MovieRepositoryImpl(PersistService service) {
        service.start();
    }

    @Override
    @Transactional
    public Movie save(Movie movie) {
        em.persist(movie);

        return movie;
    }

    @Override
    public Movie findByTitleAndYear(String title, int year) {
        TypedQuery<Movie> query = em.createQuery(
                "FROM Movie m WHERE m.title = :title AND m.year = :year",
                Movie.class
        );
        query.setParameter("title", title);
        query.setParameter("year", year);

        return query.getSingleResult();
    }
}

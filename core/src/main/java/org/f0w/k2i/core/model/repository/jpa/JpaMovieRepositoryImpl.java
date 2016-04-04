package org.f0w.k2i.core.model.repository.jpa;

import com.google.inject.Provider;
import com.google.inject.persist.Transactional;
import org.f0w.k2i.core.model.entity.Movie;

import com.google.inject.Inject;
import org.f0w.k2i.core.model.repository.MovieRepository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    @Transactional
    public List<Movie> saveAllNotExisting(final List<Movie> movies) {
        return movies.stream()
                .map(m -> Optional
                        .ofNullable(findByTitleAndYear(m.getTitle(), m.getYear()))
                        .orElseGet(() -> save(m))
                )
                .collect(Collectors.toList());
    }

    @Override
    public Movie findByTitleAndYear(final String title, final int year) {
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

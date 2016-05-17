package org.f0w.k2i.core.model.repository.mock;

import org.f0w.k2i.core.model.entity.Movie;
import org.f0w.k2i.core.model.repository.MovieRepository;

public class MockMovieRepositoryImpl extends BaseMockRepository<Movie> implements MovieRepository {
    @Override
    public Movie findByTitleAndYear(String title, int year) {
        return storage.values()
                .stream()
                .filter(m -> m.getTitle().equals(title) && m.getYear() == year)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Movie saveOrUpdate(Movie movie) {
        Movie oldMovie = findByTitleAndYear(movie.getTitle(), movie.getYear());

        if (oldMovie == null) {
            return save(movie);
        }

        if (oldMovie.getRating() == null && movie.getRating() != null) {
            oldMovie.setRating(movie.getRating());

            return save(oldMovie);
        }

        return oldMovie;
    }
}

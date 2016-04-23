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
}

package org.f0w.k2i.core.model.service;

import com.google.inject.Inject;
import org.f0w.k2i.core.model.entity.Movie;
import org.f0w.k2i.core.model.repository.MovieRepository;
import org.f0w.k2i.core.parser.MovieParsers;

import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static org.f0w.k2i.core.util.IOUtils.readFile;

public class MovieServiceImpl implements MovieService {
    private final MovieRepository repository;

    @Inject
    public MovieServiceImpl(MovieRepository repository) {
        this.repository = repository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Movie> saveOrUpdateAll(Path filePath) {
        return MovieParsers.fileParser()
                .parse(readFile(filePath, Charset.forName("windows-1251")))
                .stream()
                .map(repository::saveOrUpdate)
                .collect(Collectors.toList());
    }
}

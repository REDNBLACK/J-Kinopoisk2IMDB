package org.f0w.k2i.core.model.service;

import org.f0w.k2i.core.model.entity.Movie;

import java.nio.file.Path;
import java.util.List;

public interface MovieService {
    List<Movie> saveOrUpdateAll(Path filePath);
}

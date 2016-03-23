package org.f0w.k2i.core.handler;

import org.f0w.k2i.core.MovieManager;
import org.f0w.k2i.core.exchange.MovieRatingChanger;

import com.google.inject.Inject;
import org.f0w.k2i.core.model.entity.ImportProgress;
import org.f0w.k2i.core.model.entity.Movie;
import org.f0w.k2i.core.model.repository.ImportProgressRepository;

import java.io.IOException;
import java.util.List;

class SetRatingMovieHandler extends AbstractMovieHandler {
    private MovieRatingChanger changer;

    @Inject
    public SetRatingMovieHandler(
            ImportProgressRepository importProgressRepository,
            MovieManager movieManager,
            MovieRatingChanger changer
    ) {
        super(importProgressRepository, movieManager);
        this.changer = changer;
    }

    @Override
    public int execute() {
        List<ImportProgress> importProgress = importProgressRepository.findNotRatedByFile(kinopoiskFile);

        int successCounter = 0;

        for (ImportProgress progress : importProgress) {
            try {
                successCounter++;

                LOG.info("Setting rating of movie: {}", progress.getMovie());

                movieManager.setMovie(progress.getMovie()).prepare();

                Movie preparedMovie = movieManager.getMovie();

                changer.sendRequest(preparedMovie);

                progress.setImported(true);
                progress.setMovie(preparedMovie);

                importProgressRepository.save(progress);

                LOG.info("Rating was successfuly set");
            } catch (IOException e) {
                LOG.info("Error setting rating: {}", e.getMessage());
                successCounter--;
            }
        }

        return successCounter;
    }
}

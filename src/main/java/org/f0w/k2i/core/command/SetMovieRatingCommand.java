package org.f0w.k2i.core.command;

import com.google.inject.Inject;
import org.f0w.k2i.core.exchange.MovieRatingChanger;
import org.f0w.k2i.core.model.entity.ImportProgress;
import org.f0w.k2i.core.model.entity.Movie;

import java.io.IOException;

import static org.f0w.k2i.core.utils.MovieFieldsUtils.*;

public class SetMovieRatingCommand extends AbstractMovieCommand {
    private final MovieRatingChanger changer;

    @Inject
    public SetMovieRatingCommand(MovieRatingChanger changer) {
        this.changer = changer;
    }

    @Override
    public void execute(ImportProgress importProgress) {
        try {
            Movie movie = importProgress.getMovie();

            if (isEmptyIMDBId(movie.getImdbId())) {
                LOG.info("Can't change movie rating, IMDB ID is not set");
                return;
            }

            if (isEmptyRating(movie.getRating())) {
                LOG.info("Can't change movie rating, rating is empty");
                return;
            }

            LOG.info("Setting rating of movie: {}", movie);

            if (importProgress.isRated()) {
                LOG.info("Movie rating is already set!");
                return;
            }

            changer.sendRequest(movie);

            importProgress.setRated(true);

            LOG.info("Movie rating was successfully set");
        } catch (IOException e) {
            LOG.info("Error setting rating of movie: {}", e);
        }
    }
}

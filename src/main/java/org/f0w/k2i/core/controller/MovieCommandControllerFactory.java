package org.f0w.k2i.core.controller;

import com.google.inject.Inject;
import com.google.inject.Provider;
import org.f0w.k2i.core.command.AddMovieToWatchlistCommand;
import org.f0w.k2i.core.command.Command;
import org.f0w.k2i.core.command.ParseIMDBMovieIDCommand;
import org.f0w.k2i.core.command.SetMovieRatingCommand;
import org.f0w.k2i.core.model.entity.ImportProgress;

import java.util.Arrays;
import java.util.List;

public class MovieCommandControllerFactory {
    private final Provider<ParseIMDBMovieIDCommand> parseIMDBMovieIDCommandProvider;
    private final Provider<AddMovieToWatchlistCommand> addMovieToWatchlistCommandProvider;
    private final Provider<SetMovieRatingCommand> setMovieRatingCommandProvider;

    @Inject
    public MovieCommandControllerFactory(
            Provider<ParseIMDBMovieIDCommand> parseIMDBMovieIDCommandProvider,
            Provider<AddMovieToWatchlistCommand> addMovieToWatchlistCommandProvider,
            Provider<SetMovieRatingCommand> setMovieRatingCommandProvider
    ) {
        this.parseIMDBMovieIDCommandProvider = parseIMDBMovieIDCommandProvider;
        this.addMovieToWatchlistCommandProvider = addMovieToWatchlistCommandProvider;
        this.setMovieRatingCommandProvider = setMovieRatingCommandProvider;
    }

    public MovieCommandController make(MovieCommandController.Type movieHandlerType) {
        switch (movieHandlerType) {
            case ADD_TO_WATCHLIST:
                return makeAddToWatchListHandler();
            case SET_RATING:
                return makeSetRatingHandler();
            case COMBINED:
                return makeCombinedHandler();
            default:
                throw new IllegalArgumentException("Unexpected comparator type!");
        }
    }

    private MovieCommandController makeAddToWatchListHandler() {
        List<Command<ImportProgress>> commands = Arrays.asList(
                parseIMDBMovieIDCommandProvider.get(),
                addMovieToWatchlistCommandProvider.get()
        );

        return new MovieCommandControllerImpl(commands);
    }

    private MovieCommandController makeSetRatingHandler() {
        List<Command<ImportProgress>> commands = Arrays.asList(
                parseIMDBMovieIDCommandProvider.get(),
                setMovieRatingCommandProvider.get()
        );

        return new MovieCommandControllerImpl(commands);
    }

    private MovieCommandController makeCombinedHandler() {
        List<Command<ImportProgress>> commands = Arrays.asList(
                parseIMDBMovieIDCommandProvider.get(),
                setMovieRatingCommandProvider.get(),
                addMovieToWatchlistCommandProvider.get()
        );

        return new MovieCommandControllerImpl(commands);
    }
}

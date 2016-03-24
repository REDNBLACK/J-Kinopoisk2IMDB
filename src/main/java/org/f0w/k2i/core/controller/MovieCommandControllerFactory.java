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
                return makeAddToWatchListController();
            case SET_RATING:
                return makeSetRatingController();
            case COMBINED:
                return makeCombinedController();
            default:
                throw new IllegalArgumentException("Unexpected comparator type!");
        }
    }

    private MovieCommandController makeAddToWatchListController() {
        List<Command<ImportProgress>> commands = Arrays.asList(
                parseIMDBMovieIDCommandProvider.get(),
                addMovieToWatchlistCommandProvider.get()
        );

        return new MovieCommandControllerImpl(commands);
    }

    private MovieCommandController makeSetRatingController() {
        List<Command<ImportProgress>> commands = Arrays.asList(
                parseIMDBMovieIDCommandProvider.get(),
                setMovieRatingCommandProvider.get()
        );

        return new MovieCommandControllerImpl(commands);
    }

    private MovieCommandController makeCombinedController() {
        List<Command<ImportProgress>> commands = Arrays.asList(
                parseIMDBMovieIDCommandProvider.get(),
                setMovieRatingCommandProvider.get(),
                addMovieToWatchlistCommandProvider.get()
        );

        return new MovieCommandControllerImpl(commands);
    }

    private final class MovieCommandControllerImpl implements MovieCommandController {
        private final List<Command<ImportProgress>> commands;

        public MovieCommandControllerImpl(List<Command<ImportProgress>> commands) {
            this.commands = commands;
        }

        @Override
        public void execute(ImportProgress importProgress) {
            commands.forEach(c -> c.execute(importProgress));
        }
    }
}

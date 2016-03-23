package org.f0w.k2i.core.handler;

import com.google.inject.Inject;
import com.google.inject.Provider;
import com.typesafe.config.Config;
import org.f0w.k2i.core.exchange.MovieRatingChanger;
import org.f0w.k2i.core.exchange.MovieWatchlistAssigner;
import org.f0w.k2i.core.exchange.finder.MovieFindersFactory;
import org.f0w.k2i.core.handler.command.AddMovieToWatchlistCommand;
import org.f0w.k2i.core.handler.command.Command;
import org.f0w.k2i.core.handler.command.ParseIMDBMovieIDCommand;
import org.f0w.k2i.core.handler.command.SetMovieRatingCommand;
import org.f0w.k2i.core.model.entity.ImportProgress;

import java.util.Arrays;
import java.util.List;

public class MovieHandlerFactory {
    private final Provider<Config> configProvider;
    private final Provider<MovieFindersFactory> movieFindersFactoryProvider;
    private final Provider<MovieWatchlistAssigner> movieWatchlistAssignerProvider;
    private final Provider<MovieRatingChanger> movieRatingChangerProvider;

    @Inject
    public MovieHandlerFactory(
            Provider<Config> configProvider,
            Provider<MovieFindersFactory> movieFindersFactoryProvider,
            Provider<MovieWatchlistAssigner> movieWatchlistAssignerProvider,
            Provider<MovieRatingChanger> movieRatingChangerProvider
    ) {
        this.configProvider = configProvider;
        this.movieFindersFactoryProvider = movieFindersFactoryProvider;
        this.movieWatchlistAssignerProvider = movieWatchlistAssignerProvider;
        this.movieRatingChangerProvider = movieRatingChangerProvider;
    }

    public MovieHandler make(MovieHandler.Type movieHandlerType) {
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

    private MovieHandler makeAddToWatchListHandler() {
        List<Command<ImportProgress>> commands = Arrays.asList(
                new ParseIMDBMovieIDCommand(configProvider.get(), movieFindersFactoryProvider.get()),
                new AddMovieToWatchlistCommand(movieWatchlistAssignerProvider.get())
        );

        return new MovieHandlerImpl(commands);
    }

    private MovieHandler makeSetRatingHandler() {
        List<Command<ImportProgress>> commands = Arrays.asList(
                new ParseIMDBMovieIDCommand(configProvider.get(), movieFindersFactoryProvider.get()),
                new SetMovieRatingCommand(movieRatingChangerProvider.get())
        );

        return new MovieHandlerImpl(commands);
    }

    private MovieHandler makeCombinedHandler() {
        List<Command<ImportProgress>> commands = Arrays.asList(
                new ParseIMDBMovieIDCommand(configProvider.get(), movieFindersFactoryProvider.get()),
                new SetMovieRatingCommand(movieRatingChangerProvider.get()),
                new AddMovieToWatchlistCommand(movieWatchlistAssignerProvider.get())
        );

        return new MovieHandlerImpl(commands);
    }
}

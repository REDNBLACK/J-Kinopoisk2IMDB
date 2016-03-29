package org.f0w.k2i.core.providers;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.typesafe.config.Config;
import org.f0w.k2i.core.command.*;
import org.f0w.k2i.core.comparator.MovieComparator;
import org.f0w.k2i.core.exchange.finder.MovieFinder;
import org.f0w.k2i.core.exchange.finder.MovieFinderFactory;
import org.f0w.k2i.core.util.ReflectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class SystemProvider extends AbstractModule {
    @Override
    protected void configure() {
        // Do nothing
    }

    @Provides
    MovieFinder provideMovieFinder(Config config, MovieFinderFactory movieFinderFactory) {
        return movieFinderFactory.make(MovieFinder.Type.valueOf(config.getString("query_format")));
    }

    @Provides
    List<MovieCommand> provideCommands(
            Config config,
            Provider<ParseIDCommand> parseIDCommandProvider,
            Provider<AddToWatchlistCommand> addToWatchlistCommandProvider,
            Provider<SetRatingCommand> setRatingCommandProvider,
            Provider<SaveChangesCommand> saveChangesCommandProvider
    ) {
        MovieCommand.Type commandType = MovieCommand.Type.valueOf(config.getString("mode"));

        switch (commandType) {
            case SET_RATING:
                return Arrays.asList(
                        parseIDCommandProvider.get(),
                        setRatingCommandProvider.get(),
                        saveChangesCommandProvider.get()
                );
            case ADD_TO_WATCHLIST:
                return Arrays.asList(
                        parseIDCommandProvider.get(),
                        addToWatchlistCommandProvider.get(),
                        saveChangesCommandProvider.get()
                );
            case COMBINED:
            default:
                return Arrays.asList(
                        parseIDCommandProvider.get(),
                        setRatingCommandProvider.get(),
                        addToWatchlistCommandProvider.get(),
                        saveChangesCommandProvider.get()
                );
        }
    }

    @Provides
    MovieComparator provideMovieComparator(Config config, Injector injector) {
        List<MovieComparator> comparators = config.getStringList("comparators")
                .stream()
                .map(c -> ReflectionUtils.stringToClass(c, MovieComparator.class))
                .map(injector::getInstance)
                .collect(Collectors.toList());

        return (m1, m2) -> comparators.stream().allMatch(c -> c.areEqual(m1, m2));
    }
}

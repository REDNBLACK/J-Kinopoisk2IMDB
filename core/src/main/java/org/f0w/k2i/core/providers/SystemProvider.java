package org.f0w.k2i.core.providers;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.typesafe.config.Config;
import org.f0w.k2i.core.comparator.MovieComparator;
import org.f0w.k2i.core.comparator.MovieComparatorFactory;
import org.f0w.k2i.core.exchange.finder.MovieFinder;
import org.f0w.k2i.core.exchange.finder.MovieFinderFactory;
import org.f0w.k2i.core.handler.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Guice module providing base system binds
 */
public class SystemProvider extends AbstractModule {
    @Override
    protected void configure() {
        // Do nothing
    }

    @Provides
    MovieFinder provideMovieFinder(Config config, MovieFinderFactory factory) {
        return factory.make(MovieFinder.Type.valueOf(config.getString("query_format")));
    }

    @Provides
    MovieComparator provideMovieComparator(Config config, MovieComparatorFactory factory) {
        List<MovieComparator> comparators = config.getStringList("comparators")
                .stream()
                .map(MovieComparator.Type::valueOf)
                .distinct()
                .map(factory::make)
                .collect(Collectors.toList());

        return (m1, m2) -> comparators.stream().allMatch(c -> c.areEqual(m1, m2));
    }

    @Provides
    MovieHandler provideMovieHandler(Injector injector) {
        MovieHandler chain = injector.getInstance(ConnectionCheckHandler.class);

        chain.setNext(injector.getInstance(ParseIDHandler.class))
                .setNext(injector.getInstance(SetRatingHandler.class))
                .setNext(injector.getInstance(AddToWatchlistHandler.class))
                .setNext(injector.getInstance(SaveChangesHandler.class));

        return chain;
    }
}

package org.f0w.k2i.core.providers;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.typesafe.config.Config;
import org.f0w.k2i.core.DocumentSourceType;
import org.f0w.k2i.core.comparator.MovieComparator;
import org.f0w.k2i.core.comparator.MovieComparatorFactory;
import org.f0w.k2i.core.exchange.finder.MovieFinder;
import org.f0w.k2i.core.exchange.finder.MovieFinderFactory;
import org.f0w.k2i.core.handler.*;

import static org.f0w.k2i.core.handler.MovieHandler.Type;

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
        DocumentSourceType[] types = config.getStringList("document_source_types")
                .stream()
                .map(DocumentSourceType::valueOf)
                .toArray(DocumentSourceType[]::new);

        return factory.make(types);
    }

    @Provides
    MovieComparator provideMovieComparator(Config config, MovieComparatorFactory factory) {
        MovieComparator.Type[] types = config.getStringList("comparators")
                .stream()
                .map(MovieComparator.Type::valueOf)
                .toArray(MovieComparator.Type[]::new);

        return factory.make(types);
    }

    @Provides
    MovieHandler provideMovieHandler(Injector injector) {
        MovieHandler chain = injector.getInstance(ConnectionCheckHandler.class).setTypes(Type.COMBINED);

        chain.setNext(injector.getInstance(ParseIDHandler.class).setTypes(Type.COMBINED))
                .setNext(injector.getInstance(SetRatingHandler.class).setTypes(Type.SET_RATING))
                .setNext(injector.getInstance(AddToWatchlistHandler.class).setTypes(Type.ADD_TO_WATCHLIST))
                .setNext(injector.getInstance(SaveChangesHandler.class).setTypes(Type.COMBINED));

        return chain;
    }
}

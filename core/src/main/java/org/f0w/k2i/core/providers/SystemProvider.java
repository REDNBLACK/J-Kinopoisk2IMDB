package org.f0w.k2i.core.providers;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.typesafe.config.Config;
import org.f0w.k2i.core.command.CompositeCommandFactory;
import org.f0w.k2i.core.command.MovieCommand;
import org.f0w.k2i.core.comparator.MovieComparator;
import org.f0w.k2i.core.exchange.finder.MovieFinder;
import org.f0w.k2i.core.exchange.finder.MovieFinderFactory;
import org.f0w.k2i.core.util.ReflectionUtils;

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
    MovieCommand provideMovieCommand(Config config, CompositeCommandFactory compositeCommandFactory) {
        return compositeCommandFactory.make(MovieCommand.Type.valueOf(config.getString("mode")));
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

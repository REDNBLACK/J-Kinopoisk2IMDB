package org.f0w.k2i.core.ioc;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.assistedinject.FactoryModuleBuilder;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.typesafe.config.Config;
import org.f0w.k2i.core.DocumentSourceType;
import org.f0w.k2i.core.comparator.MovieComparator;
import org.f0w.k2i.core.exchange.MovieRatingChanger;
import org.f0w.k2i.core.exchange.MovieRatingChangerFactory;
import org.f0w.k2i.core.handler.*;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Guice module providing configuration.
 */
public class ConfigurationModule extends AbstractModule {
    private final Config config;

    public ConfigurationModule(Config config) {
        this.config = config;
    }

    @Override
    protected void configure() {
        bind(Config.class).toInstance(config);

        configureLogger();

        bindJPAPersistModule();
        bindFactories();
        bindMovieHandlerType();
        bindDocumentSourceTypes();
        bindMovieComparatorTypes();
    }

    protected void configureLogger() {
        Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.toLevel(config.getString("log_level")));
    }

    protected void bindJPAPersistModule() {
        JpaPersistModule jpa = new JpaPersistModule("K2IDB");

        Config dbConfig = config.getConfig("db");

        Properties properties = new Properties();
        properties.put("javax.persistence.jdbc.driver", dbConfig.getString("driver"));
        properties.put("javax.persistence.jdbc.url", dbConfig.getString("url"));
        properties.put("javax.persistence.jdbc.user", dbConfig.getString("user"));
        properties.put("javax.persistence.jdbc.password", dbConfig.getString("password"));

        dbConfig.getObject("additional").entrySet()
                .forEach(e -> properties.put(e.getKey(), (String) e.getValue().unwrapped()));

        jpa.properties(properties);
        install(jpa);
    }

    protected void bindFactories() {
        install(new FactoryModuleBuilder().build(MovieRatingChangerFactory.class));
    }

    protected void bindMovieHandlerType() {
        bind(MovieHandler.Type.class).toInstance(MovieHandler.Type.valueOf(config.getString("mode")));
    }

    protected void bindDocumentSourceTypes() {
        DocumentSourceType[] types = config.getStringList("document_source_types")
                .stream()
                .map(DocumentSourceType::valueOf)
                .toArray(DocumentSourceType[]::new);

        bind(DocumentSourceType[].class).toInstance(types);
    }

    protected void bindMovieComparatorTypes() {
        MovieComparator.Type[] types = config.getStringList("comparators")
                .stream()
                .map(MovieComparator.Type::valueOf)
                .toArray(MovieComparator.Type[]::new);

        bind(MovieComparator.Type[].class).toInstance(types);
    }

    @Provides
    MovieHandler provideMovieHandler(Injector injector) {
        MovieHandler chain = injector.getInstance(ConnectionCheckHandler.class).setTypes(MovieHandler.Type.COMBINED);

        chain.setNext(injector.getInstance(ParseIDHandler.class).setTypes(MovieHandler.Type.COMBINED))
                .setNext(injector.getInstance(SetRatingHandler.class).setTypes(MovieHandler.Type.SET_RATING))
                .setNext(injector.getInstance(AddToWatchlistHandler.class).setTypes(MovieHandler.Type.ADD_TO_WATCHLIST))
                .setNext(injector.getInstance(SaveChangesHandler.class).setTypes(MovieHandler.Type.COMBINED));

        return chain;
    }
}

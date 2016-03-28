package org.f0w.k2i.core.providers;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import com.google.inject.AbstractModule;
import com.google.inject.persist.jpa.JpaPersistModule;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.f0w.k2i.core.util.ConfigValidator;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class ConfigurationProvider extends AbstractModule {
    private final Config config;

    public ConfigurationProvider(Config config) {
        this.config = ConfigValidator.checkValid(config.withFallback(ConfigFactory.load()));
    }

    @Override
    protected void configure() {
        bind(Config.class).toInstance(config);
        configureLogger();
        configureDatabase();
    }

    private void configureLogger() {
        Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.toLevel(config.getString("log_level")));
    }

    private void configureDatabase() {
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
}

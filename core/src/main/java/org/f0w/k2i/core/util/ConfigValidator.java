package org.f0w.k2i.core.util;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
import org.apache.commons.lang3.EnumUtils;
import org.f0w.k2i.core.comparator.MovieComparator;
import org.f0w.k2i.core.exchange.finder.MovieFinder;
import org.f0w.k2i.core.handler.MovieHandler;

import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.base.Strings.isNullOrEmpty;

/**
 * Class for config validation
 */
public final class ConfigValidator {
    private final Config config;

    private ConfigValidator(Config config) {
        this.config = config;
    }

    /**
     * Validates given config, returns it on success or throws exception on error
     *
     * @param config Config to validate
     * @return Validated config
     * @throws ConfigException.Generic If validation of field fails
     */
    public static Config checkValid(Config config) {
        ConfigValidator validator = new ConfigValidator(config);

        try {
            checkNotNull(config);

            validator.checkLogLevel();

            validator.checkAuth();
            validator.checkMode();
            validator.checkList();

            validator.checkQueryFormat();
            validator.checkComparators();

            validator.checkUserAgent();
            validator.checkYearDeviation();
            validator.checkTimeout();
            validator.checkDataBase();
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new ConfigException.Generic(e.getMessage(), e);
        }

        return config;
    }

    /**
     * Checks the db and db.additional settings
     *
     * @throws ConfigException
     */
    private void checkDataBase() {
        final Config dbConfig = config.getConfig("db");

        checkArgument(!isNullOrEmpty(dbConfig.getString("driver")), "db.driver is not set!");
        checkArgument(!isNullOrEmpty(dbConfig.getString("url")), "db.url is not set!");

        dbConfig.getString("user");
        dbConfig.getString("password");
        dbConfig.getObject("additional");
    }

    /**
     * Checks the log_level string
     *
     * @throws ConfigException
     */
    private void checkLogLevel() {
        config.getString("log_level");
    }

    /**
     * Checks the user_agent string
     *
     * @throws IllegalArgumentException
     */
    private void checkUserAgent() {
        final String userAgent = config.getString("user_agent");

        checkArgument(!isNullOrEmpty(userAgent), "user_agent is not set!");
    }

    /**
     * Checks the year_deviation int
     *
     * @throws IllegalArgumentException If not valid
     */
    private void checkYearDeviation() {
        final int yearDeviation = config.getInt("year_deviation");

        checkArgument(yearDeviation > 0, "year_deviation is less than or equal to 0!");
    }

    /**
     * Checks the timeout int
     *
     * @throws IllegalArgumentException If not valid
     */
    private void checkTimeout() {
        final int timeout = config.getInt("timeout");

        checkArgument(timeout >= 1000, "timeout is less than 1000!");
    }

    /**
     * Checks the query_format string
     *
     * @throws IllegalArgumentException If not valid
     */
    private void checkQueryFormat() {
        final String queryFormat = config.getString("query_format");

        if (!EnumUtils.isValidEnum(MovieFinder.Type.class, queryFormat)) {
            throw new IllegalArgumentException("query_format is not valid!");
        }
    }

    /**
     * Checks the comparators string list
     *
     * @throws IllegalArgumentException If not valid
     */
    private void checkComparators() {
        final String message = "Comparators setting is not valid!";
        final List<String> comparators = config.getStringList("comparators");

        try {
            comparators.forEach(MovieComparator.Type::valueOf);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Checks the auth string
     *
     * @throws IllegalArgumentException If not valid
     */
    private void checkAuth() {
        final String auth = config.getString("auth");

        checkArgument(auth.length() > 10, "auth string length is less than or equal to 10!");
    }

    /**
     * Checks the list string
     *
     * @throws IllegalArgumentException If not valid
     */
    private void checkList() {
        final String list = config.getString("list");
        final String mode = config.getString("mode");

        if (isNullOrEmpty(list)) {
            MovieHandler.Type type = MovieHandler.Type.valueOf(mode);

            if (type.equals(MovieHandler.Type.COMBINED) || type.equals(MovieHandler.Type.ADD_TO_WATCHLIST)) {
                throw new IllegalArgumentException("list is not set, but required for current mode!");
            }
        } else {
            checkArgument(list.startsWith("ls"), "list doesn't start with ls prefix!");
            checkArgument(list.length() >= 3, "list string length less than 3!");
        }
    }

    /**
     * Checks the mode string
     *
     * @throws IllegalArgumentException If not valid
     */
    private void checkMode() {
        final String mode = config.getString("mode");

        if (!EnumUtils.isValidEnum(MovieHandler.Type.class, mode)) {
            throw new IllegalArgumentException("mode is not valid!");
        }
    }
}

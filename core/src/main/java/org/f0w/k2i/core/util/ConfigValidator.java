package org.f0w.k2i.core.util;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigException;
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
        } catch (NullPointerException | IllegalArgumentException e) {
            throw new ConfigException.Generic(e.getMessage(), e);
        }

        return config;
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
        final String message = "UserAgent setting is not valid!";
        final String userAgent = config.getString("user_agent");

        checkArgument(!isNullOrEmpty(userAgent), message);
    }

    /**
     * Checks the year_deviation int
     *
     * @throws IllegalArgumentException If not valid
     */
    private void checkYearDeviation() {
        final String message = "YearDeviation setting is not valid!";
        final int yearDeviation = config.getInt("year_deviation");

        checkArgument(yearDeviation > 0, message);
    }

    /**
     * Checks the timeout int
     *
     * @throws IllegalArgumentException If not valid
     */
    private void checkTimeout() {
        final String message = "TimeOut setting is not valid!";
        final int timeout = config.getInt("timeout");

        checkArgument(timeout >= 1000, message);
    }

    /**
     * Checks the query_format string
     *
     * @throws IllegalArgumentException If not valid
     */
    private void checkQueryFormat() {
        final String message = "QueryFormat setting is not valid!";
        final String queryFormat = config.getString("query_format");

        try {
            MovieFinder.Type.valueOf(queryFormat);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(message);
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
        final String message = "Auth setting is not valid!";
        final String auth = config.getString("auth");

        checkArgument(auth.length() > 10, message);
    }

    /**
     * Checks the list string
     *
     * @throws IllegalArgumentException If not valid
     */
    private void checkList() {
        final String message = "List setting is not valid!";
        final String list = config.getString("list");
        final String mode = config.getString("mode");

        if ("".equals(list)) {
            MovieHandler.Type commandType = MovieHandler.Type.valueOf(mode);

            if (commandType.equals(MovieHandler.Type.COMBINED)
                    || commandType.equals(MovieHandler.Type.ADD_TO_WATCHLIST)
                    ) {
                throw new IllegalArgumentException(message + " Set to null, but required for current mode setting");
            }
        } else {
            checkArgument(list.startsWith("ls"), message);
            checkArgument(list.length() >= 3, message);
        }
    }

    /**
     * Checks the mode string
     *
     * @throws IllegalArgumentException If not valid
     */
    private void checkMode() {
        final String message = "Mode setting is not valid!";
        final String mode = config.getString("mode");

        try {
            MovieHandler.Type.valueOf(mode);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(message);
        }
    }
}

package org.f0w.k2i.core.utils;

import com.google.common.base.Strings;
import com.typesafe.config.Config;
import org.f0w.k2i.core.comparators.TitleComparatorType;
import org.f0w.k2i.core.exchange.finder.MovieFinderType;
import org.f0w.k2i.core.handler.MovieHandlerType;

import static com.google.common.base.Preconditions.*;

public class ConfigValidator {
    private ConfigValidator() {
        throw new UnsupportedOperationException();
    }

    public static Config checkValid(Config config) {
        checkNotNull(config);

        checkAuth(config.getString("auth"));
        checkMode(config.getString("mode"));
        checkList(config.getString("list"), config.getString("mode"));

        checkQueryFormat(config.getString("query_format"));
        checkComparator(config.getString("comparator"));

        checkUserAgent(config.getString("user_agent"));
        checkYearDeviation(config.getInt("year_deviation"));
        checkTimeout(config.getInt("timeout"));

        return config;
    }

    private static void checkUserAgent(final String userAgent) {
        final String message = "UserAgent setting is not valid!";

        if (Strings.isNullOrEmpty(userAgent)) {
            throw new IllegalArgumentException(message);
        }
    }

    private static void checkYearDeviation(final Integer yearDeviation) {
        final String message = "YearDeviation setting is not valid!";

        checkArgument(yearDeviation > 0, message);
    }

    private static void checkTimeout(final Integer timeout) {
        final String message = "TimeOut setting is not valid!";

        checkArgument(timeout >= 1000, message);
    }

    private static void checkQueryFormat(final String queryFormat) {
        final String message = "QueryFormat setting is not valid!";

        try {
            MovieFinderType.valueOf(queryFormat);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(message);
        }
    }

    private static void checkComparator(final String comparator) {
        final String message = "Comparator setting is not valid!";

        try {
            TitleComparatorType.valueOf(comparator);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(message);
        }
    }

    private static void checkAuth(final String auth) {
        final String message = "Auth setting is not valid!";

        checkArgument(auth.length() > 10, message);
    }

    private static void checkList(final String list, final String mode) {
        final String message = "List setting is not valid!";

        if ("".equals(list)) {
            MovieHandlerType movieHandlerType = MovieHandlerType.valueOf(mode);

            if (movieHandlerType.equals(MovieHandlerType.COMBINED)
                || movieHandlerType.equals(MovieHandlerType.ADD_TO_WATCHLIST)
            ) {
                throw new IllegalArgumentException(message + " Set to null, but required for current mode setting");
            }
        } else {
            checkArgument(list.startsWith("ls"), message);
            checkArgument(list.length() >= 3, message);
        }
    }

    private static void checkMode(final String mode) {
        final String message = "Mode setting is not valid!";

        try {
             MovieHandlerType.valueOf(mode);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(message);
        }
    }
}

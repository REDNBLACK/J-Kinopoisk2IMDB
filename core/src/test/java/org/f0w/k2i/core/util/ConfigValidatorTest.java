package org.f0w.k2i.core.util;

import ch.qos.logback.classic.Level;
import com.google.common.collect.ImmutableMap;
import com.typesafe.config.ConfigException;
import org.apache.commons.lang3.StringUtils;
import org.f0w.k2i.core.comparator.MovieComparator;
import org.f0w.k2i.core.exchange.finder.MovieFinder;
import org.f0w.k2i.core.handler.MovieHandler;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static com.typesafe.config.ConfigFactory.parseMap;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.f0w.k2i.core.util.ConfigValidator.checkValid;

public class ConfigValidatorTest {
    private static final Map<String, Object> ORIGINAL_CONFIG_MAP = new ImmutableMap.Builder<String, Object>()
            .put("user_agent", "Mozilla/5.0 (Windows NT 6.3; WOW64)")
            .put("year_deviation", 1)
            .put("log_level", Level.INFO.toString())
            .put("timeout", 3000)
            .put("query_format", MovieFinder.Type.MIXED.toString())
            .put("comparators", Arrays.asList(
                    MovieComparator.Type.YEAR_DEVIATION.toString(),
                    MovieComparator.Type.TITLE_SMART.toString()
            ))
            .put("mode", MovieHandler.Type.COMBINED.toString())
            .put("auth", "BCYnIhDSKm7sIoiawZ6TVKs5htuaGRHpT")
            .put("list", "ls032387067")
            .put("db", new ImmutableMap.Builder<String, Object>()
                    .put("driver", "org.h2.Driver")
                    .put("url", "jdbc:h2:~/K2IDB/db/K2IDB;TRACE_LEVEL_FILE=4")
                    .put("password", "root")
                    .put("user", "root")
                    .put("additional", ImmutableMap.of("hibernate.hbm2ddl.auto", "update"))
                    .build()
            )
            .build();

    private Map<String, Object> configMap;

    @Before
    public void setUp() throws Exception {
        configMap = new HashMap<>(ORIGINAL_CONFIG_MAP);
    }

    @Test
    public void checkValidLogLevel() throws Exception {
        configMap.remove("log_level");

        assertThatThrownBy(() -> checkValid(parseMap(configMap)))
                .isInstanceOf(ConfigException.class);
    }

    @Test
    public void checkValidUserAgent() throws Exception {
        checkValid(parseMap(configMap));

        configMap.replace("user_agent", "");
        assertThatThrownBy(() -> checkValid(parseMap(configMap)))
                .isInstanceOf(ConfigException.class);

        configMap.remove("user_agent");
        assertThatThrownBy(() -> checkValid(parseMap(configMap)))
                .isInstanceOf(ConfigException.class);
    }

    @Test
    public void checkValidYearDeviation() throws Exception {
        checkValid(parseMap(configMap));

        configMap.replace("year_deviation", 0);
        assertThatThrownBy(() -> checkValid(parseMap(configMap)))
                .isInstanceOf(ConfigException.class);

        configMap.remove("year_deviation");
        assertThatThrownBy(() -> checkValid(parseMap(configMap)))
                .isInstanceOf(ConfigException.class);
    }

    @Test
    public void checkValidTimeout() throws Exception {
        checkValid(parseMap(configMap));

        configMap.replace("timeout", 999);
        assertThatThrownBy(() -> checkValid(parseMap(configMap)))
                .isInstanceOf(ConfigException.class);

        configMap.remove("timeout");
        assertThatThrownBy(() -> checkValid(parseMap(configMap)))
                .isInstanceOf(ConfigException.class);
    }

    @Test
    public void checkValidQueryFormat() throws Exception {
        checkValid(parseMap(configMap));

        configMap.replace("query_format", MovieFinder.Type.XML.toString());
        checkValid(parseMap(configMap));

        configMap.replace("query_format", "not existing query_format");
        assertThatThrownBy(() -> checkValid(parseMap(configMap)))
                .isInstanceOf(ConfigException.class);

        configMap.remove("query_format");
        assertThatThrownBy(() -> checkValid(parseMap(configMap)))
                .isInstanceOf(ConfigException.class);
    }

    @Test
    public void checkValidAuth() throws Exception {
        checkValid(parseMap(configMap));

        configMap.replace("auth", StringUtils.repeat("S", 10));
        assertThatThrownBy(() -> checkValid(parseMap(configMap)))
                .isInstanceOf(ConfigException.class);

        configMap.remove("auth");
        assertThatThrownBy(() -> checkValid(parseMap(configMap)))
                .isInstanceOf(ConfigException.class);
    }

    @Test
    public void checkValidList() throws Exception {
        checkValid(parseMap(configMap));

        configMap.replace("list", "");
        assertThatThrownBy(() -> checkValid(parseMap(configMap)))
                .isInstanceOf(ConfigException.class);

        configMap.replace("list", "ls");
        assertThatThrownBy(() -> checkValid(parseMap(configMap)))
                .isInstanceOf(ConfigException.class);

        configMap.replace("list", "0123456");
        assertThatThrownBy(() -> checkValid(parseMap(configMap)))
                .isInstanceOf(ConfigException.class);

        configMap.replace("list", "");
        configMap.replace("mode", MovieHandler.Type.SET_RATING.toString());
        checkValid(parseMap(configMap));

        configMap.remove("list");
        assertThatThrownBy(() -> checkValid(parseMap(configMap)))
                .isInstanceOf(ConfigException.class);
    }

    @Test
    public void checkValidMode() throws Exception {
        checkValid(parseMap(configMap));

        configMap.replace("mode", MovieHandler.Type.SET_RATING.toString());
        checkValid(parseMap(configMap));

        configMap.replace("mode", "not existing mode");
        assertThatThrownBy(() -> checkValid(parseMap(configMap)))
                .isInstanceOf(ConfigException.class);

        configMap.remove("mode");
        assertThatThrownBy(() -> checkValid(parseMap(configMap)))
                .isInstanceOf(ConfigException.class);
    }

    @Test
    public void checkValidComparators() throws Exception {
        checkValid(parseMap(configMap));

        configMap.replace("comparators", Arrays.asList(
                MovieComparator.Type.YEAR_EQUALS.toString(),
                MovieComparator.Type.TITLE_EQUALS.toString()
        ));
        checkValid(parseMap(configMap));

        configMap.replace("comparators", Collections.singletonList("not existing comparator"));
        assertThatThrownBy(() -> checkValid(parseMap(configMap)))
                .isInstanceOf(ConfigException.class);

        configMap.remove("comparators");
        assertThatThrownBy(() -> checkValid(parseMap(configMap)))
                .isInstanceOf(ConfigException.class);
    }

    @Test
    public void checkValidDataBase() throws Exception {
        checkValid(parseMap(configMap));

        configMap.remove("db");
        assertThatThrownBy(() -> checkValid(parseMap(configMap)))
                .isInstanceOf(ConfigException.class);
    }
}
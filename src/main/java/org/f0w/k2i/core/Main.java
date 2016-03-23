package org.f0w.k2i.core;

import com.google.common.collect.ImmutableMap;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.f0w.k2i.core.comparators.TitleComparatorType;
import org.f0w.k2i.core.exchange.finder.MovieFinderType;
import org.f0w.k2i.core.handler.MovieHandlerType;

import java.util.Map;

public class Main {
    public static void main(String[] args) {
        Map<String, Object> map = new ImmutableMap.Builder<String, Object>()
                .put("user_agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.120 Safari/537.36")
                .put("year_deviation", 1)
                .put("timeout", 3)
                .put("query_format", MovieFinderType.JSON.toString())
                .put("comparator", TitleComparatorType.SMART.toString())
                .put("mode", MovieHandlerType.COMBINED.toString())
                .put("auth", "BCYnIhDSKm7sIoiawZ6TVKs5htuaGRHpTzwNXSlJ--JlTfh9R68UfgVY0jMJFosYtd_exTGLGOdWhcAYhc3MFTNik6_8pfUeeqPaVG4XbCb43W3BXzPQOAajFGPWusxhkXZNRLBoAZ9cApmLytjy4tnfBMceF0eMIl8mUtdqT-V-KSObD5dKepBNYU81xFHAooVC80VhPr08Q1UxOlM6oCEYSd10mMHsaFY4zMXL2MNXuxE")
                .put("list", "ls013136398")
                .build();

        Config config = ConfigFactory.parseMap(map);

        Client client = new Client(config);
        client.init();
        client.run();
    }
}

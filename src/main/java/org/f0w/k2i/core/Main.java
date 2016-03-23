package org.f0w.k2i.core;

import com.google.common.collect.ImmutableMap;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.f0w.k2i.core.exchange.finder.MovieFinderType;
import org.f0w.k2i.core.handler.MovieHandlerType;

import java.io.File;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws Exception {
        Map<String, Object> map = new ImmutableMap.Builder<String, Object>()
                .put("query_format", MovieFinderType.JSON.toString())
                .put("mode", MovieHandlerType.SET_RATING.toString())
                .put("auth", "BCYnIhDSKm7sIoiawZ6TVKs5htuaGRHpTzwNXSlJ--JlTfh9R68UfgVY0jMJFosYtd_exTGLGOdWhcAYhc3MFTNik6_8pfUeeqPaVG4XbCb43W3BXzPQOAajFGPWusxhkXZNRLBoAZ9cApmLytjy4tnfBMceF0eMIl8mUtdqT-V-KSObD5dKepBNYU81xFHAooVC80VhPr08Q1UxOlM6oCEYSd10mMHsaFY4zMXL2MNXuxE")
                .put("list", "ls032387067")
                .build();

        Config config = ConfigFactory.parseMap(map);

        File file = new File("/Users/RB/Downloads/test-kp.xls");

        Client client = new Client(file, config);
        client.run();
    }
}

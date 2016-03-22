package org.f0w.k2i.core;

import com.google.common.collect.ImmutableMap;
import com.google.inject.AbstractModule;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.f0w.k2i.core.comparators.EqualityComparatorType;
import org.f0w.k2i.core.exchange.finder.MovieFinderType;
import org.f0w.k2i.core.handler.MovieHandlerType;
import org.f0w.k2i.core.model.repository.*;

import javax.persistence.EntityManager;
import java.util.Map;

public class ServiceProvider extends AbstractModule {
    @Override
    protected void configure() {
        Map<String, Object> map = new ImmutableMap.Builder<String, Object>()
                .put("user_agent", "Mozilla/5.0 (Windows NT 6.3; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/37.0.2062.120 Safari/537.36")
                .put("query_format", MovieFinderType.JSON)
                .put("comparator", EqualityComparatorType.SMART)
                .put("mode", MovieHandlerType.EVERYTHING)
                .put("year_deviation", 1)
                .put("auth", "BCYnIhDSKm7sIoiawZ6TVKs5htuaGRHpTzwNXSlJ--JlTfh9R68UfgVY0jMJFosYtd_exTGLGOdWhcAYhc3MFTNik6_8pfUeeqPaVG4XbCb43W3BXzPQOAajFGPWusxhkXZNRLBoAZ9cApmLytjy4tnfBMceF0eMIl8mUtdqT-V-KSObD5dKepBNYU81xFHAooVC80VhPr08Q1UxOlM6oCEYSd10mMHsaFY4zMXL2MNXuxE")
                .put("list", "ls013136398")
                .build();
//
        Config config = ConfigFactory.parseMap(map);

        bind(Config.class).toInstance(config);
        bind(ImportProgressRepository.class).to(ImportProgressRepositoryImpl.class);
        bind(KinopoiskFileRepository.class).to(KinopoiskFileRepositoryImpl.class);
        bind(MovieRepository.class).to(MovieRepositoryImpl.class);
    }
}

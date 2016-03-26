package org.f0w.k2i.core;

import com.google.common.collect.ImmutableMap;
import com.google.common.eventbus.Subscribe;
import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import org.f0w.k2i.core.command.MovieCommand;
import org.f0w.k2i.core.event.ImportListInitializedEvent;
import org.f0w.k2i.core.event.ImportListProgressAdvancedEvent;
import org.f0w.k2i.core.exchange.finder.MovieFinder;

import java.io.File;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class Main {
    public static void main(String[] args) throws Exception {
        Map<String, Object> map = new ImmutableMap.Builder<String, Object>()
                .put("query_format", MovieFinder.Type.MIXED.toString())
                .put("mode", MovieCommand.Type.ADD_TO_WATCHLIST.toString())
                .put("auth", "BCYnIhDSKm7sIoiawZ6TVKs5htuaGRHpTzwNXSlJ--JlTfh9R68UfgVY0jMJFosYtd_exTGLGOdWhcAYhc3MFTNik6_8pfUeeqPaVG4XbCb43W3BXzPQOAajFGPWusxhkXZNRLBoAZ9cApmLytjy4tnfBMceF0eMIl8mUtdqT-V-KSObD5dKepBNYU81xFHAooVC80VhPr08Q1UxOlM6oCEYSd10mMHsaFY4zMXL2MNXuxE")
                .put("list", "ls032387067")
                .build();

        Config config = ConfigFactory.parseMap(map);

        File file = new File("/Users/RB/Downloads/test-kp.xls");

        final AtomicInteger max = new AtomicInteger(0);
        final AtomicInteger current = new AtomicInteger(0);

        Client client = new Client(file, config);
        client.registerListener(new Object() {
            @Subscribe
            public void handleProgressSetUpEvent(ImportListInitializedEvent event) {
                max.set(event.listSize);
            }
        });
        client.registerListener(new Object() {
            @Subscribe
            public void handleProgressAdvanceEvent(ImportListProgressAdvancedEvent event) {
                int maximum = max.get();
                int cur = current.incrementAndGet();

                System.out.println((cur * 100 / maximum) + "% (" + cur + "/" + maximum + ")");
            }
        });
        client.run();
    }
}

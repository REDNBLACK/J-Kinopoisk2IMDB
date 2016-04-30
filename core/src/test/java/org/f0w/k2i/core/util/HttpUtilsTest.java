package org.f0w.k2i.core.util;

import com.google.common.collect.ImmutableMap;
import org.f0w.k2i.TestHelper;
import org.junit.Test;

import java.net.URL;
import java.util.Collections;
import java.util.Map;

import static org.f0w.k2i.core.util.HttpUtils.buildURL;
import static org.f0w.k2i.core.util.HttpUtils.isReachable;
import static org.junit.Assert.*;

public class HttpUtilsTest {
    @Test
    public void testPrivateConstructor() throws Exception {
        assertTrue(TestHelper.isConstructorPrivate(HttpUtils.class));

        TestHelper.callPrivateConstructor(HttpUtils.class);
    }

    @Test
    public void testExistingAddressIsReachable() throws Exception {
        final String host = "www.google.com";
        final int port = 443;
        final int timeout = 10000;

        assertTrue(isReachable(host, port, timeout));
    }

    @Test
    public void testNotExistingAddressIsUnreacheble() throws Exception {
        final String host = "sadasdsadsd";
        final int port = 80;
        final int timeout = 10000;

        assertFalse(isReachable(host, port, timeout));
    }

    @Test
    public void testBuildOfValidURLWithoutQuestionMarkOnEnd() throws Exception {
        final String hostName = "http://imdb.com";
        final Map<String, String> queryParams = new ImmutableMap.Builder<String, String>()
                .put("q", "test")
                .put("ref", "default")
                .put("json", "true")
                .build();

        final URL expected = new URL("http://imdb.com?q=test&ref=default&json=true");

        assertEquals(expected, buildURL(hostName, queryParams));
    }

    @Test
    public void testBuildOfValidURLWithQuestionMarkOnEnd() throws Exception {
        final String hostName = "http://imdb.com?";
        final Map<String, String> queryParams = new ImmutableMap.Builder<String, String>()
                .put("q", "test")
                .put("ref", "default")
                .put("json", "true")
                .build();

        final URL expected = new URL("http://imdb.com?q=test&ref=default&json=true");

        assertEquals(expected, buildURL(hostName, queryParams));
    }

    @Test
    public void testBuildOfValidUrlWithEmptyQueryParams() throws Exception {
        final String hostName = "http://imdb.com";
        final Map<String, String> queryParams = Collections.emptyMap();

        final URL expected = new URL("http://imdb.com?");

        assertEquals(expected, buildURL(hostName, queryParams));
    }

    @Test
    public void testBuildOfValidURLWithEscapedCharactersInQuery() throws Exception {
        final String hostName = "http://imdb.com/";
        final Map<String, String> queryParams = new ImmutableMap.Builder<String, String>()
                .put("q", "русский текст")
                .put("ref", "&f?$")
                .put("json", "true")
                .build();

        final URL expected = new URL("http://imdb.com/?q=%D1%80%D1%83%D1%81%D1%81%D0%BA%D0%B8%D0%B9"
                + "+%D1%82%D0%B5%D0%BA%D1%81%D1%82"
                + "&ref=%26f%3F%24&json=true"
        );

        assertEquals(expected, buildURL(hostName, queryParams));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildOfInvalidUrl() throws Exception {
        buildURL("sdsdsd", Collections.emptyMap());
    }
}
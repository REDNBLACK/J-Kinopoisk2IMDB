package org.f0w.k2i.core.util;

import com.google.common.collect.ImmutableMap;
import lombok.val;
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
    public void isConstructorPrivate() throws Exception {
        assertTrue(TestHelper.isConstructorPrivate(HttpUtils.class));

        TestHelper.callPrivateConstructor(HttpUtils.class);
    }

    @Test
    public void existingAddressIsReachable() throws Exception {
        val host = "www.google.com";
        val port = 443;
        val timeout = 10000;

        assertTrue(isReachable(host, port, timeout));
    }

    @Test
    public void notExistingAddressIsUnreacheble() throws Exception {
        val host = "sadasdsadsd";
        val port = 80;
        val timeout = 10000;

        assertFalse(isReachable(host, port, timeout));
    }

    @Test
    public void buildOfValidURLWithoutQuestionMarkOnEnd() throws Exception {
        val hostName = "http://imdb.com";
        val queryParams = new ImmutableMap.Builder<String, String>()
                .put("q", "test")
                .put("ref", "default")
                .put("json", "true")
                .build();

        assertEquals(new URL("http://imdb.com?q=test&ref=default&json=true"), buildURL(hostName, queryParams));
    }

    @Test
    public void buildOfValidURLWithQuestionMarkOnEnd() throws Exception {
        val hostName = "http://imdb.com?";
        val queryParams = new ImmutableMap.Builder<String, String>()
                .put("q", "test")
                .put("ref", "default")
                .put("json", "true")
                .build();

        assertEquals(new URL("http://imdb.com?q=test&ref=default&json=true"), buildURL(hostName, queryParams));
    }

    @Test
    public void buildOfValidUrlWithEmptyQueryParams() throws Exception {
        val hostName = "http://imdb.com";
        Map<String, String> queryParams = Collections.emptyMap();

        assertEquals(new URL("http://imdb.com?"), buildURL(hostName, queryParams));
    }

    @Test
    public void buildOfValidURLWithEscapedCharactersInQuery() throws Exception {
        val hostName = "http://imdb.com/";
        val queryParams = new ImmutableMap.Builder<String, String>()
                .put("q", "русский текст")
                .put("ref", "&f?$")
                .put("json", "true")
                .build();

        val expected = new URL("http://imdb.com/?q=%D1%80%D1%83%D1%81%D1%81%D0%BA%D0%B8%D0%B9"
                + "+%D1%82%D0%B5%D0%BA%D1%81%D1%82"
                + "&ref=%26f%3F%24&json=true"
        );

        assertEquals(expected, buildURL(hostName, queryParams));
    }

    @Test(expected = IllegalArgumentException.class)
    public void buildOfInvalidUrl() throws Exception {
        buildURL("sdsdsd", Collections.emptyMap());
    }
}
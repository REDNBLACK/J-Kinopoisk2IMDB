package org.f0w.k2i.core.utils;

import com.google.common.base.Joiner;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class RequestHelper {
    public static URL makeURL(final String url, Map<String, String> queryParams) {
        try {
            return new URL(url + Joiner.on("&").withKeyValueSeparator("=").join(queryParams));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }
}

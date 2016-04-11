package org.f0w.k2i.core.util;

import com.google.common.base.Joiner;
import com.google.common.net.UrlEscapers;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Map;
import java.util.stream.Collectors;

public final class HttpUtils {
    private HttpUtils() {
        throw new UnsupportedOperationException();
    }

    public static boolean isReachable(String hostName, int port, int timeout) {
        try (Socket soc = new Socket()) {
            soc.connect(new InetSocketAddress(hostName, port), timeout);
            return true;
        } catch (IOException ignore) {
            return false;
        }
    }

    public static String buildURL(final String url, Map<String, String> queryData) {
        Map<String, String> escapedQueryData = queryData.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> UrlEscapers.urlFormParameterEscaper().escape(e.getValue())
                ));

        return url + Joiner.on("&").withKeyValueSeparator("=").join(escapedQueryData);
    }
}

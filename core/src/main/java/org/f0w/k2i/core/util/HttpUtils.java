package org.f0w.k2i.core.util;

import com.google.common.base.Joiner;
import com.google.common.net.UrlEscapers;
import lombok.val;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Class for reusable methods and helpers working with http, queries, etc.
 */
public final class HttpUtils {
    private HttpUtils() {
    }

    /**
     * Checks that given hostname is reacheble in certain timeout.
     * Basically working as {@link java.net.InetAddress#isReachable(int)}, but working on all platforms
     *
     * @param hostName Hostname
     * @param port     Port
     * @param timeout  Timeout in ms
     * @return Reachable or not
     */
    public static boolean isReachable(final String hostName, int port, int timeout) {
        try (Socket socket = new Socket()) {
            socket.connect(new InetSocketAddress(hostName, port), timeout);
            return true;
        } catch (IOException ignore) {
            return false;
        }
    }

    /**
     * Builds and escapes URL, using host and query
     *
     * @param hostName Hostname component of URL
     * @param query    Query component of URL
     * @return URL
     * @throws IllegalArgumentException If URL is invalid
     */
    public static URL buildURL(final String hostName, final Map<String, String> query) {
        val escapedQuery = query.entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> UrlEscapers.urlFormParameterEscaper().escape(e.getValue())
                ));

        try {
            return new URL(
                    hostName
                            + (!hostName.endsWith("?") ? "?" : "")
                            + Joiner.on("&").withKeyValueSeparator("=").join(escapedQuery)
            );
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}

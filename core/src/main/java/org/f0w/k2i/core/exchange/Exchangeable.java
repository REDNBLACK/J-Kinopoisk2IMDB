package org.f0w.k2i.core.exchange;

import org.jsoup.Connection;

import java.io.IOException;

/**
 * Generic interface for classes implementing send request - get response - handle response chain.
 *
 * @param <IN>  Input
 * @param <OUT> Output
 */
public interface Exchangeable<IN, OUT> {
    void sendRequest(IN param) throws IOException;

    /**
     * @return Raw response from server
     */
    Connection.Response getRawResponse();

    OUT getProcessedResponse();
}

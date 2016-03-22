package org.f0w.k2i.core.exchange;

import org.jsoup.Connection;

import java.io.IOException;

public interface Exchangeable<IN, OUT> {
    void sendRequest(IN param) throws IOException;

    Connection.Response getRawResponse();

    OUT getProcessedResponse();
}

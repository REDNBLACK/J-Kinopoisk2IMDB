package org.f0w.k2i.core.exchange;

import org.f0w.k2i.core.net.Response;

public interface Exchangeable<IN, OUT> {
    void sendRequest(IN param);

    Response getRawResponse();

    OUT getProcessedResponse();
}

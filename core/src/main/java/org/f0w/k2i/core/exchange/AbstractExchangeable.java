package org.f0w.k2i.core.exchange;

import org.jsoup.Connection;

import java.util.Optional;


public abstract class AbstractExchangeable<IN, OUT> implements Exchangeable<IN, OUT> {
    protected Connection.Response response;

    /**
     * {@inheritDoc}
     */
    @Override
    public void setResponse(Connection.Response response) {
        this.response = response;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Connection.Response> getRawResponse() {
        return Optional.ofNullable(response);
    }

    /**
     * Returns body or response or empty string if response is null.
     *
     * @return Response body
     */
    protected String getResponseBody() {
        return getRawResponse().map(Connection.Response::body).orElse("");
    }
}

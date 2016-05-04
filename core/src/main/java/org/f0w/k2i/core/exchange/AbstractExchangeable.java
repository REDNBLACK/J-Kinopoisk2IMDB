package org.f0w.k2i.core.exchange;

import lombok.NonNull;
import lombok.Setter;
import org.jsoup.Connection;

import java.util.Optional;


public abstract class AbstractExchangeable<IN, OUT> implements Exchangeable<IN, OUT> {
    @Setter
    @NonNull
    protected Connection.Response response;

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

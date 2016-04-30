package org.f0w.k2i.core.exchange;

import org.jsoup.Connection;


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
    public Connection.Response getRawResponse() {
        return response;
    }
}

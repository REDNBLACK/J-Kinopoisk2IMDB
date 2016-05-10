package org.f0w.k2i.core.exchange.processor;

import org.jsoup.Connection;

/**
 * Interface for processors of response object.
 * @param <OUT> Type of returned processed response
 */
public interface ResponseProcessor<OUT> {
    /**
     * @param response Response to process
     * @return Processed response
     */
    OUT process(Connection.Response response);
}

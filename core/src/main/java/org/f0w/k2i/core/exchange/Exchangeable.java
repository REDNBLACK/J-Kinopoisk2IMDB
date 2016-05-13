package org.f0w.k2i.core.exchange;

import lombok.NonNull;

import java.io.IOException;

/**
 * Generic interface for classes implementing send request - get response - handle response chain.
 *
 * @param <IN>  Input
 * @param <OUT> Output
 */
public interface Exchangeable<IN, OUT> {
    ExchangeObject<OUT> prepare(@NonNull IN param) throws IOException;
}

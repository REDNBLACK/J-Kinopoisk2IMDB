package org.f0w.k2i.core.exchange;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.f0w.k2i.core.exchange.processor.ResponseProcessor;
import org.f0w.k2i.core.util.exception.KinopoiskToIMDBException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

import static org.f0w.k2i.core.util.exception.ExceptionUtils.rethrowSupplier;
import static org.f0w.k2i.core.util.exception.ExceptionUtils.uncheck;

/**
 * Interface for object which performs lazy execution of already prepared request
 * @param <OUT> Type of processed response
 */
@Slf4j
public final class ExchangeObject<OUT> {
    public static final Connection.Request EMPTY_REQUEST = new EmptyRequest();

    @NonNull
    @Getter
    private final Connection.Request request;

    @NonNull
    @Setter
    private Connection.Response response;

    @NonNull
    private final Supplier<Connection.Response> responseSupplier;

    @NonNull
    @Getter
    private final ResponseProcessor<OUT> responseProcessor;

    public ExchangeObject(Connection.Request request, ResponseProcessor<OUT> responseProcessor) throws IOException {
        this.request = request;
        this.responseProcessor = responseProcessor;
        this.responseSupplier = rethrowSupplier(() -> Jsoup.connect(request.url().toString())
                .request(request)
                .execute()
        );
    }

    public ExchangeObject(ResponseProcessor<OUT> responseProcessor) throws IOException {
        this(EMPTY_REQUEST, responseProcessor);
    }

    public Connection.Response getResponse() {
        if (response == null && !request.equals(EMPTY_REQUEST)) {
            log.debug(
                    "Sending request, to url: {}, with headers: {}", getRequest().url(), getRequest().headers()
            );
            response = responseSupplier.get();
            log.debug("Got response, status code: {}, headers: {}", response.statusCode(), response.headers());
        }

        return response;
    }

    public OUT getProcessedResponse() throws IOException {
        return responseProcessor.process(getResponse());
    }

    private static final class EmptyRequest implements Connection.Request {
        @Override
        public int timeout() {
            return 0;
        }

        @Override
        public Connection.Request timeout(int millis) {
            return null;
        }

        @Override
        public int maxBodySize() {
            return 0;
        }

        @Override
        public Connection.Request maxBodySize(int bytes) {
            return null;
        }

        @Override
        public boolean followRedirects() {
            return false;
        }

        @Override
        public Connection.Request followRedirects(boolean followRedirects) {
            return null;
        }

        @Override
        public boolean ignoreHttpErrors() {
            return false;
        }

        @Override
        public Connection.Request ignoreHttpErrors(boolean ignoreHttpErrors) {
            return null;
        }

        @Override
        public boolean ignoreContentType() {
            return false;
        }

        @Override
        public Connection.Request ignoreContentType(boolean ignoreContentType) {
            return null;
        }

        @Override
        public boolean validateTLSCertificates() {
            return false;
        }

        @Override
        public void validateTLSCertificates(boolean value) {

        }

        @Override
        public Connection.Request data(Connection.KeyVal keyval) {
            return null;
        }

        @Override
        public Collection<Connection.KeyVal> data() {
            return null;
        }

        @Override
        public Connection.Request parser(Parser parser) {
            return null;
        }

        @Override
        public Parser parser() {
            return null;
        }

        @Override
        public Connection.Request postDataCharset(String charset) {
            return null;
        }

        @Override
        public String postDataCharset() {
            return null;
        }

        @Override
        public URL url() {
            return null;
        }

        @Override
        public Connection.Request url(URL url) {
            return null;
        }

        @Override
        public Connection.Method method() {
            return null;
        }

        @Override
        public Connection.Request method(Connection.Method method) {
            return null;
        }

        @Override
        public String header(String name) {
            return null;
        }

        @Override
        public Connection.Request header(String name, String value) {
            return null;
        }

        @Override
        public boolean hasHeader(String name) {
            return false;
        }

        @Override
        public boolean hasHeaderWithValue(String name, String value) {
            return false;
        }

        @Override
        public Connection.Request removeHeader(String name) {
            return null;
        }

        @Override
        public Map<String, String> headers() {
            return null;
        }

        @Override
        public String cookie(String name) {
            return null;
        }

        @Override
        public Connection.Request cookie(String name, String value) {
            return null;
        }

        @Override
        public boolean hasCookie(String name) {
            return false;
        }

        @Override
        public Connection.Request removeCookie(String name) {
            return null;
        }

        @Override
        public Map<String, String> cookies() {
            return null;
        }
    }
}

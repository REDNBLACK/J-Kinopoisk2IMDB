package org.f0w.k2i.core.net;

import com.google.common.io.CharStreams;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class BasicResponse implements Response {
    private InputStream stream;
    private int statusCode;

    public BasicResponse(InputStream stream, int statusCode) {
        this.stream = stream;
        this.statusCode = statusCode;
    }

    @Override
    public Response getResponse() {
        return this;
    }

    @Override
    public int getStatusCode() {
        return statusCode;
    }

    @Override
    public String toString() {
        try (InputStreamReader reader = new InputStreamReader(stream, StandardCharsets.UTF_8)) {
            return CharStreams.toString(reader);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

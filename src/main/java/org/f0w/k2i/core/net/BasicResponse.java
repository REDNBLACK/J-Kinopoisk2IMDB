package org.f0w.k2i.core.net;

import com.google.common.io.CharStreams;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class BasicResponse implements Response {
    private InputStream stream;
    private int statusCode;
    private Map<String, String> info;

    public BasicResponse(InputStream stream, int statusCode, Map<String, String> info) {
        this.stream = stream;
        this.statusCode = statusCode;
        this.info = info;
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
    public Map<String, String> getInfo() {
        return info;
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

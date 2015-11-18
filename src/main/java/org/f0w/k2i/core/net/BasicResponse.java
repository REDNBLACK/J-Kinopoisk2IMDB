package org.f0w.k2i.core.net;

import com.google.common.io.CharStreams;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class BasicResponse implements Response {
    private InputStream stream;

    public BasicResponse(InputStream stream) {
        this.stream = stream;
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

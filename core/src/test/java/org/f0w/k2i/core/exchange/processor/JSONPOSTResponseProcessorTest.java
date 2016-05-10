package org.f0w.k2i.core.exchange.processor;

import org.f0w.k2i.ResponseMock;
import org.jsoup.Connection;
import org.junit.Test;

import java.net.HttpURLConnection;

import static org.junit.Assert.*;

public class JSONPOSTResponseProcessorTest {
    private JSONPOSTResponseProcessor processor = new JSONPOSTResponseProcessor();

    @Test
    public void processEmptyResponse() throws Exception {
        Connection.Response response = new ResponseMock("");

        assertTrue(0 == processor.process(response));
    }

    @Test
    public void processValidResponse() throws Exception {
        Connection.Response response = new ResponseMock("{\"status\":200}");

        assertTrue(HttpURLConnection.HTTP_OK == processor.process(response));
    }
}
package org.f0w.k2i.core.net;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HttpClient {
    private HttpURLConnection client;

    private Map<String, String> info = new HashMap<>();

    public HttpClient() {}

    public HttpClient(Request request) {
        setRequest(request);
    }

    public HttpClient setRequest(Request request) {
        try {
            client = (HttpURLConnection) new URL(request.getURL()).openConnection();

            if (request.getUserAgent() != null) {
                client.setRequestProperty("User-Agent", request.getUserAgent());
            }

            if (request.getCookies() != null) {
                client.setRequestProperty("Cookie", request.getCookies());
            }

            client.setRequestMethod(request.getMethod().toString());

            if (request.getMethod().equals(HttpMethod.POST) && request.getPostData() != null) {
                client.setDoOutput(true);
                setRequestInfo();
                client.connect();

                try (DataOutputStream stream = new DataOutputStream(client.getOutputStream())) {
                    stream.writeBytes(request.getPostData());
                    stream.flush();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return this;
    }

    public HttpClient sendRequest() {
        try {
            client.connect();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return this;
    }

    public HttpClient sendRequest(Request request) {
        setRequest(request);

        return sendRequest();
    }

    public Response getResponse() {
        try {
            client.connect();
            setResponseInfo();

            return new BasicResponse(client.getInputStream(), client.getResponseCode(), info);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            client.disconnect();
        }
    }

    private void setRequestInfo() {
        info.put("request_url", String.valueOf(client.getURL()));
        info.put("request_properties", String.valueOf(client.getRequestProperties()));
    }

    private void setResponseInfo() throws IOException {
        info.put("response_headers", String.valueOf(client.getHeaderFields()));
        info.put("response_code", String.valueOf(client.getResponseCode()));
        info.put("response_message", String.valueOf(client.getResponseMessage()));
        info.put("content_encoding", String.valueOf(client.getContentEncoding()));
        info.put("content_length", String.valueOf(client.getContentLengthLong()));
        info.put("content_type", String.valueOf(client.getContentType()));
    }
}

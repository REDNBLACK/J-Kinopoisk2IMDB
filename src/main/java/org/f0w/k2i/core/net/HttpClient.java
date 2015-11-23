package org.f0w.k2i.core.net;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpClient {
    private HttpURLConnection client;

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
                client.connect();

                DataOutputStream stream = new DataOutputStream(client.getOutputStream());
                stream.writeBytes(request.getPostData());
                stream.flush();
                stream.close();
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
            return new BasicResponse(client.getInputStream(), client.getResponseCode());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}

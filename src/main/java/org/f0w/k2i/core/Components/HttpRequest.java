package org.f0w.k2i.core.Components;

import com.google.common.base.Joiner;
import com.google.common.io.CharStreams;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Map;

public class HttpRequest {
    private HttpURLConnection request;

    public void createRequest(URL url) throws IOException {
        this.request = (HttpURLConnection) url.openConnection();
    }

    public void createRequest(final String url, Map<String, String> query) throws IOException {
        createRequest(makeURL(url, query));
    }

    public void createRequest(final String url) throws IOException {
        createRequest(new URL(url));
    }

    public HttpURLConnection getRequest() {
        return request;
    }

    public void setCookies(String cookies) {
        request.setRequestProperty("Cookie", cookies);
    }

    public void setCookies(Map<String, String> cookies) {
        setCookies(Joiner.on(";").withKeyValueSeparator("=").join(cookies));
    }

    public void setUserAgent(String userAgent) {
        request.setRequestProperty("User-Agent", userAgent);
    }

    public void setPOSTData(String postData) {
        try {
            request.setRequestMethod("POST");
            request.setDoOutput(true);

            DataOutputStream stream = new DataOutputStream(request.getOutputStream());
            stream.writeBytes(postData);
            stream.flush();
            stream.close();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setPOSTData(Map<String, String> postData) {
        setPOSTData(Joiner.on("&").withKeyValueSeparator("=").join(postData));
    }

    public String getResponse() throws IOException {
        return CharStreams.toString(new InputStreamReader(request.getInputStream()));
    }

    public int getStatusCode() throws IOException {
        return request.getResponseCode();
    }

    public static URL makeURL(final String url, Map<String, String> query) {
        URL newURL = null;

        try {
            newURL = new URL(url + Joiner.on("&").withKeyValueSeparator("=").join(query));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        return newURL;
    }
}

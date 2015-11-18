package org.f0w.k2i.core.net;

import java.net.URL;
import java.util.Map;

public interface Request {
    Request createRequest(URL url);

    Request createRequest(String url);

    Request createRequest(URL url, Map<String, String> query);

    Request createRequest(URL url, String query);

    Request createRequest(String url, Map<String, String> query);

    Request createRequest(String url, String query);

    void setUserAgent(String userAgent);

    void setCookies(String cookies);

    void setCookies(Map<String, String> cookies);

    void setPOSTData(String postData);

    void setPOSTData(Map<String, String> postData);

    Response getResponse();

    int getStatusCode();

    interface Builder {
        Request build();

        Builder setUrl(String url);

        Builder setUrl(URL url);

        Builder addQueryParam(String queryParam);

        Builder addQueryParam(String key, String value);

        Builder addQueryParams(Map<String, String> queryParams);

        Builder setUserAgent(String userAgent);

        Builder addCookie(String key, String value);

        Builder addCookie(String cookie);

        Builder addCookies(Map<String, String> cookies);

        Builder addPOSTData(String key, String value);

        Builder addPOSTData(String postData);

        Builder addPOSTData(Map<String, String> postData);
    }
}

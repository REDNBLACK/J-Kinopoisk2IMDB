package org.f0w.k2i.core.net;

import java.net.URL;
import java.util.Map;

public interface Request {
    HttpMethod getMethod();

    String getURL();

    String getPostData();

    String getCookies();

    String getUserAgent();

    interface Builder {
        Request build();

        Builder setUrl(String url);

        Builder setUrl(URL url);

        Builder addQueryParam(String queryParam);

        Builder addQueryParam(String key, String value);

        Builder addQueryParams(Map<String, String> queryParams);

        Builder setUserAgent(String userAgent);

        Builder setMethod(String method);

        Builder setMethod(HttpMethod method);

        Builder addCookie(String key, String value);

        Builder addCookie(String cookie);

        Builder addCookies(Map<String, String> cookies);

        Builder addPOSTData(String key, String value);

        Builder addPOSTData(String postData);

        Builder addPOSTData(Map<String, String> postData);
    }
}

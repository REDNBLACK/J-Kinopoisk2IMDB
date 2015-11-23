package org.f0w.k2i.core.net;

import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class HttpRequest implements Request {
    private HttpMethod method;
    private String url;
    private String postData;
    private String cookies;
    private String userAgent;

    public HttpRequest(HttpMethod method, String url, String postData, String cookies, String userAgent) {
        this.method = Preconditions.checkNotNull(method);
        this.url = Preconditions.checkNotNull(url);
        this.postData = postData;
        this.cookies = cookies;
        this.userAgent = userAgent;
    }

    @Override
    public HttpMethod getMethod() {
        return method;
    }

    @Override
    public String getURL() {
        return url;
    }

    @Override
    public String getPostData() {
        return postData;
    }

    @Override
    public String getCookies() {
        return cookies;
    }

    @Override
    public String getUserAgent() {
        return userAgent;
    }

    public static Request.Builder builder() {
        return new Builder();
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("url", url)
                .add("method", method)
                .add("user_agent", userAgent)
                .add("cookies", cookies)
                .add("post_data", postData)
                .toString()
        ;
    }

    public static class Builder implements Request.Builder {
        private String url;
        private HttpMethod method = HttpMethod.GET;
        private String userAgent;

        private List<String> queryParams = new ArrayList<>();
        private List<String> cookies = new ArrayList<>();
        private List<String> postData = new ArrayList<>();

        public Builder() {}

        @Override
        public Request build() {
            return new HttpRequest(
                    method,
                    url + Joiner.on("&").join(queryParams),
                    Joiner.on("&").join(postData),
                    Joiner.on(";").join(cookies),
                    userAgent
            );
        }

        @Override
        public String toString() {
            return MoreObjects.toStringHelper(this)
                    .add("url", url)
                    .add("method", method)
                    .add("user_agent", userAgent)
                    .add("query_params", Joiner.on("&").join(queryParams))
                    .add("cookies", Joiner.on(";").join(cookies))
                    .add("post_data", Joiner.on("&").join(postData))
                    .toString()
            ;
        }

        @Override
        public Request.Builder setUrl(String url) {
            this.url = url;

            return this;
        }

        @Override
        public Request.Builder setUrl(URL url) {
            setUrl(url.toExternalForm());

            return this;
        }

        @Override
        public Request.Builder addQueryParam(String queryParam) {
            queryParams.add(queryParam);

            return this;
        }

        @Override
        public Request.Builder addQueryParam(String key, String value) {
            addQueryParam(key + "=" + value);

            return this;
        }

        @Override
        public Request.Builder addQueryParams(Map<String, String> queryParams) {
            for (Map.Entry<String, String> pair : queryParams.entrySet()) {
                addQueryParam(pair.getKey(), pair.getValue());
            }

            return this;
        }

        @Override
        public Request.Builder setUserAgent(String userAgent) {
            this.userAgent = userAgent;

            return this;
        }

        @Override
        public Request.Builder setMethod(String method) {
            this.method = HttpMethod.valueOf(method.toUpperCase());

            return this;
        }

        @Override
        public Request.Builder setMethod(HttpMethod method) {
            this.method = method;

            return this;
        }

        @Override
        public Request.Builder addCookie(String cookie) {
            cookies.add(cookie);

            return this;
        }

        @Override
        public Request.Builder addCookie(String key, String value) {
            addCookie(key + "=" + value);

            return this;
        }

        @Override
        public Request.Builder addCookies(Map<String, String> cookies) {
            for (Map.Entry<String, String> pair : cookies.entrySet()) {
                addCookie(pair.getKey(), pair.getValue());
            }

            return this;
        }

        @Override
        public Request.Builder addPOSTData(String postData) {
            this.postData.add(postData);

            return this;
        }

        @Override
        public Request.Builder addPOSTData(String key, String value) {
            addPOSTData(key + "=" + value);

            return this;
        }

        @Override
        public Request.Builder addPOSTData(Map<String, String> postData) {
            for (Map.Entry<String, String> pair : postData.entrySet()) {
                addPOSTData(pair.getKey(), pair.getValue());
            }

            return this;
        }
    }
}

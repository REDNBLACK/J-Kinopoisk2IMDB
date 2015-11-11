package org.f0w.k2i.console;

import com.google.common.base.Joiner;
import com.google.common.io.CharStreams;

import java.io.*;
import java.net.*;
import java.util.*;

public class Request {
    private String auth;
    private Configuration config;

    public Request(String auth, Configuration config) {
        this.auth = "id=" + auth;
        this.config = config;
    }

    public String findMovie(final String title) {
        String result = "";

        try {
            QueryFormat queryFormat = config.getEnum(QueryFormat.class, "query_format");
            URL url = buildSearchQuery(title, queryFormat);
            InputStream response = makeBaseHttpRequest(url).getInputStream();

            System.out.println(url.toString());

            result = CharStreams.toString(new InputStreamReader(response));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public String downloadMoviePage(final String imdbMovieId) throws MalformedURLException {
        String result = "";

        try {
            URL url = new URL("http://www.imdb.com/title/" + imdbMovieId);
            InputStream response = makeBaseHttpRequest(url).getInputStream();

            System.out.println(url.toString());

            result = CharStreams.toString(new InputStreamReader(response));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return result;
    }

    public URL buildSearchQuery(final String title, QueryFormat queryFormat) throws MalformedURLException {
        URL url;

        switch (queryFormat) {
            case XML:
                url = buildXMLSearchQuery(title);
                break;
            case JSON:
                url = buildJSONSearchQuery(title);
                break;
            case HTML:
                url = buildHTMLSearchQuery(title);
                break;
            default:
                throw new IllegalArgumentException(
                        String.format("Недопустимый тип запроса: '%s'", queryFormat.toString())
                );
        }

        return url;
    }

    private URL buildAPISearchQuery(final String title, QueryFormat queryFormat) throws MalformedURLException {
        String url = "http://www.imdb.com/xml/find?";

        Map<String, String> query = new LinkedHashMap<>();
        query.put("q", title); // Запрос
        query.put("tt", "on"); // Поиск только по названиям
        query.put("nr", "1");
        if (queryFormat == QueryFormat.JSON) {
            query.put("json", "1"); // Вывод в формате JSON
        }

        return new URL(makeFullUrl(url, query));
    }

    private URL buildXMLSearchQuery(final String movieTitle) throws MalformedURLException {
        return buildAPISearchQuery(movieTitle, QueryFormat.XML);
    }

    private URL buildJSONSearchQuery(final String movieTitle) throws MalformedURLException {
        return buildAPISearchQuery(movieTitle, QueryFormat.JSON);
    }

    private URL buildHTMLSearchQuery(final String movieTitle) throws MalformedURLException  {
        String url = "http://www.imdb.com/find?";

        Map<String, String> query = new LinkedHashMap<>();
        query.put("q", movieTitle); // Запрос
        query.put("s", "tt"); // Поиск только по названиям
//        query.put("exact", "true"); // Поиск только по полным совпадениям
        query.put("ref", "fn_tt_ex"); // Реферер для надежности

        return new URL(makeFullUrl(url, query));
    }

    private URLConnection makeBaseHttpRequest(URL url) throws IOException {
        URLConnection connection = url.openConnection();
        connection.setRequestProperty("User-Agent", config.get("user_agent"));
        connection.setRequestProperty("Cookie", auth);
        System.out.println(connection.getRequestProperties());

        return connection;
    }

    private String makeFullUrl(final String url, Map<String, String> query) {
        return url + Joiner.on("&").withKeyValueSeparator("=").join(query);
    }
}
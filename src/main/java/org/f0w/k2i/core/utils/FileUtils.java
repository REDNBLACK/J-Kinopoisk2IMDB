package org.f0w.k2i.core.utils;

import com.google.common.base.Strings;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import org.f0w.k2i.core.model.entity.Movie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    public static String getHashCode(File file) throws IOException {
        return Files.hash(file, Hashing.sha256()).toString();
    }

    public static List<Movie> parseMovies(File file) throws IOException {
        ArrayList<Movie> movies = new ArrayList<>();
        Document document = Jsoup.parse(file, StandardCharsets.UTF_8.name());
        Elements content = document.select("table tr");
        content.remove(0);

        for (Element entity : content) {
            Elements elements = entity.getElementsByTag("td");

            movies.add(new Movie(parseTitle(elements), parseYear(elements), parseRating(elements)));
        }

        return movies;
    }

    private static String parseTitle(Elements elements) throws IOException {
        String title = elements.get(1).text().trim();

        if (Strings.isNullOrEmpty(title)) {
            title = elements.get(0).text().trim();
        }

        if (Strings.isNullOrEmpty(title)) {
            throw new IOException("Error parsing movie title");
        }

        return title;
    }

    private static int parseYear(Elements elements) {
        String yearString = elements.get(2)
                .text()
                .trim()
                .substring(0, 4);

        return Integer.parseInt(yearString);
    }

    private static Integer parseRating(Elements elements) {
        String ratingString = elements.get(9).text().trim();

        if (Strings.isNullOrEmpty(ratingString)
            || "zero".equals(ratingString)
            || "0".equals(ratingString)
        ) {
            return null;
        }

        return Integer.parseInt(ratingString);
    }
}

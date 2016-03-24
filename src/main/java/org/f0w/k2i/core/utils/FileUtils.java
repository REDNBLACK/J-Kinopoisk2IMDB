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
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import static org.f0w.k2i.core.utils.MovieFieldsUtils.*;

public class FileUtils {
    public static String getHashCode(File file) throws IOException {
        return Files.hash(file, Hashing.sha256()).toString();
    }

    public static List<Movie> parseMovies(File file) throws IOException {
        ArrayList<Movie> movies = new ArrayList<>();

        Document document = Jsoup.parse(file, Charset.forName("windows-1251").toString());
        Elements content = document.select("table tr");
        content.remove(0);

        for (Element entity : content) {
            Elements elements = entity.getElementsByTag("td");

            movies.add(new Movie(
                    parseTitle(elements.get(1).text(), elements.get(0).text()),
                    parseYear(elements.get(2).text()),
                    parseRating(elements.get(9).text())
            ));
        }

        return movies;
    }
}

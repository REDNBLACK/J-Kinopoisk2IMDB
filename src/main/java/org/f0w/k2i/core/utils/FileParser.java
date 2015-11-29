package org.f0w.k2i.core.utils;

import com.google.common.base.Strings;
import org.f0w.k2i.core.entities.Movie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileParser {
    public static List<Movie> parseMovies(java.io.File file) throws IOException {
        ArrayList<Movie> movies = new ArrayList<>();
        Document document = Jsoup.parse(file, StandardCharsets.UTF_8.name());
        Elements content = document.select("table tr");
        content.remove(0);

        for (Element entity : content) {
            Elements elements = entity.getElementsByTag("td");

            Movie movie = new Movie();
            movie.setTitle(elements.get(1).text().trim());
            if (Strings.isNullOrEmpty(movie.getTitle())) {
                movie.setTitle(elements.get(0).text().trim());
            }
            movie.setYear(Integer.parseInt(elements.get(2).text().trim().substring(0, 4)));
            movie.setRating(Integer.parseInt(elements.get(9).text().trim()));

            movies.add(movie);
        }

        return movies;
    }
}

package org.f0w.k2i.core.Components;

import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import org.f0w.k2i.core.Models.Movie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class KinopoiskTableParser {
    public List<Movie> parse(final String data) {
        ArrayList<Movie> movies = new ArrayList<>();
        Document document = Jsoup.parse(data, "UTF-8");
        Elements content = document.select("table tr");
        content.remove(0);

        for (Element entity : content) {
            Elements elements = entity.getElementsByTag("td");

            System.out.println(elements);
            Movie movie = new Movie();
            movie.setTitle(elements.get(1).text().trim());
            movie.setYear(Integer.parseInt(elements.get(2).text().trim().substring(0, 4)));
            if (Strings.isNullOrEmpty(movie.getTitle())) {
                movie.setTitle(elements.get(0).text().trim());
            }

            movies.add(movie);
        }

        return ImmutableList.copyOf(movies);
    }
}

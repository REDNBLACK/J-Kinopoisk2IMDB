package org.f0w.k2i.console;

import com.google.common.base.Strings;
import com.google.common.collect.*;
import org.f0w.k2i.console.EqualityComparators.*;
import org.f0w.k2i.console.Models.Movie;
import org.json.simple.parser.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.util.*;
import java.util.regex.*;

public class Parser {
    private Configuration config;

    public Parser(Configuration config) {
        this.config = config;
    }

    public String parseMovieIMDBId(String data, Movie kinopoiskMovie) {
        String imdbMovieId = null;
        QueryFormat queryFormat = config.getEnum(QueryFormat.class, "query_format");
        List<Movie> imdbMovies = parseMovieSearchResult(data, queryFormat);
        EqualityComparatorType comparatorType = config.getEnum(EqualityComparatorType.class, "comparator");
        EqualityComparator<Movie> comparator = EqualityComparatorsFactory.make(comparatorType);

        for (Movie imdbMovie : imdbMovies) {
            if (comparator.areEqual(imdbMovie, kinopoiskMovie)
                && isFirstMovieYearInRangeOfSecondMovieYear(imdbMovie, kinopoiskMovie, config.getInt("year_deviation"))
            ) {
                imdbMovieId = imdbMovie.getImdbId();
                break;
            }
        }

        return imdbMovieId;
    }

    private boolean isFirstMovieYearInRangeOfSecondMovieYear(Movie movie1, Movie movie2, int step) {
        Set<Integer> yearsRange = ContiguousSet.create(
                Range.closed(movie2.getYear() - step, movie2.getYear() + step),
                DiscreteDomain.integers()
        );

        for (Integer year : yearsRange) {
            if (movie1.getYear().equals(year)) {
                return true;
            }
        }

        return false;
    }

    public List<Movie> parseMovieSearchResult(final String data, QueryFormat queryFormat) {
        List<Movie> movies;

        switch (queryFormat) {
            case XML:
                movies = parseMovieSearchXMLResult(data);
                break;
            case JSON:
                movies = parseMovieSearchJSONResult(data);
                break;
            case HTML:
                movies = parseMovieSearchHTMLResult(data);
                break;
            default:
                throw new IllegalArgumentException(
                        String.format("Недопустимый тип запроса: '%s'", queryFormat.toString())
                );
        }

        return ImmutableList.copyOf(movies);
    }

    private List<Movie> parseMovieSearchXMLResult(final String data) {
        List<Movie> movies = new ArrayList<>();
        Document document = Jsoup.parse(data, "UTF-8");

        for (Element element : document.getElementsByTag("ImdbEntity")) {
            Movie movie = new Movie();
            movie.setTitle(element.ownText());
            movie.setYear(Integer.parseInt(element.getElementsByTag("Description").first().text().substring(0, 4)));
            movie.setImdbId(element.attr("id"));

            movies.add(movie);
        }

        return movies;
    }

    private List<Movie> parseMovieSearchJSONResult(final String data) {
        List<Movie> movies = new ArrayList<>();
        JSONParser parser = new JSONParser();

        try {
            Map document = (Map) parser.parse(data, new ContainerFactory() {
                @Override
                public List creatArrayContainer() {
                    return new ArrayList();
                }

                @Override
                public Map createObjectContainer() {
                    return new LinkedHashMap();
                }
            });

            for (Object categories : document.values()) {
                for (Object movieInfo : (List) categories) {
                    Map movieInfoObj = (Map) movieInfo;

                    Movie movie = new Movie();
                    movie.setTitle(movieInfoObj.get("title").toString());
                    movie.setYear(Integer.parseInt(movieInfoObj.get("description").toString().substring(0, 4)));
                    movie.setImdbId(movieInfoObj.get("id").toString());

                    movies.add(movie);
                }
            }
        } catch (ParseException e){
            e.printStackTrace();
        }

        return movies;
    }

    private List<Movie> parseMovieSearchHTMLResult(final String data) {
        List<Movie> movies = new ArrayList<>();
        Document document = Jsoup.parse(data, "UTF-8");

        for (Element element : document.select("table.findList tr td.result_text")) {
            Element link = element.getElementsByTag("a").first();

            Movie movie = new Movie();
            movie.setTitle(link.text());
            movie.setImdbId(link.attr("href").split("/")[2]);
            Matcher m = Pattern.compile("\\(([0-9]{4})\\)").matcher(element.text());
            while (m.find()) {
                movie.setYear(Integer.parseInt(m.group(1)));
            }

            movies.add(movie);
        }

        return movies;
    }

    public String parseMovieAuthString(String data) {
        Document document = Jsoup.parse(data, "UTF-8");

        return document.select("[data-auth]")
                .first()
                .attr("data-auth")
        ;
    }

    public List<Movie> parseKinopoiskList(String data) {
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

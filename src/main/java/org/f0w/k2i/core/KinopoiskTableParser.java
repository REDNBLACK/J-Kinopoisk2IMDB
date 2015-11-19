package org.f0w.k2i.core;

import com.google.common.base.Strings;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import org.f0w.k2i.core.entities.File;
import org.f0w.k2i.core.entities.Movie;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import javax.persistence.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class KinopoiskTableParser {
    private EntityManager entityManager;

    public KinopoiskTableParser(EntityManagerFactory entityManagerFactory) {
        entityManager = entityManagerFactory.createEntityManager();
    }

    public File parse(java.io.File file) {
        File kinopoiskFile = null;
        String hashCode = getFileHashcode(file);

        try {
            entityManager.getTransaction().begin();
            Query query = entityManager.createNamedQuery("findKinopoiskFileByChecksum", File.class);
            query.setParameter("checksum", hashCode);

            kinopoiskFile = (File) query.getSingleResult();
        } catch (NoResultException e) {

        } catch (IOException e) {
            e.printStackTrace();
        }

        return kinopoiskFile;
    }

    private String getFileHashcode(java.io.File file) throws IOException {
        return Files.hash(file, Hashing.sha256()).toString();
    }

    private List<Movie> getMoviesList(java.io.File file) throws IOException {
        ArrayList<Movie> movies = new ArrayList<>();
        Document document = Jsoup.parse(file, "UTF-8");
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

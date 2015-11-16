package org.f0w.k2i.core;

import org.f0w.k2i.core.Components.Configuration;
import org.f0w.k2i.core.Components.HttpRequest;
import org.f0w.k2i.core.Requests.MovieAuthStringFetcher;
import org.f0w.k2i.core.Models.Movie;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class Main {
    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("K2IDB");

    public static void main(String[] args) throws Exception {
        Configuration config = new Configuration();
        config.loadSettings(new Main().getClass().getClassLoader().getResource("config.properties").getFile());
        config.loadSettings(new Main().getClass().getClassLoader().getResource("user_config.properties").getFile());

        MovieAuthStringFetcher handler = new MovieAuthStringFetcher(new HttpRequest(), config);
        System.out.println(handler.fetch("tt1375666"));

        // Request
//        String auth = "BCYuzKtGHqodwCwt7AzY8YtkW_JN65crXSjp0_ZW807AqSkbWfJ1Ng8oGIzCEUpJUo3zhxtfQcYO52KI7dnku1z-8xuG6hVJ4_EBkgHGPZnDpRhpXD_1jKFMuL0mKCg94yOLfemTgqgHfSb9FBOcIM3spafBaoUBFFWZIj7Ijrt3_QsWvg4rhldHvuSQrHU3eDo5j7r_zJt6rxgym--hDjbUaKedpAO5tDETOlhpHNUBFh0";
//        Request request = new Request(auth, config);
//
//        String moviePage = request.downloadMoviePage("tt1375666");

//        String data = request.findMovie("Inception");
//

        // Parser
//        Parser parser = new Parser(config);

//        System.out.println(parser.parseMovieAuthString(moviePage));
//        List<Movie> list = parser.parseMovieSearchResult(data, QueryFormat.JSON);
//        System.out.println(list.size());
//        System.out.println(list);
//
//        String content = Files.toString(new File("/users/RB/Downloads/kinopoisk.ru-Любимые-фильмы.xls"), Charset.forName("windows-1251"));

//        System.out.println(parser.parseKinopoiskList(content));


        // Models
//        Main main = new Main();
//        main.save();
//        main.read();
    }

    public void save() {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();

        em.persist(new Movie("Inception", 2008));
        em.persist(new Movie("Big Fish", 2005));
        em.persist(new Movie("Interstellar", 2014, "tt2412312"));

        em.getTransaction().commit();
        em.close();
    }

    public void read() {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();

        List<Movie> result = em.createQuery("FROM Movie", Movie.class).getResultList();
        for (Movie movie : result) {
            System.out.println(movie.getId() + " " + movie.getTitle() + " " + movie.getYear() + " " + movie.getImdbId());
        }

        em.getTransaction().commit();
        em.close();
    }
}
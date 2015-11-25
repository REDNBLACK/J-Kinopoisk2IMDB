package org.f0w.k2i.core;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.f0w.k2i.core.comparators.EqualityComparator;
import org.f0w.k2i.core.comparators.EqualityComparatorType;
import org.f0w.k2i.core.comparators.EqualityComparatorsFactory;
import org.f0w.k2i.core.configuration.Configuration;
import org.f0w.k2i.core.configuration.NativeConfiguration;
import org.f0w.k2i.core.exchange.MovieAuthStringFetcher;
import org.f0w.k2i.core.exchange.MovieFinders.MovieFinder;
import org.f0w.k2i.core.exchange.MovieFinders.MovieFinderType;
import org.f0w.k2i.core.exchange.MovieFinders.MovieFindersFactory;
import org.f0w.k2i.core.entities.Movie;
import org.f0w.k2i.core.exchange.MovieRatingChanger;
import org.f0w.k2i.core.exchange.MovieWatchlistAssigner;
import org.f0w.k2i.core.filters.EmptyMovieInfoFilter;
import org.f0w.k2i.core.filters.MovieYearDeviationFilter;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;
import java.util.prefs.Preferences;

public class Main {
//    private EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("K2IDB");

    public static void main(String[] args) throws Exception {
        new NativeConfiguration(Main.class.getClassLoader().getResource("config.xml").getFile());
        Injector injector = Guice.createInjector(new ServiceProvider());
        Movie movie = new Movie("Inception", 2010, 10);

        MovieManager manager = injector.getInstance(MovieManager.class);
        manager.setMovie(movie);
        manager.handleMovie();

        System.out.println(manager.isSuccessful());

//        String content = Files.toString(new File("/users/RB/Downloads/kinopoisk.ru-Любимые-фильмы.xls"), Charset.forName("windows-1251"));

        // Entities
//        Main main = new Main();
//        main.save();
//        main.read();
    }

//    public void save() {
//        EntityManager em = entityManagerFactory.createEntityManager();
//        em.getTransaction().begin();
//
//        em.persist(new Movie("Inception", 2008));
//        em.persist(new Movie("Big Fish", 2005));
//        em.persist(new Movie("Interstellar", 2014, "tt2412312"));
//
//        em.getTransaction().commit();
//        em.close();
//    }
//
//    public void read() {
//        EntityManager em = entityManagerFactory.createEntityManager();
//        em.getTransaction().begin();
//
//        List<Movie> result = em.createQuery("FROM Movie", Movie.class).getResultList();
//        for (Movie movie : result) {
//            System.out.println(movie.getId() + " " + movie.getTitle() + " " + movie.getYear() + " " + movie.getImdbId());
//        }
//
//        em.getTransaction().commit();
//        em.close();
//    }
}
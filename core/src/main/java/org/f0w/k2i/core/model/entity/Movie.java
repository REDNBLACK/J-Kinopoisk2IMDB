package org.f0w.k2i.core.model.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "MOVIES", uniqueConstraints = @UniqueConstraint(columnNames = {"TITLE", "YEAR"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false, of = {"title", "year"})
public class Movie extends BaseEntity {
    @Column(name = "TITLE", nullable = false)
    @NonNull
    private String title;

    @Column(name = "YEAR", nullable = false)
    private int year;

    @Column(name = "TYPE", nullable = false)
    @Enumerated(EnumType.STRING)
    @NonNull
    private Type type;

    @Column(name = "RATING")
    private Integer rating;

    @Column(name = "IMDB_ID")
    private String imdbId;

    public Movie(String title, int year) {
        this(title, year, Type.MOVIE, null, null);
    }

    public Movie(String title, int year, Integer rating) {
        this(title, year, Type.MOVIE, rating, null);
    }

    public Movie(String title, int year, Type type) {
        this(title, year, type, null, null);
    }

    public Movie(String title, int year, String imdbId) {
        this(title, year, Type.MOVIE, null, imdbId);
    }

    public Movie(Movie movie) {
        this(movie.getTitle(), movie.getYear(), movie.getType(), movie.getRating(), movie.getImdbId());
    }

    /**
     * Type of movie
     */
    public enum Type {
        MOVIE,
        DOCUMENTARY,
        SHORT,
        SERIES,
        VIDEO_GAME
    }
}

package org.f0w.k2i.core.model.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "MOVIES", uniqueConstraints = @UniqueConstraint(columnNames = {"TITLE", "YEAR"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"title", "year", "type"})
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

    public Movie(Movie movie) {
        this(movie.getTitle(), movie.getYear(), movie.getType(), movie.getRating(), movie.getImdbId());
    }

    /**
     * Checks that title equals "null" string
     *
     * @return Is title empty
     */
    public boolean isEmptyTitle() {
        return "null".equals(getTitle());
    }

    /**
     * Checks that year equals 0
     *
     * @return Is year empty
     */
    public boolean isEmptyYear() {
        return getYear() == 0;
    }

    /**
     * Checks that type equals to {@link Movie.Type#MOVIE}
     *
     * @return Is default type
     */
    public boolean isDefaultType() {
        return getType().equals(Type.MOVIE);
    }

    /**
     * Checks that IMDB ID is null
     *
     * @return Is IMDB ID null
     */
    public boolean isEmptyIMDBId() {
        return getImdbId() == null;
    }

    /**
     * Checks that rating is null
     *
     * @return Is rating null
     */
    public boolean isEmptyRating() {
        return getRating() == null;
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

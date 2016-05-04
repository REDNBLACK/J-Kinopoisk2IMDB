package org.f0w.k2i.core.model.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

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

    @Column(name = "RATING")
    private Integer rating;

    @Column(name = "IMDB_ID")
    private String imdbId;

    public Movie(String title, int year) {
        this(title, year, null, null);
    }

    public Movie(String title, int year, Integer rating) {
        this(title, year, rating, null);
    }

    public Movie(String title, int year, String imdbId) {
        this(title, year, null, imdbId);
    }
}

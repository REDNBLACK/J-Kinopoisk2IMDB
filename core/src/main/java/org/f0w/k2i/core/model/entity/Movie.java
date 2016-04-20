package org.f0w.k2i.core.model.entity;

import com.google.common.base.MoreObjects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

@Entity
@Table(name = "MOVIES", uniqueConstraints = @UniqueConstraint(columnNames = {"TITLE", "YEAR"}))
public class Movie extends BaseEntity {
    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "YEAR", nullable = false)
    private int year;

    @Column(name = "RATING")
    private Integer rating;

    @Column(name = "IMDB_ID")
    private String imdbId;

    protected Movie() {
        // Required by JPA
    }

    public Movie(String title, int year, Integer rating, String imdbId) {
        setTitle(title);
        setYear(year);
        setRating(rating);
        setImdbId(imdbId);
    }

    public Movie(String title, int year) {
        this(title, year, null, null);
    }

    public Movie(String title, int year, Integer rating) {
        this(title, year, rating, null);
    }

    public Movie(String title, int year, String imdbId) {
        this(title, year, null, imdbId);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = requireNonNull(title);
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Movie other = (Movie) obj;
        return Objects.equals(getTitle(), other.getTitle())
                && Objects.equals(getYear(), other.getYear());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getYear());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .omitNullValues()
                .add("id", getId())
                .add("title", getTitle())
                .add("year", getYear())
                .add("rating", getRating())
                .add("imdb_id", getImdbId())
                .toString();
    }
}

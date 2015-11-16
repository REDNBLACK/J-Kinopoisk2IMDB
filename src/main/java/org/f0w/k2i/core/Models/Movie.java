package org.f0w.k2i.core.Models;

import javax.persistence.*;

import com.google.common.base.MoreObjects;
import org.hibernate.annotations.GenericGenerator;

import java.util.Objects;

@Entity
@Table(name = "MOVIES", uniqueConstraints = @UniqueConstraint(columnNames = {"title", "year"}))
public class Movie {
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Long id;

    @Column(name = "TITLE", nullable = false)
    private String title;

    @Column(name = "YEAR", nullable = false)
    private Integer year;

    @Column(name = "IMDB_ID")
    private String imdbId;

    public Movie() {}

    public Movie(String title, Integer year, String imdbId) {
        this.title = title;
        this.year = year;
        this.imdbId = imdbId;
    }

    public Movie(String title, Integer year) {
        this(title, year, null);
    }

    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
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
                && Objects.equals(getYear(), other.getYear())
        ;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTitle(), getYear());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", getId())
                .add("title", getTitle())
                .add("year", getYear())
                .add("imdb_id", getImdbId())
                .toString()
        ;
    }
}

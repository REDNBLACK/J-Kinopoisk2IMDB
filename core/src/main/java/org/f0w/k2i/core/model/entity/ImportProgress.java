package org.f0w.k2i.core.model.entity;

import javax.persistence.*;

import com.google.common.base.MoreObjects;
import org.hibernate.annotations.GenericGenerator;

import java.util.Objects;

@Entity
@Table(name = "IMPORT_PROGRESS", uniqueConstraints = @UniqueConstraint(columnNames = {"KINOPOISK_FILE_ID", "MOVIE_ID"}))
public class ImportProgress {
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "KINOPOISK_FILE_ID", nullable = false)
    private KinopoiskFile kinopoiskFile;

    @ManyToOne
    @JoinColumn(name = "MOVIE_ID", nullable = false)
    private Movie movie;

    @Column(name = "IMPORTED", nullable = false)
    private boolean imported;

    @Column(name = "RATED", nullable = false)
    private boolean rated;

    protected ImportProgress() {
        // Required by JPA
    }

    public ImportProgress(KinopoiskFile kinopoiskFile, Movie movie, boolean imported, boolean rated) {
        setKinopoiskFile(kinopoiskFile);
        setMovie(movie);
        setImported(imported);
        setRated(rated);
    }

    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    public KinopoiskFile getKinopoiskFile() {
        return kinopoiskFile;
    }

    public void setKinopoiskFile(KinopoiskFile kinopoiskFile) {
        this.kinopoiskFile = kinopoiskFile;
    }

    public boolean isImported() {
        return imported;
    }

    public void setImported(boolean imported) {
        this.imported = imported;
    }

    public boolean isRated() {
        return rated;
    }

    public void setRated(boolean rated) {
        this.rated = rated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImportProgress that = (ImportProgress) o;
        return isImported() == that.isImported() &&
                isRated() == that.isRated() &&
                Objects.equals(getKinopoiskFile(), that.getKinopoiskFile()) &&
                Objects.equals(getMovie(), that.getMovie());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKinopoiskFile(), getMovie(), isImported(), isRated());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .omitNullValues()
                .add("id", id)
                .add("kinopoiskFile", kinopoiskFile)
                .add("movie", movie)
                .add("imported", imported)
                .add("rated", rated)
                .toString();
    }
}

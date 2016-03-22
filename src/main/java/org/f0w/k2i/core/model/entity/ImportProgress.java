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

    @Column(name = "KINOPOISK_FILE_ID", nullable = false)
    private Long kinopoiskFileId;

    @Column(name = "MOVIE_ID", nullable = false)
    private Long movieId;

    @Column(name = "IMPORTED", nullable = false)
    private boolean imported;

    @Column(name = "RATED", nullable = false)
    private boolean rated;

    @ManyToOne
    private Movie movie;

    @ManyToOne
    private KinopoiskFile kinopoiskFile;

    public ImportProgress() {}

    public ImportProgress(long kinopoiskFileId, long movieId, boolean imported, boolean rated) {
        setKinopoiskFileId(kinopoiskFileId);
        setMovieId(movieId);
        setImported(imported);
        setRated(rated);
    }

    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    public long getKinopoiskFileId() {
        return kinopoiskFileId;
    }

    public void setKinopoiskFileId(long kinopoiskFileId) {
        this.kinopoiskFileId = kinopoiskFileId;
    }

    public long getMovieId() {
        return movieId;
    }

    public void setMovieId(long movieId) {
        this.movieId = movieId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImportProgress that = (ImportProgress) o;
        return isImported() == that.isImported() &&
                isRated() == that.isRated() &&
                Objects.equals(getKinopoiskFileId(), that.getKinopoiskFileId()) &&
                Objects.equals(getMovieId(), that.getMovieId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getKinopoiskFileId(), getMovieId(), isImported(), isRated());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("fileId", kinopoiskFileId)
                .add("movieId", movieId)
                .add("imported", imported)
                .add("rated", rated)
                .toString();
    }
}

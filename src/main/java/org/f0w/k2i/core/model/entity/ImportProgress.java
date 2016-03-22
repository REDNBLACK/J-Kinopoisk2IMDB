package org.f0w.k2i.core.model.entity;

import javax.persistence.*;

import com.google.common.base.MoreObjects;
import org.hibernate.annotations.GenericGenerator;

import java.util.Objects;

@Entity
@Table(name = "IMPORT_PROGRESS", uniqueConstraints = @UniqueConstraint(columnNames = {"FILE_ID", "MOVIE_ID"}))
public class ImportProgress {
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Long id;

    @Column(name = "FILE_ID", nullable = false)
    private Long fileId;

    @Column(name = "MOVIE_ID", nullable = false)
    private Long movieId;

    @Column(name = "IMPORTED", nullable = false)
    private boolean imported;

    @Column(name = "RATED", nullable = false)
    private boolean rated;

    private Movie movie;

    private KinopoiskFile kinopoiskFile;

    public ImportProgress() {}

    public ImportProgress(long fileId, long movieId, boolean imported, boolean rated) {
        setFileId(fileId);
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

    public long getFileId() {
        return fileId;
    }

    public void setFileId(long fileId) {
        this.fileId = fileId;
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

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "IMPORT_PROGRESS", cascade = CascadeType.ALL)
    public Movie getMovie() {
        return movie;
    }

    public void setMovie(Movie movie) {
        this.movie = movie;
    }

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "IMPORT_PROGRESS", cascade = CascadeType.ALL)
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
                Objects.equals(getFileId(), that.getFileId()) &&
                Objects.equals(getMovieId(), that.getMovieId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFileId(), getMovieId(), isImported(), isRated());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("fileId", fileId)
                .add("movieId", movieId)
                .add("imported", imported)
                .add("rated", rated)
                .toString();
    }
}

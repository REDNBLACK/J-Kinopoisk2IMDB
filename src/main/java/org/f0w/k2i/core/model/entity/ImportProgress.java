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
    private Integer fileId;

    @Column(name = "MOVIE_ID", nullable = false)
    private Integer movieId;

    @Column(name = "IMPORTED", nullable = false)
    private boolean imported;

    @Column(name = "RATED", nullable = false)
    private boolean rated;

    @Column(name = "ADDITIONAL_INFO")
    private String additionalInfo;

    public ImportProgress() {}

    public ImportProgress(int fileId, int movieId, boolean imported, boolean rated, String additionalInfo) {
        setFileId(fileId);
        setMovieId(movieId);
        setImported(imported);
        setRated(rated);
        setAdditionalInfo(additionalInfo);
    }

    public ImportProgress(int fileId, int movieId, boolean imported, boolean rated) {
        this(fileId, movieId, imported, rated, null);
    }

    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    public int getFileId() {
        return fileId;
    }

    public void setFileId(int fileId) {
        this.fileId = fileId;
    }

    public int getMovieId() {
        return movieId;
    }

    public void setMovieId(int movieId) {
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

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(final String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ImportProgress that = (ImportProgress) o;
        return isImported() == that.isImported() &&
                isRated() == that.isRated() &&
                Objects.equals(getFileId(), that.getFileId()) &&
                Objects.equals(getMovieId(), that.getMovieId()) &&
                Objects.equals(getAdditionalInfo(), that.getAdditionalInfo());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getFileId(), getMovieId(), isImported(), isRated(), getAdditionalInfo());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("fileId", fileId)
                .add("movieId", movieId)
                .add("imported", imported)
                .add("rated", rated)
                .add("additionalInfo", additionalInfo)
                .toString();
    }
}

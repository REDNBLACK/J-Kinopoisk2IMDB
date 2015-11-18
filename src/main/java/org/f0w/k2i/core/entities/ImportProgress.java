package org.f0w.k2i.core.entities;

import javax.persistence.*;

import com.google.common.base.Preconditions;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "IMPORT_PROGRESS")
public class ImportProgress {
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Long id;

    @Column(name = "FILE_ID", nullable = false)
    private Integer fileId;

    @Column(name = "MOVIE_ID", nullable = false)
    private Integer movieId;

    @Column(name = "STATUS", nullable = false)
    private Integer status;

    @Column(name = "STATUS_DATA")
    private String statusData;

    public ImportProgress() {}

    public ImportProgress(Integer fileId, Integer movieId, Integer status, String statusData) {
        setFileId(fileId);
        setMovieId(movieId);
        setStatus(status);
        setStatusData(statusData);
    }

    public ImportProgress(Integer fileId, Integer movieId, Integer status) {
        this(fileId, movieId, status, null);
    }

    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = Preconditions.checkNotNull(fileId);
    }

    public Integer getMovieId() {
        return movieId;
    }

    public void setMovieId(Integer movieId) {
        this.movieId = Preconditions.checkNotNull(movieId);
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = Preconditions.checkNotNull(status);
    }

    public String getStatusData() {
        return statusData;
    }

    public void setStatusData(String statusData) {
        this.statusData = statusData;
    }
}

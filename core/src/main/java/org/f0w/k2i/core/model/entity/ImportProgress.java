package org.f0w.k2i.core.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "IMPORT_PROGRESS", uniqueConstraints = @UniqueConstraint(columnNames = {"KINOPOISK_FILE_ID", "MOVIE_ID", "LIST_ID"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ImportProgress extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "KINOPOISK_FILE_ID", nullable = false)
    private KinopoiskFile kinopoiskFile;

    @ManyToOne
    @JoinColumn(name = "MOVIE_ID", nullable = false)
    private Movie movie;

    @Column(name = "LIST_ID", nullable = false)
    private String listId;

    @Column(name = "IMPORTED", nullable = false)
    private boolean imported;

    @Column(name = "RATED", nullable = false)
    private boolean rated;

    public ImportProgress(ImportProgress ip) {
        this(new KinopoiskFile(ip.getKinopoiskFile()), new Movie(ip.getMovie()), ip.getListId(), ip.isImported(), ip.isRated());
    }
}

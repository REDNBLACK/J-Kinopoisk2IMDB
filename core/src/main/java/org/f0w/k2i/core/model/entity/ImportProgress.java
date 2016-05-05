package org.f0w.k2i.core.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "IMPORT_PROGRESS", uniqueConstraints = @UniqueConstraint(columnNames = {"KINOPOISK_FILE_ID", "MOVIE_ID"}))
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

    @Column(name = "IMPORTED", nullable = false)
    private boolean imported;

    @Column(name = "RATED", nullable = false)
    private boolean rated;

    public ImportProgress(ImportProgress ip) {
        this(new KinopoiskFile(ip.getKinopoiskFile()), new Movie(ip.getMovie()), ip.isImported(), ip.isRated());
    }
}

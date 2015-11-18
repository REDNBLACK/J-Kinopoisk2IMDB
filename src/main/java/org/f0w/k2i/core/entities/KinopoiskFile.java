package org.f0w.k2i.core.entities;

import javax.persistence.*;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.hash.Hashing;
import com.google.common.io.Files;
import org.hibernate.annotations.GenericGenerator;
import java.io.*;
import java.util.Objects;

@Entity
@NamedQuery(
        name="findKinopoiskFileByChecksum",
        query="SELECT OBJECT(kf) FROM KinopoiskFile kf WHERE kf.checksum = :checksum"
)
@Table(name = "KINOPOISK_FILES")
public class KinopoiskFile {
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Long id;

    @Column(name = "CHECKSUM", unique = true, nullable = false)
    private String checksum;

    public KinopoiskFile() {}

    public KinopoiskFile(String checksum) {
        setChecksum(checksum);
    }

    public KinopoiskFile(File file) {
        setChecksum(file);
    }

    public Long getId() {
        return id;
    }

    private void setId(Long id) {
        this.id = id;
    }

    public String getChecksum() {
        return checksum;
    }

    public void setChecksum(String checksum) {
        this.checksum = Preconditions.checkNotNull(checksum);
    }

    public void setChecksum(File file) {
        try {
            setChecksum(Files.hash(file, Hashing.sha256()).toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        return Objects.equals(getChecksum(), ((KinopoiskFile) obj).getChecksum());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getChecksum());
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", getId())
                .add("checksum", getChecksum())
                .toString()
        ;
    }
}

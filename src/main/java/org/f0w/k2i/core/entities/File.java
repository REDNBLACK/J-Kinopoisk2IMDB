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
        name="findFileByChecksum",
        query="SELECT OBJECT(f) FROM File f WHERE f.checksum = :checksum"
)
@Table(name = "FILE")
public class File {
    @Id
    @GeneratedValue(generator="increment")
    @GenericGenerator(name="increment", strategy = "increment")
    private Long id;

    @Column(name = "CHECKSUM", unique = true, nullable = false)
    private String checksum;

    public File() {}

    public File(String checksum) {
        setChecksum(checksum);
    }

    public File(java.io.File file) {
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

    public void setChecksum(java.io.File file) {
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

        return Objects.equals(getChecksum(), ((File) obj).getChecksum());
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

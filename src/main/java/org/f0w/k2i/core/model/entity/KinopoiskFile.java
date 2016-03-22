package org.f0w.k2i.core.model.entity;

import javax.persistence.*;

import com.google.common.base.MoreObjects;
import org.hibernate.annotations.GenericGenerator;
import java.util.Objects;
import static com.google.common.base.Preconditions.*;

@Entity
@Table(name = "KINOPOISK_FILE")
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
        this.checksum = checkNotNull(checksum);
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
                .add("id", id)
                .add("checksum", checksum)
                .toString();
    }
}

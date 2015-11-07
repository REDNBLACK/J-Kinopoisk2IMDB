package org.f0w.k2i.console.Models;

import javax.persistence.*;
import org.hibernate.annotations.GenericGenerator;
import java.io.*;
import java.security.*;

@Entity
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
        this.checksum = checksum;
    }

    public void setChecksum(File file) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            try (InputStream is = new FileInputStream(file);
                 DigestInputStream dis = new DigestInputStream(is, md)
            ) {
                byte[] buffer = new byte[16384];
                while (dis.read(buffer) != -1) {
                }

                StringBuilder sb = new StringBuilder(64);
                for (byte b : md.digest()) {
                    sb.append(String.format("%02x", b));
                }

                setChecksum(sb.toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}

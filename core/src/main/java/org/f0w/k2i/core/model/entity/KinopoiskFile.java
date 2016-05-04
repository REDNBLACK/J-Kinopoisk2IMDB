package org.f0w.k2i.core.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "KINOPOISK_FILE")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KinopoiskFile extends BaseEntity {
    @Column(name = "HASH_CODE", unique = true, nullable = false)
    @NonNull
    private String hashCode;
}

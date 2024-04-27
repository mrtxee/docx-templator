package ru.fabit.docxtemplate.infrastructure.persistence.store.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.time.Instant;

/**
 * Файл шаблона
 */
@Data
@Entity
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "files_")
@ToString(exclude = {"data"})
public class FileStore {

  @Id
  @SequenceGenerator(name = "files_seq", sequenceName = "files_id_seq")
  @GeneratedValue(generator = "files_seq", strategy = GenerationType.SEQUENCE)
  private Long id;

  @Lob
  @Column(name = "data")
  private byte[] data;

  @Column(name = "created_date")
  private Instant createdDate;

  @PrePersist
  void prePersist() {

    if (createdDate == null) {
      createdDate = Instant.now();
    }
  }
}
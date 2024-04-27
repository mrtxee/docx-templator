package ru.fabit.docxtemplate.infrastructure.persistence.store.models;

import com.vladmihalcea.hibernate.type.array.StringArrayType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Type;
import java.time.Instant;

/**
 * Информация о шаблоне
 */
@Data
@Entity
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "templates")
public class TemplateStore {

  @Id
  @SequenceGenerator(name = "templates_seq", sequenceName = "templates_id_seq")
  @GeneratedValue(generator = "templates_seq", strategy = GenerationType.SEQUENCE)
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "version")
  private Integer version;

  @Column(name = "last_version")
  private boolean lastVersion;

  @Type(StringArrayType.class)
  @Column(
    name = "template_tags",
    columnDefinition = "text[]"
  )
  private String[] templateTags;

  @Column(name = "file_id")
  private Long fileId;

  @Column(name = "created_date")
  private Instant createdDate;

  @PrePersist
  void prePersist() {

    if (createdDate == null) {
      createdDate = Instant.now();
    }
  }
}
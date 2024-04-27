package ru.fabit.docxtemplate.domain.templates.models;

import lombok.Getter;
import lombok.ToString;
import java.time.Instant;

@Getter
@ToString(exclude = {"data"})
public class TemplateFile {

  private FileId id;

  private byte[] data;

  private Instant createdDate;

  private TemplateFile(final FileId id, final byte[] data, final Instant createdDate) {
    this.id = id;
    this.data = data;
    this.createdDate = createdDate;
  }

  public static TemplateFile createFromRepository(final FileId id, final byte[] data, final Instant createdDate) {
    return new TemplateFile(id, data, createdDate);
  }
}
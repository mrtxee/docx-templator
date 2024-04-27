package ru.fabit.docxtemplate.infrastructure.web.rest.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TemplateResponse implements Serializable {

  private String name;

  private int version;

  private boolean lastVersion;

  private List<String> templateTags;

  private Instant createdDate;
}
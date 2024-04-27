package ru.fabit.docxtemplate.api.requests;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TemplateData implements Serializable {

  @NotNull
  @NotEmpty
  private String name;

  @NotNull
  private String value;
}
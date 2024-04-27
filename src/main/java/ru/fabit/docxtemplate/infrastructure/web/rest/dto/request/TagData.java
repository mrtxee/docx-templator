package ru.fabit.docxtemplate.infrastructure.web.rest.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.fabit.docxtemplate.domain.templates.models.TagDataSupplier;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TagData implements TagDataSupplier {

  private String name;

  private String value;
}
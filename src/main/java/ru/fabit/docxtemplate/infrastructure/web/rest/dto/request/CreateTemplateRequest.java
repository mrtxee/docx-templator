package ru.fabit.docxtemplate.infrastructure.web.rest.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.fabit.docxtemplate.api.templates.usecases.CreateTemplateParams;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateTemplateRequest implements CreateTemplateParams {

  private String name;

  private List<String> templateTags;

  private String templateFile;
}
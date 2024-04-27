package ru.fabit.docxtemplate.api.templates.usecases;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.fabit.docxtemplate.api.templates.exceptions.TemplateNotFoundException;
import ru.fabit.docxtemplate.api.templates.repositories.FileRepository;
import ru.fabit.docxtemplate.api.templates.repositories.TemplateRepository;
import ru.fabit.docxtemplate.domain.templates.models.Template;
import ru.fabit.docxtemplate.domain.templates.models.TemplateFile;
import ru.fabit.docxtemplate.domain.templates.models.TemplateName;
import ru.fabit.docxtemplate.domain.templates.models.TemplateSubstitutionValues;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreatePdfByTemplateUseCase {


  private final FileRepository fileRepository;
  private final TemplateRepository templateRepository;
  private final TemplateRenderer templateRenderer;

  public InputStream create(final TemplateName templateName, final TemplateSubstitutionValues templateSubstitutionValues) throws IOException {

    final Template template = templateRepository.findByName(templateName.toString());

    if (template == null) {
      throw new TemplateNotFoundException("Not found template with name = " + templateName);
    }

    final TemplateFile templateFile = fileRepository.findById(template.getFileId());
    final InputStream stream = new ByteArrayInputStream(templateFile.getData());

    return templateRenderer.renderAsPdf(stream, templateSubstitutionValues, template.getFileId().toString());
  }

  public InputStream create(final TemplateName templateName, final Integer templateVersion,
                            final TemplateSubstitutionValues templateSubstitutionValues) throws IOException {

    final Template template = templateRepository.findByNameAndVersion(templateName.toString(), templateVersion);

    if (template == null) {
      throw new TemplateNotFoundException(
        "Not found template with name = " + templateName + "and version = " + templateVersion
      );
    }

    final TemplateFile templateFile = fileRepository.findById(template.getFileId());

    final InputStream stream = new ByteArrayInputStream(templateFile.getData());

    return templateRenderer.renderAsPdf(stream, templateSubstitutionValues, template.getFileId().toString());
  }


}
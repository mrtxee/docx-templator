package ru.fabit.docxtemplate.api.templates.usecases;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.util.StringUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.fabit.docxtemplate.api.templates.repositories.FileRepository;
import ru.fabit.docxtemplate.api.templates.repositories.TemplateRepository;
import ru.fabit.docxtemplate.domain.templates.models.FileId;
import ru.fabit.docxtemplate.domain.templates.models.Template;
import ru.fabit.docxtemplate.domain.templates.models.TemplateId;
import java.util.Base64;

@Slf4j
@Service
@RequiredArgsConstructor
public class CreateTemplateUseCase {

  private final FileRepository fileRepository;
  private final TemplateRepository templateRepository;
  private final DocxFormatChecker docxFormatChecker;

  @Transactional
  public Template create(final CreateTemplateParams createTemplateParams) {

    Template.createValidate(createTemplateParams.getName(), createTemplateParams.getTemplateTags());
    final byte[] file = StringUtil.isBlank(createTemplateParams.getTemplateFile())
      ? new byte[]{}
      : Base64.getDecoder().decode(createTemplateParams.getTemplateFile());

    docxFormatChecker.assertFileDocXFormat(file);

    final FileId fileId = fileRepository.save(file);

    final int lastExistVersion = templateRepository.findLastVersionByName(createTemplateParams.getName());

    final Template template = Template.create(
      new TemplateId(null), createTemplateParams.getName(), lastExistVersion, createTemplateParams.getTemplateTags(),
      fileId
    );

    templateRepository.save(template);

    return template;
  }
}
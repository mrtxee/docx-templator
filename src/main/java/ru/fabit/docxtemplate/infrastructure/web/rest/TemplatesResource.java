package ru.fabit.docxtemplate.infrastructure.web.rest;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.fabit.docxtemplate.api.templates.usecases.CreatePdfByTemplateUseCase;
import ru.fabit.docxtemplate.api.templates.usecases.CreateTemplateUseCase;
import ru.fabit.docxtemplate.domain.templates.models.Template;
import ru.fabit.docxtemplate.domain.templates.models.TemplateName;
import ru.fabit.docxtemplate.domain.templates.models.TemplateSubstitutionValues;
import ru.fabit.docxtemplate.infrastructure.persistence.query.TemplateQueryRepository;
import ru.fabit.docxtemplate.infrastructure.persistence.store.models.TemplateStore;
import ru.fabit.docxtemplate.infrastructure.web.rest.dto.request.CreatePdfRequest;
import ru.fabit.docxtemplate.infrastructure.web.rest.dto.request.CreateTemplateRequest;
import ru.fabit.docxtemplate.infrastructure.web.rest.dto.request.TemplateFilters;
import ru.fabit.docxtemplate.infrastructure.web.rest.dto.response.TemplateResponse;
import ru.fabit.docxtemplate.infrastructure.web.rest.mappers.TemplateMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/templates")
@RequiredArgsConstructor
public class TemplatesResource {

  private static final String PDF = ".pdf";
  private static final String DOCX = ".docx";

  private final CreatePdfByTemplateUseCase createPdfByTemplateUseCase;
  private final CreateTemplateUseCase createTemplateUseCase;
  private final TemplateMapper templateMapper;
  private final TemplateQueryRepository templateQueryRepository;

  @GetMapping
  public ResponseEntity<List<TemplateResponse>> getTemplates(final TemplateFilters filters) {

    log.debug("REST request to get templates by filters = {}", filters);

    final List<TemplateStore> templates = templateQueryRepository.findTemplates(filters);

    return ResponseEntity.ok(templateMapper.toResponse(templates));
  }

  @PostMapping
  public ResponseEntity<TemplateResponse> createTemplate(@RequestBody final CreateTemplateRequest request) {

    log.info("REST request to create template with name: {}", request.getName());

    final Template template = createTemplateUseCase.create(request);

    return new ResponseEntity<>(templateMapper.toResponse(template), HttpStatus.CREATED);
  }

  @PostMapping("/{templateName}/pdf")
  public ResponseEntity<InputStreamResource> generatePdfByTemplate(@PathVariable final String templateName,
                                                                   @RequestBody final CreatePdfRequest data)
    throws IOException {

    log.info("REST request to create pdf file by template with name = {}", templateName);

    final InputStream inputStream = createPdfByTemplateUseCase.create(TemplateName.from(templateName),
      TemplateSubstitutionValues.fromTagDataSupplier(data.getData()));

    return ResponseEntity
      .ok()
      .header("Content-Disposition", "attachment; filename=" + templateName + PDF)
      .contentLength(inputStream.available())
      .contentType(MediaType.APPLICATION_PDF)
      .body(new InputStreamResource(inputStream));
  }

  @PostMapping("/{templateName}/{templateVersion}/pdf")
  public ResponseEntity<InputStreamResource> generatePdfByTemplateVersion(@PathVariable final String templateName,
                                                                          @PathVariable final Integer templateVersion,
                                                                          @RequestBody final CreatePdfRequest data)
    throws IOException {

    log.info("REST request to create pdf file by template with name = {} version {}", templateName, templateVersion);

    final InputStream inputStream = createPdfByTemplateUseCase.create(
      TemplateName.from(templateName), templateVersion, TemplateSubstitutionValues.fromTagDataSupplier(data.getData())
    );

    return ResponseEntity.ok()
      .header("Content-Disposition", "attachment; filename=" + templateName + templateVersion + PDF)
      .contentLength(inputStream.available())
      .contentType(MediaType.APPLICATION_PDF)
      .body(new InputStreamResource(inputStream));
  }


  @GetMapping("/{templateName}")
  public ResponseEntity<TemplateResponse> getTemplate(@PathVariable final String templateName) {

    log.debug("REST request to get template {}", templateName);

    final TemplateStore template = templateQueryRepository.findTemplateByName(TemplateName.from(templateName));

    return ResponseEntity.ok(templateMapper.toResponse(template));
  }

  @GetMapping("/{templateName}/{templateVersion}")
  public ResponseEntity<TemplateResponse> getTemplateVersion(@PathVariable final String templateName,
                                                      @PathVariable final Integer templateVersion) {

    log.debug("REST request to get template {} version {}", templateName, templateVersion);

    final TemplateStore template = templateQueryRepository.findTemplateByNameAndVersion(
      TemplateName.from(templateName), templateVersion);

    return ResponseEntity.ok(templateMapper.toResponse(template));
  }

  @GetMapping("/{templateName}/{templateVersion}/file")
  public ResponseEntity<InputStreamResource> getTemplateVersionFile(@PathVariable final String templateName,
                                                                    @PathVariable final Integer templateVersion)
    throws IOException {

    log.debug("REST request to get docx file for template {} version {}", templateName, templateVersion);

    final InputStream inputStream = templateQueryRepository.getTemplateFile(TemplateName.from(templateName),
      templateVersion);

    return ResponseEntity.ok()
      .header("Content-Disposition", "attachment; filename=" + templateName + templateVersion + DOCX)
      .contentLength(inputStream.available())
      .contentType(MediaType.APPLICATION_OCTET_STREAM)
      .body(new InputStreamResource(inputStream));
  }
}
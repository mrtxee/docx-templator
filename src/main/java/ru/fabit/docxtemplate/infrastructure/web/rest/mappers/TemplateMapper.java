package ru.fabit.docxtemplate.infrastructure.web.rest.mappers;

import org.mapstruct.Mapper;
import ru.fabit.docxtemplate.domain.templates.models.Template;
import ru.fabit.docxtemplate.infrastructure.persistence.store.models.TemplateStore;
import ru.fabit.docxtemplate.infrastructure.web.rest.dto.response.TemplateResponse;
import java.util.List;

@Mapper(componentModel = "spring")
public interface TemplateMapper {

  List<TemplateResponse> toResponse(final List<TemplateStore> templates);

  TemplateResponse toResponse(final TemplateStore template);

  TemplateResponse toResponse(final Template template);
}
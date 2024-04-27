package ru.fabit.docxtemplate.infrastructure.persistence.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.Expressions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IterableUtils;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import ru.fabit.docxtemplate.api.templates.exceptions.TemplateNotFoundException;
import ru.fabit.docxtemplate.domain.templates.models.TemplateName;
import ru.fabit.docxtemplate.infrastructure.persistence.store.models.QTemplateStore;
import ru.fabit.docxtemplate.infrastructure.persistence.store.models.TemplateStore;
import ru.fabit.docxtemplate.infrastructure.persistence.store.repositories.FilePersistenceRepository;
import ru.fabit.docxtemplate.infrastructure.persistence.store.repositories.TemplatePersistenceRepository;
import ru.fabit.docxtemplate.infrastructure.web.rest.dto.request.TemplateFilters;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TemplateQueryRepository {

  private final FilePersistenceRepository fileRepository;
  private final TemplatePersistenceRepository persistenceRepository;

  public TemplateStore findTemplateByName(final TemplateName templateName) {

    return persistenceRepository
      .findFirstByNameAndLastVersionIsTrue(templateName.toString())
      .orElseThrow(() -> new TemplateNotFoundException("Not found template with name = " + templateName));
  }

  public TemplateStore findTemplateByNameAndVersion(final TemplateName templateName, final Integer templateVersion) {

    return persistenceRepository
      .findFirstByNameAndVersion(templateName.toString(), templateVersion)
      .orElseThrow(() -> new TemplateNotFoundException(
        "Not found template with name = " + templateName + " and version = " + templateVersion
      ));
  }

  public InputStream getTemplateFile(final TemplateName templateName, final Integer templateVersion) {

    final Long fileId = persistenceRepository
      .findFirstByNameAndVersion(templateName.toString(), templateVersion)
      .orElseThrow(() -> new TemplateNotFoundException(
        "Not found template with name = " + templateName + " and version = " + templateVersion
      ))
      .getFileId();

    return new ByteArrayInputStream(fileRepository.findById(fileId).get().getData());
  }

  public List<TemplateStore> findTemplates(final TemplateFilters filters) {

    final Iterable<TemplateStore> templateStores = persistenceRepository
      .findAll(buildPredicate(filters), Sort.by(Sort.Direction.DESC, "createdDate"));

    return IterableUtils.toList(templateStores);
  }

  private Predicate buildPredicate(final TemplateFilters filters) {

    final QTemplateStore template = QTemplateStore.templateStore;
    final BooleanBuilder where = new BooleanBuilder();

    if (filters == null) {
      return where;
    }

    if (!CollectionUtils.isEmpty(filters.getTags())) {
      //with help of igor glumov and https://github.com/vitamaxDH/postgres-querydsl-array-fuction-demo/blob/main/src/main/kotlin/io/vitamax/graphqlfunction/domain/StudentRepositoryImpl.kt
      where.and(Expressions.booleanTemplate(
        "ARRAYOVERLAP({0}, {1}) = true",
        template.templateTags,
        filters.getTags().toArray(String[]::new)
      ));
    }
    if (filters.getIsLastVersion() != null) {
      where.and(template.lastVersion.eq(filters.getIsLastVersion()));
    }

    return where;
  }
}
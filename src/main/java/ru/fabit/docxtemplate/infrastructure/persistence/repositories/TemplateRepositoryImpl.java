package ru.fabit.docxtemplate.infrastructure.persistence.repositories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.fabit.docxtemplate.api.templates.repositories.TemplateRepository;
import ru.fabit.docxtemplate.domain.templates.models.FileId;
import ru.fabit.docxtemplate.domain.templates.models.Template;
import ru.fabit.docxtemplate.domain.templates.models.TemplateId;
import ru.fabit.docxtemplate.infrastructure.persistence.store.models.TemplateStore;
import ru.fabit.docxtemplate.infrastructure.persistence.store.repositories.TemplatePersistenceRepository;
import java.util.Arrays;

@Slf4j
@Repository
@RequiredArgsConstructor
public class TemplateRepositoryImpl implements TemplateRepository {

  private final TemplatePersistenceRepository templatePersistenceRepository;

  @Override
  @Transactional
  public void save(final Template template) {

    templatePersistenceRepository.makeActualTemplateAsInactive(template.getName());

    final TemplateStore templateStore = mapToStore(template);

    templatePersistenceRepository.save(templateStore);
  }

  private TemplateStore mapToStore(final Template template) {

    return new TemplateStore(
      template.getId().id(), template.getName(), template.getVersion(), template.isLastVersion(),
      template.getTemplateTags().toArray(String[]::new), template.getFileId().id(), template.getCreatedDate()
    );
  }

  @Override
  public Template findByName(final String name) {

    return templatePersistenceRepository
      .findFirstByNameAndLastVersionIsTrue(name)
      .map(this::mapToDomain)
      .orElse(null);
  }

  @Override
  public Template findByNameAndVersion(final String name, final int version) {

    return templatePersistenceRepository
      .findFirstByNameAndVersion(name, version)
      .map(this::mapToDomain)
      .orElse(null);
  }

  private Template mapToDomain(final TemplateStore template) {

    return Template.createFromRepository(
      new TemplateId(template.getId()), template.getName(), template.getVersion(), template.isLastVersion(),
      Arrays.asList(template.getTemplateTags()), new FileId(template.getFileId()), template.getCreatedDate()
    );
  }

  @Override
  public int findLastVersionByName(final String name) {

    return templatePersistenceRepository
      .findFirstByNameAndLastVersionIsTrue(name)
      .map(TemplateStore::getVersion)
      .orElse(0);
  }
}
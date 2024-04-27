package ru.fabit.docxtemplate.api.templates.repositories;

import ru.fabit.docxtemplate.domain.templates.models.Template;

public interface TemplateRepository {

  void save(final Template template);

  Template findByName(final String name);

  Template findByNameAndVersion(final String name, final int version);

  /** Возвращает 0, если шаблона с таким именем нет */
  int findLastVersionByName(final String name);
}
package ru.fabit.docxtemplate.domain.templates.models;

import lombok.Getter;

import static ru.fabit.docxtemplate.domain.templates.models.validators.Validators.requireNotEmptyAndNull;

@Getter
public class TemplateName {
  private final String name;

  private TemplateName(String name) {
    requireNotEmptyAndNull(name, "Template name field must not be empty or null");
    this.name = name;
  }

  public static TemplateName from(String name) {
    return new TemplateName(name);
  }

  @Override
  public String toString() {
    return String.valueOf(name);
  }
}

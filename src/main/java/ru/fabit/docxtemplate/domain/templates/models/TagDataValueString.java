package ru.fabit.docxtemplate.domain.templates.models;

import static ru.fabit.docxtemplate.domain.templates.models.validators.Validators.requireNotNull;

public class TagDataValueString extends TagDataValue {

  private final String value;

  TagDataValueString(String name, String value) {
    super(name);
    requireNotNull(value, "templateTags[].value must not null. templateTags[].name=%s".formatted(name));
    this.value = value;
  }

  public String getValue() {
    return value;
  }
}
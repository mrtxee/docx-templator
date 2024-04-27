package ru.fabit.docxtemplate.domain.templates.models;

import static ru.fabit.docxtemplate.domain.templates.models.validators.Validators.requireNotEmptyAndNull;
import static ru.fabit.docxtemplate.domain.templates.models.validators.Validators.requireNotNull;

public abstract class TagDataValue {

  private final String name;

  TagDataValue(String name) {
    requireNotEmptyAndNull(name, "templateTags[].name must not be empty or null");
    this.name = name;
  }

  public static TagDataValue from(String name, String value) {
    if (isPictureTag(name)) {
      return new TagDataValuePicture(name, value);
    }
    return new TagDataValueString(name, value);
  }

  public static TagDataValue from(TagDataSupplier data) {
    return from(data.getName(), data.getValue());
  }

  public String getName() {
    return name;
  }

  public abstract Object getValue();

  private static boolean isPictureTag(String tagName){
    return tagName.startsWith("@");
  }
}
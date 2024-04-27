package ru.fabit.docxtemplate.domain.templates.models;

import java.util.HashMap;
import java.util.List;

import static ru.fabit.docxtemplate.domain.templates.models.validators.Validators.requireNotEmptyAndNull;

public class TemplateSubstitutionValues {
  List<TagDataValue> tags;

  private TemplateSubstitutionValues(List<TagDataValue> tags) {
    this.tags = tags;
  }

  public static TemplateSubstitutionValues from(List<TagDataValue> tags) {
    requireNotEmptyAndNull(tags, "tags data must be not empty or null");
    return new TemplateSubstitutionValues(tags);
  }

  public static TemplateSubstitutionValues fromTagDataSupplier(List<? extends TagDataSupplier> tags) {
    return from(tags.stream().map(TagDataValue::from).toList());
  }

  public final HashMap<String, Object> asHashMap() {
    HashMap<String, Object> map = new HashMap<>();
    this.tags.forEach(t -> map.put(t.getName(), t.getValue()));
    return map;
  }
}

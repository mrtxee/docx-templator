package ru.fabit.docxtemplate.domain.templates.models;

import lombok.Getter;
import lombok.ToString;
import java.time.Instant;
import java.util.List;

import static ru.fabit.docxtemplate.domain.templates.models.validators.Validators.requireNotEmptyAndNull;

@Getter
@ToString
public class Template {

  private final TemplateId id;

  private final TemplateName name;

  private final int version;

  private final boolean lastVersion;

  private final List<String> templateTags;

  private final FileId fileId;

  private final Instant createdDate;

  public String getName() {
    return name.getName();
  }

  private Template(final TemplateId id, final TemplateName name, final int version, final boolean lastVersion,
                   final List<String> templateTags, final FileId fileId, final Instant createdDate) {

    this.id = id;
    this.name = name;
    this.version = version;
    this.lastVersion = lastVersion;
    this.templateTags = templateTags;
    this.fileId = fileId;
    this.createdDate = createdDate;
  }

  public static Template create(final TemplateId id, final String name, final int lastExistVersion,
                                final List<String> templateTags, final FileId fileId) {
    createValidate(name, templateTags);
    return new Template(id, TemplateName.from(name), lastExistVersion + 1, true,
      templateTags, fileId, Instant.now());
  }

  public static void createValidate(final String name, final List<String> templateTags) {
    TemplateName.from(name);
    requireNotEmptyAndNull(templateTags, "templateTags filed must be not empty or null");
  }

  public static Template createFromRepository(final TemplateId id, final String name, final int version,
                                              final boolean lastVersion, final List<String> templateTags,
                                              final FileId fileId, final Instant createdDate) {

    return new Template(id, TemplateName.from(name), version, lastVersion, templateTags, fileId, createdDate);
  }
}
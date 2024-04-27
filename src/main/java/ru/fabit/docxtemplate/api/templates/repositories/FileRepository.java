package ru.fabit.docxtemplate.api.templates.repositories;

import ru.fabit.docxtemplate.domain.templates.models.FileId;
import ru.fabit.docxtemplate.domain.templates.models.TemplateFile;

public interface FileRepository {

  FileId save(final byte[] fileData);

  TemplateFile findById(final FileId id);
}
package ru.fabit.docxtemplate.infrastructure.persistence.repositories;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import ru.fabit.docxtemplate.api.templates.exceptions.FileNotFoundException;
import ru.fabit.docxtemplate.api.templates.repositories.FileRepository;
import ru.fabit.docxtemplate.domain.templates.models.FileId;
import ru.fabit.docxtemplate.domain.templates.models.TemplateFile;
import ru.fabit.docxtemplate.infrastructure.persistence.store.models.FileStore;
import ru.fabit.docxtemplate.infrastructure.persistence.store.repositories.FilePersistenceRepository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class FileRepositoryImpl implements FileRepository {

  private final FilePersistenceRepository filePersistenceRepository;

  @Override
  public FileId save(final byte[] fileData) {

    final FileStore fileStore = FileStore.builder().data(fileData).build();

    return new FileId(filePersistenceRepository.save(fileStore).getId());
  }

  @Override
  public TemplateFile findById(final FileId id) {

    final FileStore fileStore = filePersistenceRepository
      .findById(id.id())
      .orElseThrow(() -> new FileNotFoundException("Not found file with id = " + id));

    return TemplateFile.createFromRepository(id, fileStore.getData(), fileStore.getCreatedDate());
  }
}
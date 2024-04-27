package ru.fabit.docxtemplate.infrastructure.persistence.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.fabit.docxtemplate.infrastructure.persistence.store.models.FileStore;

@Repository
public interface FilePersistenceRepository
  extends JpaRepository<FileStore, Long>, QuerydslPredicateExecutor<FileStore> {
}
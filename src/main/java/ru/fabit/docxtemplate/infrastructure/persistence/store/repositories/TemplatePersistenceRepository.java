package ru.fabit.docxtemplate.infrastructure.persistence.store.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;
import ru.fabit.docxtemplate.infrastructure.persistence.store.models.TemplateStore;
import java.util.Optional;

@Repository
public interface TemplatePersistenceRepository
        extends JpaRepository<TemplateStore, Long>, QuerydslPredicateExecutor<TemplateStore> {

  Optional<TemplateStore> findFirstByNameAndVersion(final String name, final int version);

  Optional<TemplateStore> findFirstByNameAndLastVersionIsTrue(final String name);

  Optional<TemplateStore> findFirstByNameOrderByCreatedDateDesc(final String name);

  @Modifying
  @Query(
    nativeQuery = true, value = "UPDATE templates SET last_version = false WHERE name = ?1 AND last_version is true"
  )
  void makeActualTemplateAsInactive(final String name);
}
package ru.fabit.docxtemplate.domain.templates.models;

public record FileId(Long id) {

  @Override
  public String toString() {
    return String.valueOf(id);
  }
}
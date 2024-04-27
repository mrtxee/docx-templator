package ru.fabit.docxtemplate.api.templates.exceptions;

public class TemplateNotFoundException extends RuntimeException{
  public TemplateNotFoundException(final String message) {
    super(message);
  }
}
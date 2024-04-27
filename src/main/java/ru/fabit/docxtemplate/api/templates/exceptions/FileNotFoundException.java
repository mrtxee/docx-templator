package ru.fabit.docxtemplate.api.templates.exceptions;

public class FileNotFoundException extends RuntimeException{
  public FileNotFoundException(final String message) {
    super(message);
  }
}
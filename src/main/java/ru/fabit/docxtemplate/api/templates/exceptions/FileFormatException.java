package ru.fabit.docxtemplate.api.templates.exceptions;

public class FileFormatException extends IllegalArgumentException {

  public FileFormatException(final String message) {
    super(message);
  }
}
package ru.fabit.docxtemplate.api.templates.usecases;

import ru.fabit.docxtemplate.api.templates.exceptions.FileFormatException;

public interface DocxFormatChecker {
  void assertFileDocXFormat(byte[] fileBytes) throws FileFormatException;
}

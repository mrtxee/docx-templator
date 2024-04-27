package ru.fabit.docxtemplate.infrastructure.formatchecker;

import lombok.RequiredArgsConstructor;
import org.apache.poi.poifs.filesystem.FileMagic;
import org.springframework.stereotype.Component;
import ru.fabit.docxtemplate.api.templates.exceptions.FileFormatException;
import ru.fabit.docxtemplate.api.templates.usecases.DocxFormatChecker;

@Component
@RequiredArgsConstructor
public class PoiOoxmlFormatChecker implements DocxFormatChecker {
  @Override
  public void assertFileDocXFormat(byte[] fileBytes) throws FileFormatException {
    if (fileBytes.length > 1 && FileMagic.valueOf(fileBytes) != (FileMagic.OOXML)) {
      throw new FileFormatException("Template file must be docx format");
    }
  }
}

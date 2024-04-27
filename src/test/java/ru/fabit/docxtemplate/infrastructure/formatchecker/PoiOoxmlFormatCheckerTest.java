package ru.fabit.docxtemplate.infrastructure.formatchecker;

import org.junit.jupiter.api.Test;
import org.springframework.util.ResourceUtils;
import ru.fabit.docxtemplate.api.templates.exceptions.FileFormatException;
import ru.fabit.docxtemplate.api.templates.usecases.DocxFormatChecker;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class PoiOoxmlFormatCheckerTest {

  @Test
  void validDocXFormatTemplateFileNotThrowsException() throws IOException {
    File validDocxFile = ResourceUtils.getFile("classpath:test-templates/small.docx");
    byte[] validDocxFileBytes = Files.readAllBytes(validDocxFile.toPath());

    DocxFormatChecker sut = new PoiOoxmlFormatChecker();

    sut.assertFileDocXFormat(validDocxFileBytes);

  }

  @Test
  void notDocXFormatTemplateFileThrowsException() throws IOException {
    File notDocxFile = ResourceUtils.getFile("classpath:test-templates/not_docx_file.docx");
    byte[] notDocxFileBytes = Files.readAllBytes(notDocxFile.toPath());

    DocxFormatChecker sut = new PoiOoxmlFormatChecker();

    assertThatExceptionOfType(FileFormatException.class)
      .isThrownBy(() -> {
        sut.assertFileDocXFormat(notDocxFileBytes);
      }).withMessage("Template file must be docx format");

  }

}
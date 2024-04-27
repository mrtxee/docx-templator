package ru.fabit.docxtemplate.domain.templates.models;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class TemplateNameTest {

  @ParameterizedTest
  @NullSource
  @EmptySource
  void notValidTemplateNameTrowsException(String name) {
    assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> {
        TemplateName.from(name);
      }).withMessage("Template name field must not be empty or null");
  }
}
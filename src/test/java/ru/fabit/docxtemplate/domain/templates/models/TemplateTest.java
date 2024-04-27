package ru.fabit.docxtemplate.domain.templates.models;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullSource;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class TemplateTest {

  @ParameterizedTest
  @NullSource
  @EmptySource
  void notValidTemplateNameTrowsException(String name) {
    assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> {
        Template.create(new TemplateId(1L), name, 1, List.of("Communal"), new FileId(123L));
      }).withMessage("Template name field must not be empty or null");
  }

  @ParameterizedTest
  @NullSource
  @EmptySource
  void validationOfNotValidTemplateNameTrowsException(String name) {
    assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> {
        Template.createValidate( name, List.of("Communal"));
      }).withMessage("Template name field must not be empty or null");
  }

  @Test
  void validationOfNullListOfTagsTrowsException() {
    assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> {
        Template.createValidate("name", null);
      }).withMessage("templateTags filed must be not empty or null");
  }

  @Test
  void validationOfEmptyListOfTagsTrowsException() {
    assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> {
        Template.createValidate("name", List.of());
      }).withMessage("templateTags filed must be not empty or null");
  }

  @Test
  void nullListOfTagsTrowsException() {
    assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> {
        Template.create(new TemplateId(1L), "name", 1, null, new FileId(123L));
      }).withMessage("templateTags filed must be not empty or null");
  }

  @Test
  void emptyListOfTagsTrowsException() {
    assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> {
        Template.create(new TemplateId(1L), "name", 1, List.of(), new FileId(123L));
      }).withMessage("templateTags filed must be not empty or null");
  }
}
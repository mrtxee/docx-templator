package ru.fabit.docxtemplate.domain.templates.models;

import org.junit.jupiter.api.Test;
import ru.fabit.docxtemplate.infrastructure.web.rest.dto.request.CreatePdfRequest;
import ru.fabit.docxtemplate.infrastructure.web.rest.dto.request.TagData;

import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class TemplateSubstitutionValuesTest {

  @Test
  void validTagsListSuccessfullyConvertsToHashmap() {
    HashMap<String, Object> actual = TemplateSubstitutionValues.fromTagDataSupplier(
      CreatePdfRequest.builder()
      .data(List.of(
        TagData.builder().name("ДатаДокумента").value("1900.01.01").build(),
        TagData.builder().name("ФИО").value("Майор громов").build()
      )).build().getData()
    ).asHashMap();

    HashMap<String, String> expected = new HashMap<>();
    expected.put("ДатаДокумента", "1900.01.01");
    expected.put("ФИО", "Майор громов");

    assertThat(actual).isEqualTo(expected);
  }

  @Test
  void emptyTagsListThrowsException() {

    assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> {
        TemplateSubstitutionValues.fromTagDataSupplier(
          CreatePdfRequest.builder()
            .data(List.of()).build().getData()
        ).asHashMap();
      }).withMessage("tags data must be not empty or null");
  }

  @Test
  void emptyTagNameThrowsException() {

    assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> {
        TemplateSubstitutionValues.fromTagDataSupplier(
          CreatePdfRequest.builder()
            .data(List.of(TagData.builder().name("").value("1900.01.01").build()))
            .build().getData()
        ).asHashMap();
      }).withMessage("templateTags[].name must not be empty or null");
  }

  @Test
  void nullTagValueThrowsException() {

    assertThatExceptionOfType(IllegalArgumentException.class)
      .isThrownBy(() -> {
        TemplateSubstitutionValues.fromTagDataSupplier(
          CreatePdfRequest.builder()
            .data(List.of(TagData.builder().name("ДатаДокумента").value(null).build()))
            .build().getData()
        ).asHashMap();
      }).withMessage("templateTags[].value must not null. templateTags[].name=ДатаДокумента");
  }

}
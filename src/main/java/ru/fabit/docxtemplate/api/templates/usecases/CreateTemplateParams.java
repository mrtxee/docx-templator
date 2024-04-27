package ru.fabit.docxtemplate.api.templates.usecases;

public interface CreateTemplateParams {
  String getName();

  java.util.List<String> getTemplateTags();

  String getTemplateFile();
}

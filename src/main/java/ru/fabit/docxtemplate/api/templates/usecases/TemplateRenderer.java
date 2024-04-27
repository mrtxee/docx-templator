package ru.fabit.docxtemplate.api.templates.usecases;

import ru.fabit.docxtemplate.domain.templates.models.TemplateSubstitutionValues;
import java.io.IOException;
import java.io.InputStream;

public interface TemplateRenderer {

  /**
   * Ренедерит шаблон docx как pdf
   * @param docxTemplate docx-шаблон
   * @param values значения для подстановки в шаблон
   * @param cacheKey ключ кеширования шаблона. FileId или имя шаблона + версия.
   *                 В случае если передан тот же ключ берется закешированный файл шаблона.
   * @return PDF-файл
   */
  InputStream renderAsPdf(InputStream docxTemplate,
                          TemplateSubstitutionValues values,
                          String cacheKey) throws IOException;
}

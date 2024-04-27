package ru.fabit.docxtemplate;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class TemplateServiceRunner {

  private final PdfGenerateService pdfGenerateService;

  public void run(final String... args) {

    final Map<String, String> data = new HashMap<>();

    data.put("{ФИОНарушителя}", "Иванов Иван Иванович");
    data.put("{НазваниеОрганизации}", "ООО «Управляйка»");
    data.put("{ДатаНарушения}", "17.11.2022");
    data.put("{ВремяНарушения}", "09:56");
    data.put("{АдресКомиссии}", "г. Тула ул.Ленина д.34");

    pdfGenerateService.generate(data);
  }
}

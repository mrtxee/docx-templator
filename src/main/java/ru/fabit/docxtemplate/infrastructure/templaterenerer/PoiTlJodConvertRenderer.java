package ru.fabit.docxtemplate.infrastructure.templaterenerer;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import org.jodconverter.core.document.DocumentFamily;
import org.jodconverter.core.document.DocumentFormat;
import org.jodconverter.local.JodConverter;
import org.springframework.stereotype.Component;
import ru.fabit.docxtemplate.api.templates.usecases.TemplateRenderer;
import ru.fabit.docxtemplate.domain.templates.models.TemplateSubstitutionValues;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Instant;
import java.util.Map;

@Component
public class PoiTlJodConvertRenderer implements TemplateRenderer {

  private static final String PDF = ".pdf";
  private static final String DOCX = ".docx";
  // Нахождение тегов в документе осуществляется по регэкспу, который по умолчанию находит теги
  // на английском и китайском языках, поэтому необходимо передавать другую маску, с кириллическими символами
  private static final String GRAMER_REGEX = "((#)?[\\w\\u0400-\\u044f]+(\\.[\\w\\u0400-\\u044f]+)*)?";

  @Override
  public InputStream renderAsPdf(InputStream docxTemplate, TemplateSubstitutionValues values, String cacheKey)
    throws IOException {
    final File outFile = File.createTempFile("replaced-docx", DOCX);

    XWPFTemplate
      .compile(docxTemplate, Configure.builder().buildGrammerRegex(GRAMER_REGEX).build())
      .render(values.asHashMap())
      .writeAndClose(new FileOutputStream(outFile));

    return createPdfByJodConverter(outFile);
  }

  private InputStream createPdfByJodConverter(final File docxFile) {
    try {

      final File outputFile = File.createTempFile(String.valueOf(Instant.now().toEpochMilli()), PDF);
      JodConverter.convert(docxFile).to(outputFile).as(getDocumentFormatPDFA14()).execute();

      return new FileInputStream(outputFile);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private DocumentFormat getDocumentFormatPDFA14() {
    final int PDF1_4 = 1;
    final Map<String, Integer> pdfOptions = Map.of("SelectPdfVersion", PDF1_4);

    return DocumentFormat.builder()
      .name("pdf_1_4")
      .inputFamily(DocumentFamily.TEXT)
      .extension("pdf")
      .mediaType("pdf")
      .storeProperty(DocumentFamily.TEXT, "FilterData", pdfOptions)
      .storeProperty(DocumentFamily.TEXT, "FilterName", "writer_pdf_Export")
      .unmodifiable(false)
      .build();
  }
}

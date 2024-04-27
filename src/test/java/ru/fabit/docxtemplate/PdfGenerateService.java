package ru.fabit.docxtemplate;

import com.google.common.base.Stopwatch;
//import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
//import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
//import org.docx4j.Docx4J;
//import org.docx4j.fonts.BestMatchingMapper;
//import org.docx4j.openpackaging.packages.WordprocessingMLPackage;
import org.jodconverter.core.office.OfficeManager;
import org.jodconverter.core.office.OfficeUtils;
import org.jodconverter.local.JodConverter;
import org.jodconverter.local.office.LocalOfficeManager;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
@RequiredArgsConstructor
public class PdfGenerateService {

  public static final String VAR_REGEX = "\\{[А-Яа-я]*\\}";
  Pattern varPattern = Pattern.compile(VAR_REGEX);

  //  @Value("${app.pdf.directory}")
  private String pdfDirectory;
  private final Stopwatch stopwatch = Stopwatch.createUnstarted();

  public void generate(final Map<String, String> data) {

    stopwatch.start();

    try (FileInputStream fis = new FileInputStream(ResourceUtils.getFile("classpath:medium.docx"))) {

      final XWPFDocument document = new XWPFDocument(fis);

      log.info("!!docx was read for: {}", stopwatch.stop());

      replacePlaceholders(data, document);

//      createPdfByXDocReport(document);

      final File tempDocxFile = File.createTempFile(UUID.randomUUID().toString(), ".docx");
      try (FileOutputStream fos = new FileOutputStream(tempDocxFile)) {
        document.write(fos);
        document.close();
      } catch (IOException e) {
        throw new RuntimeException(e);
      }

//      createPdfBySpire(tempDocxFile);
//      createPdfByDoc4j(tempDocxFile);
      createPdfByJodConverter(tempDocxFile);

    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void replacePlaceholders(final Map<String, String> data, final XWPFDocument document) {
    stopwatch.start();

    document.getHeaderList().forEach(h -> replaceVars(data, h.getParagraphs()));
    document.getFooterList().forEach(f -> replaceVars(data, f.getParagraphs()));
    replaceVars(data, document.getParagraphs());
    document.getTables().forEach(table ->
      table.getRows().forEach(row ->
        row.getTableCells().forEach(cell -> replaceVars(data, cell.getParagraphs()))
      )
    );

    log.info("!!docx was parsed for: {}", stopwatch.stop());
  }

  private void replaceVars(final Map<String, String> data, final List<XWPFParagraph> paragraphs) {
    paragraphs.forEach(p -> p.getRuns().forEach(r -> replaceVarsInRun(data, r)));
  }

  private void replaceVarsInRun(final Map<String, String> data, final XWPFRun r) {

    if (r.getText(0) != null) {

      final Matcher matcher = varPattern.matcher(r.getText(0));
      Set<String> vars = new HashSet<>();
      while (matcher.find()) {
        vars.add(matcher.group());
      }
      if (!vars.isEmpty()) {
        final String result = vars.stream().reduce(r.getText(0), (text, var) -> text.replace(var, data.get(var)));
        r.setText(result, 0);
      }
    }
  }

  private void createPdfByXDocReport(final XWPFDocument document) {

    stopwatch.start();

    try (OutputStream outputStream = new FileOutputStream(pdfDirectory + Instant.now().toEpochMilli() + ".pdf")) {
//      PdfOptions options = PdfOptions.create();
      // Convert .docx file to .pdf file
//      PdfConverter.getInstance().convert(document, outputStream, options);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }

    log.info("!!pdf was transformed for: {}", stopwatch.stop());
  }

//  private void createPdfBySpire(final File tempDocxFile) {
//
//    stopwatch.start();
//
//    try (final InputStream is = Files.newInputStream(
//        Paths.get(tempDocxFile.getAbsolutePath()), StandardOpenOption.DELETE_ON_CLOSE
//    )) {
//      new Document(is).saveToFile(pdfDirectory + Instant.now().toEpochMilli() + ".pdf", FileFormat.PDF);
//    } catch (IOException e) {
//      throw new RuntimeException(e);
//    }
//
//    log.info("!!pdf was transformed for: {}", stopwatch.stop());
//  }

  private void createPdfByDoc4j(final File tempDocxFile) {

    stopwatch.start();

    try {

//      final WordprocessingMLPackage docxPkg = Docx4J.load(tempDocxFile);

//      docxPkg.setFontMapper(new BestMatchingMapper());

//      try (OutputStream outputStream = new FileOutputStream(pdfDirectory + Instant.now().toEpochMilli() + ".pdf")) {
//        Docx4J.toPDF(docxPkg, outputStream);
//      }

    } catch (Exception e) {
      throw new RuntimeException(e);
    }

    log.info("!!pdf was transformed for: {}", stopwatch.stop());
  }

  private void createPdfByJodConverter(final File tempDocxFile) {

    OfficeManager officeManager = null;

    try {
      officeManager = LocalOfficeManager.builder().install().officeHome(new File("/usr/lib/libreoffice")).build();
      officeManager.start();
      stopwatch.start();

      final File outputFile = new File(pdfDirectory + Instant.now().toEpochMilli() + ".pdf");
      JodConverter.convert(tempDocxFile).to(outputFile).execute();
      log.info("!!pdf was transformed for: {}", stopwatch.stop());

    } catch (Exception e) {
      throw new RuntimeException(e);
    } finally {
      // 4) Stop LibreOffice in headless mode.
      if (officeManager != null) {
        OfficeUtils.stopQuietly(officeManager);
      }
    }
  }
}

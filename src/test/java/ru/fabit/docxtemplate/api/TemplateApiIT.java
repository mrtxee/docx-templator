package ru.fabit.docxtemplate.api;

import static com.codeborne.pdftest.assertj.Assertions.assertThat;
import static io.restassured.http.ContentType.JSON;
import static org.hamcrest.Matchers.blankOrNullString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.not;
import static ru.fabit.docxtemplate.api.requests.TemplateRequestMother.createTemplateRequest;

import com.codeborne.pdftest.PDF;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.Timeout;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.util.ResourceUtils;
import org.testcontainers.shaded.com.google.common.collect.ImmutableList;
import ru.fabit.docxtemplate.api.requests.CreatePdfRequest;
import ru.fabit.docxtemplate.api.requests.TemplateData;
import ru.fabit.docxtemplate.integrationtest.IntegrationTest;

@IntegrationTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Slf4j
public class TemplateApiIT {

  @LocalServerPort
  private int localServerPort = 8099;

  RequestSpecification requestToServer;

  @BeforeEach
  public void setup() {
    requestToServer = RestAssured
      .given()
      .and().port(localServerPort)
      .accept(JSON);
  }

  @Test
  @DisplayName("GET /api/templates returns empty list when no templates")
  @Order(1)
  public void templates_empty() {
    RestAssured
      .given(requestToServer)
      .when()
      .get("/api/templates")
      .then()
      .log().ifValidationFails()
      .and()
      .assertThat()
      .statusCode(200)
      .contentType(JSON)
      .body("$.size()", equalTo(0));
  }

  @Test
  @DisplayName("GET /api/templates/not_exist should return 404")
  @Order(2)
  public void template_not_exists_404() {
    RestAssured
      .given(requestToServer)
      .when()
      .get("/api/templates/not_exist")
      .then()
      .log().ifValidationFails()
      .and()
      .assertThat()
      .statusCode(404)
      .contentType(JSON)
      .body("type", equalTo("urn:problem-type:not-found"))
      .body("title", equalTo("Not Found"))
      .body("detail", equalTo("Not found template with name = not_exist"))
      .body("instance", not(blankOrNullString()));
  }

  @Test
  @DisplayName("POST /api/templates should create template")
  @Order(3)
  public void template_create() throws IOException {
    RestAssured
      .given(createTemplateRequest(requestToServer, "lawnReclamation",
        ImmutableList.of("АП", "lawn", "газон"), "medium.docx"))
      .when()
      .post("/api/templates")
      .then()
      .log().ifValidationFails()
      .and()
      .assertThat()
      .statusCode(201)
      .contentType(JSON)
      .body("name", equalTo("lawnReclamation"))
      .body("templateTags", hasItems("АП", "lawn", "газон"));
  }

  @Test
  @DisplayName("GET /api/templates/{templateName}/{templateVersion}/file should return uploaded template")
  @Order(4)
  public void get_template_file() throws IOException {
    //arrange
    String uploadedTemplateName = "lawnReclamation";
    String uploadedTemplateFile = "medium.docx";
    String uploadedVersion = RestAssured
      .given(createTemplateRequest(requestToServer, uploadedTemplateName,
        ImmutableList.of("АП", "lawn", "газон"), uploadedTemplateFile))
      .when()
      .post("/api/templates")
      .then()
      .log().ifValidationFails()
      .and()
      .assertThat()
      .statusCode(201)
      .contentType(JSON)
      .body("name", equalTo("lawnReclamation"))
      .body("templateTags", hasItems("АП", "lawn", "газон"))
      .extract()
      .path("version").toString();
    //act
    byte[] savedFile =
      RestAssured
        .given(requestToServer)
        .when()
        .pathParam("templateName", uploadedTemplateName)
        .pathParam("templateVersion", uploadedVersion)
        .get("/api/templates/{templateName}/{templateVersion}/file")
        .then()
        .log().ifValidationFails()
        .and()
        .assertThat()
        .statusCode(200)
        .contentType("application/octet-stream")
        .extract()
        .body()
        .asByteArray();
    //assert
    File file = ResourceUtils.getFile("classpath:test-templates/" + uploadedTemplateFile);
    assertThat(savedFile).isEqualTo(Files.readAllBytes(file.toPath()));
  }

  @Test
  @DisplayName("GET /api/templates should filter templates by tags when filter tags overlaps with template tags")
  @Order(6)
  public void template_filter_by_tags() throws IOException {
    //create template
    RestAssured
      .given(createTemplateRequest(requestToServer, "communalReclamation",
        ImmutableList.of("communal"), "small.docx"))
      .when()
      .post("/api/templates")
      .then()
      .log().ifValidationFails()
      .and()
      .assertThat()
      .statusCode(201);
    //
    RestAssured
      .given(requestToServer)
      .queryParam("tags", List.of("communal", "unknown"))
      .when()
      .get("/api/templates")
      .then()
      .log().ifValidationFails()
      .and()
      .assertThat()
      .statusCode(200)
      .contentType(JSON)
      .body("$.size()", equalTo(1))
      .body("[0].name", equalTo("communalReclamation"));

    RestAssured
      .given(requestToServer)
      .queryParam("tags", List.of("unknown"))
      .when()
      .get("/api/templates")
      .then()
      .log().ifValidationFails()
      .and()
      .assertThat()
      .statusCode(200)
      .contentType(JSON)
      .body("$.size()", equalTo(0));
  }

  @Test
  @DisplayName("POST /api/templates/{templateName}/pdf should generate pdf 1.4 with replaced tags")
  @Order(7)
  public void template_generate_pdf() throws IOException {

    String templateName = "testTemplate";
    //arrange
    RestAssured
      .given(createTemplateRequest(requestToServer, templateName,
        ImmutableList.of("communal"), "small.docx"))
      .when()
      .post("/api/templates")
      .then()
      .log().ifValidationFails()
      .and()
      .assertThat()
      .statusCode(201);
    //act
    String replaceAddress = "планета Марс, город Максим";
    String replaceDate = "01.01.2040";
    String replaceFio = "IVAN";
    CreatePdfRequest createPdfRequest = CreatePdfRequest.builder()
      .data(Arrays.asList(
        TemplateData.builder().name("АдресКомиссии").value(replaceAddress).build(),
        TemplateData.builder().name("ДатаНарушения").value(replaceDate).build(),
        TemplateData.builder().name("ФИОНарушителя").value(replaceFio).build())
      )
      .build();
    byte[] generatedPdf = RestAssured
      .given(requestToServer)
      .contentType(JSON)
      .pathParam("templateName", templateName)
      .body(createPdfRequest)
      .when()
      .post("/api/templates/{templateName}/pdf")
      .then()
      .log().ifValidationFails()
      .and()
      .assertThat()
      .statusCode(200)
      .contentType("application/pdf")
      .extract()
      .body()
      .asByteArray();
    //assert
    String generatedPdfAsStr = new String(generatedPdf);
    assertThat(generatedPdfAsStr.startsWith("%PDF-1.4")).isTrue();

    PDF pdf = new PDF(generatedPdf);
    assertThat(pdf).containsExactText(replaceAddress);
    assertThat(pdf).containsExactText(replaceDate);
    assertThat(pdf).containsExactText(replaceFio);
    assertThat(pdf).doesNotContainExactText("{{ВремяНарушения}}");

    Path tempPath = Files.createTempFile("output", ".pdf");
    FileOutputStream fos = new FileOutputStream(tempPath.toFile());
    fos.write(generatedPdf, 0, generatedPdf.length);
    fos.flush();
    fos.close();

    log.info("View pdf by eyes: " + tempPath);
  }

  @Test
  @Timeout(1)
  @DisplayName("POST /api/templates/{templateName}/pdf works fast")
  @Order(8)
  @Disabled("Not works fast. Optimize in future")
  public void template_generate_pdf_works_fast() throws IOException {

    String templateName = "testLargeTemplate";
    //arrange
    RestAssured
      .given(createTemplateRequest(requestToServer, templateName,
        ImmutableList.of("communal"), "large.docx"))
      .when()
      .post("/api/templates")
      .then()
      .log().ifValidationFails()
      .and()
      .assertThat()
      .statusCode(201);
    //act
    String replaceAddress = "планета Марс, город Максим";
    String replaceDate = "01.01.2040";
    String replaceFio = "IVAN";
    CreatePdfRequest createPdfRequest = CreatePdfRequest.builder()
      .data(Arrays.asList(
        TemplateData.builder().name("АдресКомиссии").value(replaceAddress).build(),
        TemplateData.builder().name("ДатаНарушения").value(replaceDate).build(),
        TemplateData.builder().name("ФИОНарушителя").value(replaceFio).build())
      )
      .build();

    for (int i = 0; i < 5; i++) {
      RestAssured
        .given(requestToServer)
        .contentType(JSON)
        .pathParam("templateName", templateName)
        .body(createPdfRequest)
        .when()
        .post("/api/templates/{templateName}/pdf")
        .then()
        .log().ifValidationFails()
        .and()
        .assertThat()
        .statusCode(200)
        .contentType("application/pdf");
    }
  }

  @Test
  @DisplayName("POST /api/templates/{templateName}/1/pdf should generate pdf of v1 template when v2 exists")
  @Order(8)
  public void template_generate_version_pdf() throws IOException {


    String templateName = "testVersionTemplate";
    //arrange
    RestAssured
      .given(createTemplateRequest(requestToServer, templateName,
        ImmutableList.of("communal"), "small.docx"))
      .when()
      .post("/api/templates")
      .then()
      .log().ifValidationFails()
      .and()
      .assertThat()
      .statusCode(201);

    RestAssured
      .given(createTemplateRequest(requestToServer, templateName,
        ImmutableList.of("communal"), "not_template.docx"))
      .when()
      .post("/api/templates")
      .then()
      .log().ifValidationFails()
      .and()
      .assertThat()
      .statusCode(201);

    //act
    String replaceAddress = "планета Марс, город Максим";
    String replaceDate = "01.01.2040";
    String replaceFio = "IVAN";
    CreatePdfRequest createPdfRequest = CreatePdfRequest.builder()
      .data(Arrays.asList(
        TemplateData.builder().name("АдресКомиссии").value(replaceAddress).build(),
        TemplateData.builder().name("ДатаНарушения").value(replaceDate).build(),
        TemplateData.builder().name("ФИОНарушителя").value(replaceFio).build())
      )
      .build();

    byte[] generatedPdfV1 = RestAssured
      .given(requestToServer)
      .contentType(JSON)
      .pathParam("templateName", templateName)
      .body(createPdfRequest)
      .when()
      .post("/api/templates/{templateName}/1/pdf")
      .then()
      .log().ifValidationFails()
      .and()
      .assertThat()
      .statusCode(200)
      .contentType("application/pdf")
      .extract()
      .body()
      .asByteArray();

    byte[] generatedPdfV2 = RestAssured
      .given(requestToServer)
      .contentType(JSON)
      .pathParam("templateName", templateName)
      .body(createPdfRequest)
      .when()
      .post("/api/templates/{templateName}/2/pdf")
      .then()
      .log().ifValidationFails()
      .and()
      .assertThat()
      .statusCode(200)
      .contentType("application/pdf")
      .extract()
      .body()
      .asByteArray();

    //assert
    PDF pdf1 = new PDF(generatedPdfV1);

    assertThat(pdf1).containsExactText(replaceAddress);
    assertThat(pdf1).containsExactText(replaceDate);
    assertThat(pdf1).containsExactText(replaceFio);
    assertThat(pdf1).doesNotContainExactText("{{ВремяНарушения}}");


    PDF pdf2 = new PDF(generatedPdfV2);
    assertThat(pdf2).doesNotContainExactText(replaceAddress);
    assertThat(pdf2).doesNotContainExactText(replaceDate);
    assertThat(pdf2).doesNotContainExactText(replaceFio);
  }

  @Test
  @DisplayName("POST /api/templates/{templateName}/pdf should generate pdf with picture")
  @Order(9)
  public void template_generate_pdf_with_image() throws IOException {

    String templateName = "testTemplate";
    //arrange
    RestAssured
      .given(createTemplateRequest(requestToServer, templateName,
        ImmutableList.of("communal"), "medium_shpi.docx"))
      .when()
      .post("/api/templates")
      .then()
      .log().ifValidationFails()
      .and()
      .assertThat()
      .statusCode(201);
    //act
    String replaceShpi = "iVBORw0KGgoAAAANSUhEUgAAAdgAAAB2AQMAAABRdhDwAAAAAXNSR0IB2cksfwAAAAlwSFlzAAAuIwAALiMBeKU/dgAAAAZQTFRFAAAA////pdmf3QAAAFhJREFUeJzty6ENgEAURMFPEEhKoBRKg9IohRKQCJLlzqHRY15W7FTVnLQ8Ves1JkdbyV21nVOyV89yDy3t1h8fwLIsy7Isy7Isy7Isy7Isy7Isy7LsL/sCIFOl3zsC3XAAAAAASUVORK5CYII=";
    String replaceFio = "Иванову Ивану Ивановичу";

    CreatePdfRequest createPdfRequest = CreatePdfRequest.builder()
      .data(Arrays.asList(
        TemplateData.builder().name("@ШПИ").value(replaceShpi).build(),
        TemplateData.builder().name("ФИОНарушителя").value(replaceFio).build())
      )
      .build();
    byte[] generatedPdf = RestAssured
      .given(requestToServer)
      .contentType(JSON)
      .pathParam("templateName", templateName)
      .body(createPdfRequest)
      .when()
      .post("/api/templates/{templateName}/pdf")
      .then()
      .log().ifValidationFails()
      .and()
      .assertThat()
      .statusCode(200)
      .contentType("application/pdf")
      .extract()
      .body()
      .asByteArray();
    String generatedPdfAsStr = new String(generatedPdf);
    PDF pdf = new PDF(generatedPdf);

    //assert
    assertThat(generatedPdfAsStr.contains("Type/XObject/Subtype/Image/Width 472/Height 118/BitsPerComponent 8/Length")).isTrue();
    assertThat(pdf).containsExactText(replaceFio);

    Path tempPath = Files.createTempFile("output", ".pdf");
    FileOutputStream fos = new FileOutputStream(tempPath.toFile());
    fos.write(generatedPdf, 0, generatedPdf.length);
    fos.flush();
    fos.close();

    log.info("View pdf by eyes: " + tempPath);
  }


}

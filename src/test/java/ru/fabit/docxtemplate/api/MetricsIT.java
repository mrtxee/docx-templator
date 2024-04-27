package ru.fabit.docxtemplate.api;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.web.server.LocalServerPort;
import ru.fabit.docxtemplate.integrationtest.IntegrationTest;

@IntegrationTest
public class MetricsIT {

  private static final int OK = 200;

  @LocalServerPort
  private int localServerPort;

  RequestSpecification requestToServer;

  @BeforeEach
  public void setup() {
    requestToServer = RestAssured
      .given()
      .and().port(localServerPort)
      .accept(ContentType.TEXT);
  }

  @Test
  @DisplayName("Endpoint /metrics should return 200 and work without auth")
  public void metrics() {
    RestAssured
      .given(requestToServer)
      .when()
      .get("/metrics")
      .then()
      .log().ifValidationFails()
      .and()
      .assertThat()
      .statusCode(OK);
  }
}

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
public class HealthcheckIT {

  private static final int OK = 200;

  @LocalServerPort
  private int localServerPort;

  RequestSpecification requestToServer;

  @BeforeEach
  public void setup() {
    requestToServer = RestAssured
      .given()
      .and().port(localServerPort)
      .accept(ContentType.JSON);
  }

  @Test
  @DisplayName("Endpoint /system/health/live should return 200 and work without auth")
  public void live() {
    RestAssured
      .given(requestToServer)
      .when()
      .get("/system/health/live")
      .then()
      .log().ifValidationFails()
      .and()
      .assertThat()
      .statusCode(OK);
  }

  @Test
  @DisplayName("Endpoint /system/health/ready should return 200 and work without auth")
  public void ready() {
    RestAssured
      .given(requestToServer)
      .when()
      .get("/system/health/ready")
      .then()
      .log().ifValidationFails()
      .and()
      .assertThat()
      .statusCode(OK);
  }
}

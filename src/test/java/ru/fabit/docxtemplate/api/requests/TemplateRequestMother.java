package ru.fabit.docxtemplate.api.requests;

import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import org.springframework.util.ResourceUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static io.restassured.http.ContentType.JSON;

public class TemplateRequestMother {
  public static RequestSpecification createTemplateRequest(RequestSpecification specification, String name,
                                                           List<String> tags, String filename) throws IOException {

    File file = ResourceUtils.getFile("classpath:test-templates/" + filename);
    String base64EncodedTemplateFile =
      Base64.getEncoder().withoutPadding().encodeToString(Files.readAllBytes(file.toPath()));

    Map<String, Object> jsonAsMap = new HashMap<>();
    jsonAsMap.put("name", name);
    jsonAsMap.put("templateTags", tags);
    jsonAsMap.put("templateFile", base64EncodedTemplateFile);

    return RestAssured
      .given(specification)
      .contentType(JSON)
      .body(jsonAsMap);
  }

}

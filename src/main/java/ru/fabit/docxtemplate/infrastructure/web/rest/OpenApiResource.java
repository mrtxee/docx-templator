package ru.fabit.docxtemplate.infrastructure.web.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;

@Slf4j
@RestController
@RequiredArgsConstructor
public class OpenApiResource {

  @Value("classpath:/static/docs/openapi.yaml")
  private Resource openApiResource;

  @GetMapping(value = "/v2/api-docs", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<String> getOpenApiSpec() throws IOException {

    String yaml = asString(openApiResource);
    ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
    Object obj = yamlReader.readValue(yaml, Object.class);

    ObjectMapper jsonWriter = new ObjectMapper();
    String json = jsonWriter.writeValueAsString(obj);
    return ResponseEntity.ok().body(json);
  }

  public static String asString(final Resource resource) {

    try (Reader reader = new InputStreamReader(resource.getInputStream())) {
      return FileCopyUtils.copyToString(reader);
    } catch (IOException e) {
      throw new UncheckedIOException(e);
    }
  }
}
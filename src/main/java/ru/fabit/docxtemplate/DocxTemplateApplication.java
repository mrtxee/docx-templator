package ru.fabit.docxtemplate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication(exclude = ErrorMvcAutoConfiguration.class)
@ConfigurationPropertiesScan("ru.fabit.docxtemplate.boot.config.properties")
public class DocxTemplateApplication {

  public static void main(String[] args) {
    SpringApplication.run(DocxTemplateApplication.class, args);
  }

}
package ru.fabit.docxtemplate.boot.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "app.office")
public class OfficeProperties {

  private String homePath;
}
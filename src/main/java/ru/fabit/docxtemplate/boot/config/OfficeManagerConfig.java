package ru.fabit.docxtemplate.boot.config;

import lombok.RequiredArgsConstructor;
import org.jodconverter.core.office.OfficeException;
import org.jodconverter.core.office.OfficeManager;
import org.jodconverter.local.office.LocalOfficeManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.fabit.docxtemplate.boot.config.properties.OfficeProperties;

@Configuration
@RequiredArgsConstructor
public class OfficeManagerConfig {

  private final OfficeProperties officeProperties;

  @Bean
  public OfficeManager officeManager() throws OfficeException {

    final OfficeManager officeManager = LocalOfficeManager
      .builder()
      .install()
      .officeHome(officeProperties.getHomePath())
      .build();

    officeManager.start();

    return officeManager;
  }
}
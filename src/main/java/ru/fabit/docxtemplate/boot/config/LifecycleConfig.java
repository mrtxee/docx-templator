package ru.fabit.docxtemplate.boot.config;

import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.jodconverter.core.office.OfficeException;
import org.jodconverter.core.office.OfficeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class LifecycleConfig {

  @Autowired
  OfficeManager officeManager;

  @PreDestroy
  public void onExit() {
    log.info("Stopping office manager (libreoffice) because application stopped");
    try {
      officeManager.stop();
    } catch (OfficeException e) {
      log.error("", e);
    }
    log.info("Stopped from lifecycle");
  }
}

package ru.fabit.docxtemplate.integrationtest;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.opentest4j.AssertionFailedError;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
public class PostgresExtension implements
  BeforeAllCallback, BeforeEachCallback, ExtensionContext.Store.CloseableResource {

  private static boolean INITIALIZED = false;
  private static PostgresContainer container;
  private static boolean shouldRaiseException = false;

  @Override
  public void beforeAll(ExtensionContext extensionContext) {
    try {
      if (INITIALIZED) {
        return;
      }
      INITIALIZED = true;
      container = PostgresContainer.getInstance();
      container.start();
      assertThat(container.isRunning()).isTrue();
    } catch (Throwable ex) {
      log.error("Failed to start container, all related tests will be marked as failed");
      shouldRaiseException = true;
    }
  }

  @Override
  public void close() {
    if (container == null) {
      return;
    }
    container.stop();
    assertThat(container.isShouldBeReused()).isFalse();
  }

  @Override
  public void beforeEach(ExtensionContext context) throws Exception {
    if (shouldRaiseException) {
      throw new AssertionFailedError(
        "Test \"" + context.getDisplayName() + "\" has been marked as failed due to container failure");
    }
  }

  public static PostgresContainer getPostgresContainer() {
    if (shouldRaiseException) {
      throw new AssertionFailedError(
        "This test has been marked as failed due to container failure. Explore the logs for details.");
    }
    return PostgresContainer.getInstance();
  }
}

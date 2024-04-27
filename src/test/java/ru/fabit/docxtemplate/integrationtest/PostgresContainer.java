package ru.fabit.docxtemplate.integrationtest;

import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.LogMessageWaitStrategy;
import org.testcontainers.utility.DockerImageName;
import javax.sql.DataSource;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static java.lang.System.setProperty;

public class PostgresContainer extends PostgreSQLContainer<PostgresContainer> {

  private static final String IMAGE_NAME = "bitnami/postgresql:13";
  private static final String USERNAME = "postgres";
  private static final String PASSWORD = "postgres";
  private static final String DATABASE_NAME = "docx_template";
  private static final long STARTUP_TIMEOUT_MINUTES = 2;

  private static PostgresContainer instance;
  private static DataSource datasourceInstance;

  public PostgresContainer() {
    super(DockerImageName.parse(IMAGE_NAME).asCompatibleSubstituteFor("postgres"));
    this.withUsername(USERNAME);
    this.withPassword(PASSWORD);
    this.withDatabaseName(DATABASE_NAME);
    this.setCommandParts(new String[0]);
    this.waitStrategy = new LogMessageWaitStrategy()
      .withRegEx(".*database system is ready to accept connections.*\\s")
      .withTimes(1)
      .withStartupTimeout(Duration.of(STARTUP_TIMEOUT_MINUTES, ChronoUnit.MINUTES));
  }

  public static PostgresContainer getInstance() {
    if (instance == null) {
      instance = new PostgresContainer();
    }
    return instance;
  }

  @Override
  protected void configure() {
    this.addEnv("POSTGRESQL_DATABASE", DATABASE_NAME);
    this.addEnv("POSTGRESQL_USERNAME", this.getUsername());
    this.addEnv("POSTGRESQL_PASSWORD", this.getPassword());
  }

  @Override
  public String getJdbcUrl() {
    return super.getJdbcUrl();
  }

  @Override
  public void start() {
    super.start();
    setProperty("spring.datasource.url", getJdbcUrl());
    setProperty("spring.datasource.username", getUsername());
    setProperty("spring.datasource.password", getPassword());
  }
}

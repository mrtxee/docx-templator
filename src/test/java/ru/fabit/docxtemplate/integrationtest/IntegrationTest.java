package ru.fabit.docxtemplate.integrationtest;

import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.annotation.AliasFor;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@SpringBootTest
@ActiveProfiles("test")
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Testcontainers
@ExtendWith({PostgresExtension.class})
public @interface IntegrationTest {

  @AliasFor(annotation = SpringBootTest.class, attribute = "webEnvironment")
  SpringBootTest.WebEnvironment webEnvironment() default SpringBootTest.WebEnvironment.RANDOM_PORT;

}

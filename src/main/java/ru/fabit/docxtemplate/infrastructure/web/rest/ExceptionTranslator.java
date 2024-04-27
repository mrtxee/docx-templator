package ru.fabit.docxtemplate.infrastructure.web.rest;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.fabit.docxtemplate.api.templates.exceptions.FileFormatException;
import ru.fabit.docxtemplate.api.templates.exceptions.FileNotFoundException;
import ru.fabit.docxtemplate.api.templates.exceptions.TemplateNotFoundException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;

@Slf4j
@RestControllerAdvice
public class ExceptionTranslator extends ResponseEntityExceptionHandler {

  private final Map<HttpStatus, String> typeByStatus;

  public ExceptionTranslator() {

    this.typeByStatus = new HashMap<>();

    typeByStatus.put(INTERNAL_SERVER_ERROR, "urn:problem-type:error");
    typeByStatus.put(BAD_REQUEST, "urn:problem-type:bad-request");
    typeByStatus.put(NOT_FOUND, "urn:problem-type:not-found");
    typeByStatus.put(CONFLICT, "urn:problem-type:conflict");
    typeByStatus.put(UNPROCESSABLE_ENTITY, "urn:problem-type:business-rules");
    typeByStatus.put(FORBIDDEN, "urn:problem-type:forbidden");
    typeByStatus.put(UNAUTHORIZED, "urn:problem-type:unauthenticated");
  }

  @ExceptionHandler
  public ProblemDetail handleRuntimeException(RuntimeException e) {
    return handleException(e);
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public ProblemDetail handleIllegalArgumentException(IllegalArgumentException e) {
    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(BAD_REQUEST, e.getMessage());
    problemDetail.setTitle("Bad request");
    problemDetail.setType(URI.create(typeByStatus.get(BAD_REQUEST)));
    problemDetail.setInstance(getInstance());
    log.warn(problemDetail.toString());
    return problemDetail;
  }

  @ExceptionHandler(Exception.class)
  public ProblemDetail handleException(Exception e) {

    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(INTERNAL_SERVER_ERROR, e.getMessage());
    problemDetail.setTitle("Internal server error");
    problemDetail.setType(URI.create(typeByStatus.get(INTERNAL_SERVER_ERROR)));
    problemDetail.setInstance(getInstance());
    log.error(problemDetail.toString(), e);
    return problemDetail;
  }
    @ExceptionHandler(TemplateNotFoundException.class)
    ProblemDetail handleBookmarkNotFoundException(TemplateNotFoundException e) {
      ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(NOT_FOUND, e.getMessage());
      problemDetail.setTitle("Not Found");
      problemDetail.setType(URI.create(typeByStatus.get(NOT_FOUND)));
      problemDetail.setInstance(getInstance());
      log.warn(problemDetail.toString());
      return problemDetail;
    }


  @ExceptionHandler(FileNotFoundException.class)
  public ProblemDetail handleFileNotFoundException(FileNotFoundException e) {

    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(NOT_FOUND, e.getMessage());
    problemDetail.setTitle("Not Found");
    problemDetail.setType(URI.create(typeByStatus.get(NOT_FOUND)));
    problemDetail.setInstance(getInstance());
    log.warn(problemDetail.toString());
    return problemDetail;
  }

  @ExceptionHandler(FileFormatException.class)
  public ProblemDetail handleFileFormatException(FileFormatException e) {

    ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(BAD_REQUEST, e.getMessage());
    problemDetail.setTitle("Bad request");
    problemDetail.setType(URI.create(typeByStatus.get(BAD_REQUEST)));
    problemDetail.setInstance(getInstance());
    log.warn(problemDetail.toString());
    return problemDetail;
  }



  private URI getInstance() {
    return URI.create("urn:uuid:" + UUID.randomUUID());
  }
}
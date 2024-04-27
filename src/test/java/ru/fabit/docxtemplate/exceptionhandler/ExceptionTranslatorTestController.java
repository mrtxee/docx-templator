package ru.fabit.docxtemplate.exceptionhandler;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.fabit.docxtemplate.api.templates.exceptions.FileNotFoundException;
import java.util.NoSuchElementException;

@RestController
@RequiredArgsConstructor
@RequestMapping(ExceptionUrls.ROOT)
public class ExceptionTranslatorTestController {

  public final ObjectMapper objectMapper;

  @PostMapping(ExceptionUrls.INTERNAL_SERVER_ERROR)
  public void internalServerError() throws Exception {
    throw new Exception("internal-server-error");
  }

  @PostMapping(ExceptionUrls.RUNTIME_EXCEPTION)
  public void runtimeException() {
    throw new RuntimeException("Some runtime exception");
  }


  @GetMapping(ExceptionUrls.NOT_FOUND)
  public void notFoundException() {
    throw new NoSuchElementException("Not Found");
  }

  @GetMapping(ExceptionUrls.FILE_NOT_FOUND)
  public void fileNotFoundException() {
    throw new FileNotFoundException("File not found");
  }

  @GetMapping(ExceptionUrls.BAD_REQUEST_ILLEGAL_ARGUMENT_EXCEPTION)
  public void illegalArgumentException() {
    throw new IllegalArgumentException("Illegal argument");
  }



}

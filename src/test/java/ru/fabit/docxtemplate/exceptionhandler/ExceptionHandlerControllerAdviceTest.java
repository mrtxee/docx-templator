package ru.fabit.docxtemplate.exceptionhandler;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.fabit.docxtemplate.exceptionhandler.ExceptionUrls.BAD_REQUEST_ILLEGAL_ARGUMENT_EXCEPTION;
import static ru.fabit.docxtemplate.exceptionhandler.ExceptionUrls.FILE_NOT_FOUND;
import static ru.fabit.docxtemplate.exceptionhandler.ExceptionUrls.INTERNAL_SERVER_ERROR;
import static ru.fabit.docxtemplate.exceptionhandler.ExceptionUrls.ROOT;
import static ru.fabit.docxtemplate.exceptionhandler.ExceptionUrls.RUNTIME_EXCEPTION;

@WebMvcTest(controllers = ExceptionTranslatorTestController.class)
public class ExceptionHandlerControllerAdviceTest {

  @Autowired
  private MockMvc mockMvc;

  @DisplayName(
    "handleBadRequest with IllegalArgumentException thrown should return correct problem-json format response"
  )
  @Test
  public void handleBadRequestIllegalArgumentExceptionResponseCheck() throws Exception {

    this.mockMvc.perform(get(ROOT + BAD_REQUEST_ILLEGAL_ARGUMENT_EXCEPTION))
                .andExpect(jsonPath("$.type").value("urn:problem-type:bad-request"))
                .andExpect(jsonPath("$.title").value("Bad request"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.detail").value("Illegal argument"))
                .andExpect(jsonPath("$.instance").isString())
                .andDo(print());

  }

  @DisplayName("handleFileNotFound should return correct problem-json format response")
  @Test
  public void handleFileNotFoundResponseCheck() throws Exception {

    this.mockMvc.perform(get(ROOT + FILE_NOT_FOUND))
      .andExpect(jsonPath("$.type").value("urn:problem-type:not-found"))
      .andExpect(jsonPath("$.title").value("Not Found"))
      .andExpect(status().isNotFound())
      .andExpect(jsonPath("$.detail").value("File not found"))
      .andExpect(jsonPath("$.instance").isString())
      .andDo(print());
  }

  @DisplayName("handleException should return correct problem-json format response")
  @Test
  public void handleExceptionResponseCheck() throws Exception {

    this.mockMvc.perform(post(ROOT + INTERNAL_SERVER_ERROR))
                .andExpect(jsonPath("$.type").value("urn:problem-type:error"))
                .andExpect(jsonPath("$.title").value("Internal server error"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.detail").value("internal-server-error"))
                .andExpect(jsonPath("$.instance").isString())
                .andDo(print());
  }

  @DisplayName("handleRuntimeException should return correct problem-json format response")
  @Test
  public void handleRuntimeExceptionCheck() throws Exception {

    this.mockMvc.perform(post(ROOT + RUNTIME_EXCEPTION))
                .andExpect(jsonPath("$.type").value("urn:problem-type:error"))
                .andExpect(jsonPath("$.title").value("Internal server error"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.detail").value("Some runtime exception"))
                .andExpect(jsonPath("$.instance").isString())
                .andDo(print());
  }


}

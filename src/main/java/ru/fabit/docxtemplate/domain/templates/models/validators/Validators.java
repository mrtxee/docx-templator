package ru.fabit.docxtemplate.domain.templates.models.validators;

import org.apache.commons.lang3.StringUtils;
import java.util.List;
import java.util.Objects;

public class Validators {

  /**
   * Проверка, что строка не null и не пустая.
   * @param valueToCheck проверяемое значение.
   * @param message сообщение при ошибке.
   */
  public static void requireNotEmptyAndNull(String valueToCheck, String message) {
    if (Objects.isNull(valueToCheck) || StringUtils.isBlank(valueToCheck)) {
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Проверка, что строка не null.
   * @param valueToCheck проверяемое значение.
   * @param message сообщение при ошибке.
   */
  public static void requireNotNull(String valueToCheck, String message) {
    if (Objects.isNull(valueToCheck)) {
      throw new IllegalArgumentException(message);
    }
  }

  /**
   * Проверка, что список не null и не пустой.
   * @param valueToCheck проверяемое значение.
   * @param message сообщение при ошибке.
   */
  public static void requireNotEmptyAndNull(List valueToCheck, String message) {
    if (Objects.isNull(valueToCheck) || valueToCheck.isEmpty()) {
      throw new IllegalArgumentException(message);
    }
  }

}

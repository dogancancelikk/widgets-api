package com.miro.widget.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(NOT_FOUND)
public class WidgetNotFoundException extends RuntimeException {

  public WidgetNotFoundException() {
    super();
  }

}
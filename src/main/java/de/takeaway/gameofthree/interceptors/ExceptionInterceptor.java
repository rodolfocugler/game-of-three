package de.takeaway.gameofthree.interceptors;

import de.takeaway.gameofthree.exceptions.ExceptionResponse;
import de.takeaway.gameofthree.exceptions.GenericException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ExceptionInterceptor extends ResponseEntityExceptionHandler {

  @ExceptionHandler(value = GenericException.class)
  public ResponseEntity<Object> handleGenericException(GenericException ex, WebRequest request) {
    ExceptionResponse response = new ExceptionResponse(ex.getStatus().value(), ex.getMessage(),
            LocalDateTime.now());
    return new ResponseEntity<>(response, ex.getStatus());
  }
}
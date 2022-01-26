package de.takeaway.gameofthree.exceptions;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class GenericException extends RuntimeException {
  private final HttpStatus status;

  public GenericException(String message, HttpStatus status) {
    super(message);
    this.status = status;
  }
}
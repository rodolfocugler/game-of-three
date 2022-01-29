package de.takeaway.gameofthree.exceptions;

import org.springframework.http.HttpStatus;

public class ForbiddenException extends GenericException {
  public ForbiddenException(String message) {
    super(message, HttpStatus.FORBIDDEN);
  }
}
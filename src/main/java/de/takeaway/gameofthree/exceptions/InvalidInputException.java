package de.takeaway.gameofthree.exceptions;

import org.springframework.http.HttpStatus;

public class InvalidInputException extends GenericException {
  public InvalidInputException(String message) {
    super(message, HttpStatus.BAD_REQUEST);
  }
}
package de.takeaway.gameofthree.exceptions;

import lombok.*;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionResponse {
  private int status;
  private String message;
  private LocalDateTime timestamp;
}
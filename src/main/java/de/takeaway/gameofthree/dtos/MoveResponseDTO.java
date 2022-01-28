package de.takeaway.gameofthree.dtos;

import lombok.*;

import java.util.Objects;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MoveResponseDTO {
  private long addedNumber;
  private long resultingNumber;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    MoveResponseDTO that = (MoveResponseDTO) o;
    return addedNumber == that.addedNumber && resultingNumber == that.resultingNumber;
  }

  @Override
  public int hashCode() {
    return Objects.hash(addedNumber, resultingNumber);
  }
}
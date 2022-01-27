package de.takeaway.gameofthree.dtos;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class MoveRequestDTO {
  private long playerId;
  private int number;
  private long gameId;
}
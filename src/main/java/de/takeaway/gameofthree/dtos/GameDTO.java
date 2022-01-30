package de.takeaway.gameofthree.dtos;

import de.takeaway.gameofthree.models.Move;
import lombok.*;

import java.util.List;
import java.util.Objects;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class GameDTO {
  private long id = 0;
  private List<Move> moves;
  private PlayerDTO player1;
  private PlayerDTO player2;
  private PlayerDTO winner;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    GameDTO gameDTO = (GameDTO) o;
    return id == gameDTO.id && Objects.equals(moves, gameDTO.moves) && Objects.equals(player1, gameDTO.player1) && Objects.equals(player2, gameDTO.player2) && Objects.equals(winner, gameDTO.winner);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, moves, player1, player2, winner);
  }
}
package de.takeaway.gameofthree.models;

import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Game {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id = 0;

  @OneToMany(cascade = CascadeType.ALL)
  private List<Move> moves;

  @ManyToOne
  @JoinColumn(referencedColumnName = "id")
  private Player player1;

  @ManyToOne
  @JoinColumn(referencedColumnName = "id")
  private Player player2;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Game game = (Game) o;
    return id == game.id && Objects.equals(moves, game.moves) && Objects.equals(player1, game.player1) && Objects.equals(player2, game.player2);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, moves, player1, player2);
  }
}
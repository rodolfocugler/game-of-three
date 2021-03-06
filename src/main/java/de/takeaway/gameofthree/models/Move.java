package de.takeaway.gameofthree.models;

import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Move {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id = 0;

  private int number;

  private int addedNumber;

  @Column(name = "movement_order")
  private int order;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Move move = (Move) o;
    return id == move.id && number == move.number && addedNumber == move.addedNumber && order == move.order;
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, number, addedNumber, order);
  }
}
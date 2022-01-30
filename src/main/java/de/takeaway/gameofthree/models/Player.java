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
public class Player {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private long id = 0;

  @Column(nullable = false, length = 20, unique = true)
  private String username;

  @Column(nullable = false)
  private String password;

  @Column
  private boolean isAutomaticPlayEnabled;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Player player = (Player) o;
    return id == player.id && Objects.equals(username, player.username) && Objects.equals(password, player.password);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, username, password);
  }
}


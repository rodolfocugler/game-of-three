package de.takeaway.gameofthree.dtos;

import lombok.*;

import java.util.Objects;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class PlayerDTO {
  private long id;
  private String username;
  private boolean isAutomaticPlayEnabled;

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    PlayerDTO playerDTO = (PlayerDTO) o;
    return id == playerDTO.id && isAutomaticPlayEnabled == playerDTO.isAutomaticPlayEnabled && Objects.equals(username, playerDTO.username);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, username, isAutomaticPlayEnabled);
  }
}
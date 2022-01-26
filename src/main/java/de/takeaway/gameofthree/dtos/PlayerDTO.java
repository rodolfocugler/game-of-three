package de.takeaway.gameofthree.dtos;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class PlayerDTO extends User {

  private final long id;

  public PlayerDTO(String username, String password, Collection<? extends GrantedAuthority> authorities, long id) {
    super(username, password, authorities);
    this.id = id;
  }

  public long getId() {
    return id;
  }
}
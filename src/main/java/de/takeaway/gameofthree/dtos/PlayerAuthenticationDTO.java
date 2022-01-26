package de.takeaway.gameofthree.dtos;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class PlayerAuthenticationDTO extends User {

  private final long id;

  public PlayerAuthenticationDTO(String username, String password, Collection<? extends GrantedAuthority> authorities, long id) {
    super(username, password, authorities);
    this.id = id;
  }

  public long getId() {
    return id;
  }
}
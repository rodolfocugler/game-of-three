package de.takeaway.gameofthree.configs;

import de.takeaway.gameofthree.dtos.PlayerAuthenticationDTO;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;

import java.util.Collections;
import java.util.List;

@TestConfiguration
@Primary
public class PlayerAuthenticationTest implements UserDetailsManager {

  private final InMemoryUserDetailsManager inMemoryUserDetailsManager =
          new InMemoryUserDetailsManager(List.of(testUser()));

  public PlayerAuthenticationDTO testUser() {
    return new PlayerAuthenticationDTO("username", "123456",
            Collections.emptyList(), 1);
  }

  @Override
  public void createUser(UserDetails userDetails) {
    this.inMemoryUserDetailsManager.createUser(userDetails);
  }

  @Override
  public void updateUser(UserDetails userDetails) {
    this.inMemoryUserDetailsManager.updateUser(userDetails);
  }

  @Override
  public void deleteUser(String s) {
    this.inMemoryUserDetailsManager.deleteUser(s);
  }

  @Override
  public void changePassword(String s, String s1) {
    this.inMemoryUserDetailsManager.changePassword(s, s1);
  }

  @Override
  public boolean userExists(String s) {
    return this.inMemoryUserDetailsManager.userExists(s);
  }

  @Override
  public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
    return testUser();
  }
}
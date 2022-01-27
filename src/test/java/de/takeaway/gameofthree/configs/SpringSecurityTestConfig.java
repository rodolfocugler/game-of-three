package de.takeaway.gameofthree.configs;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetailsService;

@TestConfiguration
public class SpringSecurityTestConfig {

  @Bean
  @Primary
  public UserDetailsService userDetailsService() {
    return new PlayerAuthenticationTest();
  }
}
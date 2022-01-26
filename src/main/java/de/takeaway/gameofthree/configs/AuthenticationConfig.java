package de.takeaway.gameofthree.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AuthenticationConfig {

  public static final String SECRET = "0a046f598da583324875d93ff23f3657d4e6c025336bd08dc02f609878149b4fb6560829da98f4c28a90323458799011d1888b46c5096b7428c24a5cc63f8308";
  public static final long EXPIRATION_TIME = 3600000;
  public static final String TOKEN_PREFIX = "Bearer ";
  public static final String HEADER_STRING = "Authorization";
  public static final String SIGN_UP_URL = "/api/players";

  @Bean
  public BCryptPasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
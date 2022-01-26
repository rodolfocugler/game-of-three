package de.takeaway.gameofthree.interceptors;

import com.auth0.jwt.JWT;
import de.takeaway.gameofthree.dtos.PlayerAuthenticationDTO;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static de.takeaway.gameofthree.configs.AuthenticationConfig.*;

public class JWTAuthenticationInterceptor extends UsernamePasswordAuthenticationFilter {
  private AuthenticationManager authenticationManager;

  public JWTAuthenticationInterceptor(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest req,
                                              HttpServletResponse res) throws AuthenticationException {
    String username = Objects.requireNonNull(obtainUsername(req)).trim();
    String password = Objects.requireNonNull(obtainPassword(req));

    return authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(username, password, new ArrayList<>()));
  }

  @Override
  protected void successfulAuthentication(HttpServletRequest req,
                                          HttpServletResponse res,
                                          FilterChain chain,
                                          Authentication auth) {
    PlayerAuthenticationDTO user = ((PlayerAuthenticationDTO) auth.getPrincipal());
    String token = JWT.create()
            .withSubject(user.getUsername())
            .withClaim("id", user.getId())
            .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .sign(HMAC512(SECRET.getBytes()));
    res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
  }
}
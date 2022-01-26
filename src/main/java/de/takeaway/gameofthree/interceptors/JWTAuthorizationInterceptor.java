package de.takeaway.gameofthree.interceptors;


import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import de.takeaway.gameofthree.models.Player;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static de.takeaway.gameofthree.configs.AuthenticationConfig.*;

public class JWTAuthorizationInterceptor extends BasicAuthenticationFilter {

  public JWTAuthorizationInterceptor(AuthenticationManager authManager) {
    super(authManager);
  }

  @Override
  protected void doFilterInternal(HttpServletRequest req,
                                  HttpServletResponse res,
                                  FilterChain chain) throws IOException, ServletException {
    String header = req.getHeader(HEADER_STRING);

    if (header == null || !header.startsWith(TOKEN_PREFIX)) {
      chain.doFilter(req, res);
      return;
    }

    UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

    SecurityContextHolder.getContext().setAuthentication(authentication);
    chain.doFilter(req, res);
  }

  private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
    String token = request.getHeader(HEADER_STRING);

    if (token == null) {
      return null;
    }

    try {
      DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
              .build()
              .verify(token.replace(TOKEN_PREFIX, ""));

      Player player = Player.builder().username(decodedJWT.getSubject())
              .id(decodedJWT.getClaim("id").asLong()).build();

      return new UsernamePasswordAuthenticationToken(player, null, new ArrayList<>());
    } catch (Exception ex) {
      return null;
    }
  }
}
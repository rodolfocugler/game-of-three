package de.takeaway.gameofthree.utils;

import de.takeaway.gameofthree.models.Player;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public class AuthenticationUtil {

  public static void setAuthentication(long playerId) {
    Player player = Player.builder().id(playerId).username(String.format("username%s", playerId))
            .build();
    Authentication auth = new UsernamePasswordAuthenticationToken(player, null);
    SecurityContextHolder.getContext().setAuthentication(auth);
  }
}

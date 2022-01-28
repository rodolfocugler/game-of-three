package de.takeaway.gameofthree.controllers;

import de.takeaway.gameofthree.dtos.MoveRequestDTO;
import de.takeaway.gameofthree.dtos.MoveResponseDTO;
import de.takeaway.gameofthree.models.Player;
import de.takeaway.gameofthree.services.GameService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameControllerTest {

  private final MoveRequestDTO moveRequest = MoveRequestDTO.builder().number(56).playerId(2)
          .build();
  private final MoveResponseDTO dbMoveResponse = MoveResponseDTO.builder().resultingNumber(56)
          .build();
  private final Player player = Player.builder().username("username").id(1).build();
  @Mock
  Authentication authentication;
  @Mock
  SecurityContext securityContext;
  @Mock
  private GameService gameService;
  @InjectMocks
  private GameController gameController;

  @Test
  public void shouldReturnTheSavedMoveIfAValidMoveIsSent() {
    when(gameService.addMoveInGame(moveRequest, player)).thenReturn(dbMoveResponse);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getPrincipal()).thenReturn(player);
    SecurityContextHolder.setContext(securityContext);

    MoveResponseDTO moveResponse = gameController.addMove(moveRequest);

    assertThat(moveResponse).isEqualTo(dbMoveResponse);
  }

  @Test
  public void shouldPropagateExceptionIfServiceThrowsAnyException() {
    when(gameService.addMoveInGame(moveRequest, player)).thenThrow(new RuntimeException());
    when(securityContext.getAuthentication()).thenReturn(authentication);
    when(authentication.getPrincipal()).thenReturn(player);
    SecurityContextHolder.setContext(securityContext);

    assertThrows(RuntimeException.class, () -> gameController.addMove(moveRequest));
  }
}
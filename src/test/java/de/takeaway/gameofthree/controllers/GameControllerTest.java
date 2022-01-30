package de.takeaway.gameofthree.controllers;

import de.takeaway.gameofthree.dtos.GameDTO;
import de.takeaway.gameofthree.dtos.MoveRequestDTO;
import de.takeaway.gameofthree.dtos.MoveResponseDTO;
import de.takeaway.gameofthree.dtos.PlayerDTO;
import de.takeaway.gameofthree.models.Move;
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

import java.util.List;

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

  @Test
  public void shouldReturnTheAvailableGames() {
    PlayerDTO player1 = PlayerDTO.builder().id(1).build();
    PlayerDTO player2 = PlayerDTO.builder().id(2).build();
    Move move = Move.builder().id(1).addedNumber(1).number(1).order(1).build();
    GameDTO game = GameDTO.builder().player1(player1).player2(player2).winner(player1)
            .moves(List.of(move)).build();
    when(authentication.getPrincipal()).thenReturn(player);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(gameService.getAvailableGames(player)).thenReturn(List.of(game));

    List<GameDTO> games = gameController.getAvailableGames();

    assertThat(games).isEqualTo(List.of(game));
  }

  @Test
  public void shouldReturnAllGames() {
    PlayerDTO player1 = PlayerDTO.builder().id(1).build();
    PlayerDTO player2 = PlayerDTO.builder().id(2).build();
    Move move = Move.builder().id(1).addedNumber(1).number(1).order(1).build();
    GameDTO game = GameDTO.builder().player1(player1).player2(player2).winner(player1)
            .moves(List.of(move)).build();
    when(authentication.getPrincipal()).thenReturn(player);
    when(securityContext.getAuthentication()).thenReturn(authentication);
    SecurityContextHolder.setContext(securityContext);
    when(gameService.getAllGames(player)).thenReturn(List.of(game));

    List<GameDTO> games = gameController.getAllGames();

    assertThat(games).isEqualTo(List.of(game));
  }
}
package de.takeaway.gameofthree.controllers;

import de.takeaway.gameofthree.dtos.PlayerDTO;
import de.takeaway.gameofthree.models.Player;
import de.takeaway.gameofthree.services.PlayerService;
import de.takeaway.gameofthree.utils.AuthenticationUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlayerControllerTest {

  @Mock
  private PlayerService playerService;

  @InjectMocks
  private PlayerController playerController;

  private final Player player = Player.builder().username("username").build();
  private final Player dbPlayer = Player.builder().username("username1").id(1).build();
  private final PlayerDTO playerDto = PlayerDTO.builder().username("username").id(1).build();

  @Test
  public void shouldReturnNewPlayerIfANewPlayerIsCreated() {
    when(playerService.create(player)).thenReturn(playerDto);

    PlayerDTO response = playerController.create(player);

    assertThat(response).isEqualTo(playerDto);
  }

  @Test
  public void shouldPropagateExceptionIfServiceThrowsAnException() {
    when(playerService.create(player)).thenThrow(new RuntimeException());

    assertThrows(RuntimeException.class, () -> playerController.create(player));
  }

  @Test
  public void shouldReturnAListOfPlayers() {
    List<PlayerDTO> players = List.of(playerDto, playerDto, playerDto);
    when(playerService.get()).thenReturn(players);

    List<PlayerDTO> response = playerController.get();

    assertThat(response).isEqualTo(players);
  }

  @Test
  public void shouldReturnTheUpdatedPlayerIfAPlayerIsUpdated() {
    when(playerService.update(dbPlayer.getId(), dbPlayer, dbPlayer)).thenReturn(playerDto);
    AuthenticationUtil.setAuthentication(dbPlayer.getId());
    PlayerDTO response = playerController.update(dbPlayer, dbPlayer.getId());

    assertThat(response).isEqualTo(playerDto);
  }

  @Test
  public void shouldPropagateExceptionIfServiceThrowsAnExceptionForUpdateMethod() {
    when(playerService.update(player.getId(), player, player)).thenThrow(new RuntimeException());
    AuthenticationUtil.setAuthentication(player.getId());
    assertThrows(RuntimeException.class, () -> playerController.update(player, player.getId()));
  }
}
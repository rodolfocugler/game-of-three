package de.takeaway.gameofthree.controllers;

import de.takeaway.gameofthree.models.Player;
import de.takeaway.gameofthree.services.PlayerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

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
  private final Player dbPlayer = Player.builder().username("username").id(1).build();

  @Test
  public void shouldReturnNewPlayerIfANewPlayerIsCreated() {
    when(playerService.create(player)).thenReturn(dbPlayer);

    Player response = playerController.create(player);

    assertThat(response).isEqualTo(dbPlayer);
  }

  @Test
  public void shouldPropagateExceptionIfServiceThrowsAnException() {
    when(playerService.create(player)).thenThrow(new RuntimeException());

    assertThrows(RuntimeException.class, () -> playerController.create(player));
  }
}
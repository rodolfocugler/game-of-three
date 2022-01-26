package de.takeaway.gameofthree.services;

import de.takeaway.gameofthree.exceptions.InvalidInputException;
import de.takeaway.gameofthree.models.Player;
import de.takeaway.gameofthree.repositories.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest {

  @Mock
  private PlayerRepository playerRepository;


  @InjectMocks
  private PlayerService playerService;
  private final Player player = Player.builder().username("username").build();
  private final Player dbPlayer = Player.builder().username("username").id(1).build();

  @Test
  public void shouldReturnNewPlayerIfANewPlayerIsCreated() {
    when(playerRepository.save(player)).thenReturn(dbPlayer);

    Player response = playerService.create(player);

    assertThat(response).isEqualTo(dbPlayer);
  }

  @Test
  public void shouldThrowInvalidInputExceptionIfUsernameIsNull() {
    InvalidInputException exception = assertThrows(InvalidInputException.class,
            () -> playerService.create(new Player()));
    assertThat(exception.getMessage()).isEqualTo("Username cannot be null or empty.");
  }

  @Test
  public void shouldThrowInvalidInputExceptionIfUsernameIsEmpty() {
    InvalidInputException exception = assertThrows(InvalidInputException.class,
            () -> playerService.create(Player.builder().username("").build()));
    assertThat(exception.getMessage()).isEqualTo("Username cannot be null or empty.");
  }

  @Test
  public void shouldThrowInvalidInputExceptionIfUsernameIsWhitespace() {
    InvalidInputException exception = assertThrows(InvalidInputException.class,
            () -> playerService.create(Player.builder().username("  ").build()));
    assertThat(exception.getMessage()).isEqualTo("Username cannot be null or empty.");
  }

  @Test
  public void shouldThrowInvalidInputExceptionIfUsernameHasMoreThan20Characters() {
    InvalidInputException exception = assertThrows(InvalidInputException.class,
            () -> playerService.create(Player.builder().username("012345678901234567890").build()));
    assertThat(exception.getMessage()).isEqualTo("Username must have less then 20 characters.");
  }
}
package de.takeaway.gameofthree.services;

import de.takeaway.gameofthree.dtos.PlayerDTO;
import de.takeaway.gameofthree.exceptions.InvalidInputException;
import de.takeaway.gameofthree.models.Player;
import de.takeaway.gameofthree.repositories.PlayerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PlayerServiceTest {

  @Mock
  private PlayerRepository playerRepository;

  @Mock
  private BCryptPasswordEncoder bCryptPasswordEncoder;

  @InjectMocks
  private PlayerService playerService;
  private final Player player = Player.builder().username("username").password("123456").build();
  private final Player playerWithEncodedPassword =
          Player.builder().username("username").password("password").build();
  private final Player dbPlayer = Player.builder().username("username").password("asd").id(1)
          .build();

  @Test
  public void shouldReturnNewPlayerIfANewPlayerIsCreated() {
    when(bCryptPasswordEncoder.encode("123456")).thenReturn("password");
    when(playerRepository.save(playerWithEncodedPassword)).thenReturn(dbPlayer);

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

  @Test
  public void shouldThrowInvalidInputExceptionIfPasswordIsNull() {
    InvalidInputException exception = assertThrows(InvalidInputException.class,
            () -> playerService.create(Player.builder().username("dummy").build()));
    assertThat(exception.getMessage()).isEqualTo("Password cannot be null or empty.");
  }

  @Test
  public void shouldThrowInvalidInputExceptionIfPasswordIsEmpty() {
    InvalidInputException exception = assertThrows(InvalidInputException.class,
            () -> playerService.create(Player.builder().username("dummy").password("").build()));
    assertThat(exception.getMessage()).isEqualTo("Password cannot be null or empty.");
  }

  @Test
  public void shouldThrowInvalidInputExceptionIfPasswordIsWhitespace() {
    InvalidInputException exception = assertThrows(InvalidInputException.class,
            () -> playerService.create(Player.builder().username("dummy").password("   ").build()));
    assertThat(exception.getMessage()).isEqualTo("Password cannot be null or empty.");
  }

  @Test
  public void shouldThrowInvalidInputExceptionIfPasswordHasMoreThan20Characters() {
    InvalidInputException exception = assertThrows(InvalidInputException.class,
            () -> playerService.create(Player.builder().username("dummy")
                    .password("012345678901234567890").build()));
    assertThat(exception.getMessage()).isEqualTo("Password must have between 6 and 20 characters.");
  }

  @Test
  public void shouldThrowInvalidInputExceptionIfPasswordHasLessThan6Characters() {
    InvalidInputException exception = assertThrows(InvalidInputException.class,
            () -> playerService.create(Player.builder().username("dummy").password("123").build()));
    assertThat(exception.getMessage()).isEqualTo("Password must have between 6 and 20 characters.");
  }

  @Test
  public void shouldReturnUserIfUsernameIsPresent() {
    when(playerRepository.findByUsername("dummy")).thenReturn(dbPlayer);
    PlayerDTO expectedPlayer = new PlayerDTO(dbPlayer.getUsername(), dbPlayer.getPassword(),
            Collections.emptyList(), dbPlayer.getId());

    PlayerDTO player = (PlayerDTO) playerService.loadUserByUsername("dummy");


    assertThat(player).isEqualTo(expectedPlayer);
  }


  @Test
  public void shouldThrowUsernameNotFoundExceptionIfUsernameIsNotPresent() {
    assertThrows(UsernameNotFoundException.class, () -> playerService.loadUserByUsername("dummy"));
  }
}
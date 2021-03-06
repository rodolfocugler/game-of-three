package de.takeaway.gameofthree.services;

import de.takeaway.gameofthree.dtos.PlayerAuthenticationDTO;
import de.takeaway.gameofthree.dtos.PlayerDTO;
import de.takeaway.gameofthree.exceptions.ForbiddenException;
import de.takeaway.gameofthree.exceptions.InvalidInputException;
import de.takeaway.gameofthree.exceptions.NotFoundException;
import de.takeaway.gameofthree.models.Player;
import de.takeaway.gameofthree.repositories.PlayerRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PlayerService implements UserDetailsService {
  private final PlayerRepository playerRepository;
  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public PlayerService(PlayerRepository playerRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.playerRepository = playerRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  public PlayerDTO create(Player player) {
    validateUsername(player.getUsername());
    validatePassword(player.getPassword());
    player.setPassword(bCryptPasswordEncoder.encode(player.getPassword()));
    try {
      Player dbPlayer = playerRepository.save(player);
      return PlayerDTO.builder().username(dbPlayer.getUsername()).id(dbPlayer.getId())
              .isAutomaticPlayEnabled(dbPlayer.isAutomaticPlayEnabled()).build();
    } catch (DataIntegrityViolationException ex) {
      throw new InvalidInputException("Username already exists.");
    }
  }

  private void validateUsername(String username) {
    if (username == null || username.isBlank()) {
      throw new InvalidInputException("Username cannot be null or empty.");
    } else if (username.length() > 20) {
      throw new InvalidInputException("Username must have less then 20 characters.");
    }
  }

  private void validatePassword(String password) {
    if (password == null || password.isBlank()) {
      throw new InvalidInputException("Password cannot be null or empty.");
    } else if (password.length() > 20 || password.length() < 6) {
      throw new InvalidInputException("Password must have between 6 and 20 characters.");
    }
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Player player = playerRepository.findByUsername(username.trim());

    if (player == null) {
      throw new UsernameNotFoundException(username.trim());
    }

    return new PlayerAuthenticationDTO(player.getUsername(), player.getPassword(), Collections.emptyList(),
            player.getId());
  }

  public List<PlayerDTO> get() {
    return playerRepository.findAll().stream().map(player ->
            PlayerDTO.builder().id(player.getId()).username(player.getUsername())
                    .isAutomaticPlayEnabled(player.isAutomaticPlayEnabled()).build())
            .collect(Collectors.toList());
  }

  public Player findById(long id) {
    Optional<Player> optionalPlayer = playerRepository.findById(id);
    if (optionalPlayer.isEmpty()) {
      throw new NotFoundException("Player id does not exist.");
    }
    return optionalPlayer.get();
  }

  public PlayerDTO update(long id, Player player, Player loggedPlayer) {
    if (id != loggedPlayer.getId()) {
      throw new ForbiddenException("Player cannot update another player");
    }
    validatePassword(player.getPassword());
    Player dbPlayer = findById(id);

    if (!dbPlayer.getUsername().equals(player.getUsername())) {
      throw new ForbiddenException("Player cannot update the username");
    }

    dbPlayer.setPassword(bCryptPasswordEncoder.encode(player.getPassword()));
    dbPlayer.setAutomaticPlayEnabled(player.isAutomaticPlayEnabled());

    playerRepository.save(dbPlayer);
    return PlayerDTO.builder().username(dbPlayer.getUsername()).id(dbPlayer.getId())
            .isAutomaticPlayEnabled(dbPlayer.isAutomaticPlayEnabled()).build();
  }

  public PlayerDTO mapToPlayerDTO(Player player) {
    return PlayerDTO.builder().id(player.getId()).username(player.getUsername())
            .isAutomaticPlayEnabled(player.isAutomaticPlayEnabled()).build();
  }
}
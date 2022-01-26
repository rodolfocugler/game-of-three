package de.takeaway.gameofthree.services;

import de.takeaway.gameofthree.exceptions.InvalidInputException;
import de.takeaway.gameofthree.models.Player;
import de.takeaway.gameofthree.repositories.PlayerRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class PlayerService {
  private final PlayerRepository playerRepository;

  public PlayerService(PlayerRepository playerRepository) {
    this.playerRepository = playerRepository;
  }

  public Player create(Player player) {
    if (player.getUsername() == null || player.getUsername().isBlank()) {
      throw new InvalidInputException("Username cannot be null or empty.");
    } else if (player.getUsername().length() > 20) {
      throw new InvalidInputException("Username must have less then 20 characters.");
    }

    try {
      return playerRepository.save(player);
    } catch (DataIntegrityViolationException ex) {
      throw new InvalidInputException("Username already exists.");
    }
  }
}
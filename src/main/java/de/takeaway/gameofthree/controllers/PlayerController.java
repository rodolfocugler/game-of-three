package de.takeaway.gameofthree.controllers;

import de.takeaway.gameofthree.models.Player;
import de.takeaway.gameofthree.services.PlayerService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/players")
public class PlayerController {

  private final PlayerService playerService;

  public PlayerController(PlayerService playerService) {
    this.playerService = playerService;
  }

  @PostMapping
  public Player create(@RequestBody @Validated Player player) {
    return playerService.create(player);
  }
}
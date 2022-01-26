package de.takeaway.gameofthree.controllers;

import de.takeaway.gameofthree.dtos.PlayerDTO;
import de.takeaway.gameofthree.models.Player;
import de.takeaway.gameofthree.services.PlayerService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

  @GetMapping
  public List<PlayerDTO> get() {
    return playerService.get();
  }
}
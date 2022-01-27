package de.takeaway.gameofthree.controllers;

import de.takeaway.gameofthree.dtos.MoveRequestDTO;
import de.takeaway.gameofthree.dtos.MoveResponseDTO;
import de.takeaway.gameofthree.models.Player;
import de.takeaway.gameofthree.services.GameService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/games")
public class GameController {

  private final GameService gameService;

  public GameController(GameService gameService) {
    this.gameService = gameService;
  }

  @PostMapping("/move")
  public MoveResponseDTO addMove(@RequestBody @Validated MoveRequestDTO moveRequest) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Player loggedPlayer = (Player) authentication.getPrincipal();
    return gameService.addMoveInGame(moveRequest, loggedPlayer);
  }
}
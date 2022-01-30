package de.takeaway.gameofthree.controllers;

import de.takeaway.gameofthree.dtos.MoveRequestDTO;
import de.takeaway.gameofthree.dtos.MoveResponseDTO;
import de.takeaway.gameofthree.models.Player;
import de.takeaway.gameofthree.services.GameService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/games")
@Tag(name = "Game Controller", description = "Game related code")
public class GameController {

  private final GameService gameService;

  public GameController(GameService gameService) {
    this.gameService = gameService;
  }

  @Operation(summary = "Add move to a game",
          description = "Add a move in a new game or in an existing one")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Move created successfully"),
          @ApiResponse(responseCode = "400", description = "Player cannot play alone.<br />" +
                  "First move must be higher than 3.<br />" +
                  "It is not the turn of the player. <br />" +
                  "Added number must be {-1, 0 or 1}."),
          @ApiResponse(responseCode = "401", description = "Invalid authentication."),
          @ApiResponse(responseCode = "404", description = "Game id does not exist.<br />" +
                  "Player id does not exist.")
  })
  @PostMapping("/move")
  public MoveResponseDTO addMove(@RequestBody @Validated MoveRequestDTO moveRequest) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    Player loggedPlayer = (Player) authentication.getPrincipal();
    return gameService.addMoveInGame(moveRequest, loggedPlayer);
  }
}
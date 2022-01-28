package de.takeaway.gameofthree.controllers;

import de.takeaway.gameofthree.dtos.PlayerDTO;
import de.takeaway.gameofthree.models.Player;
import de.takeaway.gameofthree.services.PlayerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players")
@Tag(name = "Player Controller", description = "Player related code")
public class PlayerController {

  private final PlayerService playerService;

  public PlayerController(PlayerService playerService) {
    this.playerService = playerService;
  }

  @Operation(summary = "Sign up",
          description = "Sign up in the game project")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Player created successfully"),
          @ApiResponse(responseCode = "400", description = "Username already exists.<br />" +
                  "Username cannot be null or empty.<br />" +
                  "Username must have less then 20 characters.<br />" +
                  "Password cannot be null or empty.<br />" +
                  "Password must have between 6 and 20 characters.")
  })
  @PostMapping
  public Player create(@RequestBody @Validated Player player) {
    return playerService.create(player);
  }

  @Operation(summary = "List of Players",
          description = "Get a list of available players to play against")
  @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "List of Players"),
          @ApiResponse(responseCode = "401", description = "Invalid authentication.")
  })
  @GetMapping
  public List<PlayerDTO> get() {
    return playerService.get();
  }
}
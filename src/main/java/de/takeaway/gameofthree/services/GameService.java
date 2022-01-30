package de.takeaway.gameofthree.services;

import de.takeaway.gameofthree.dtos.MoveRequestDTO;
import de.takeaway.gameofthree.dtos.MoveResponseDTO;
import de.takeaway.gameofthree.exceptions.InvalidInputException;
import de.takeaway.gameofthree.exceptions.NotFoundException;
import de.takeaway.gameofthree.models.Game;
import de.takeaway.gameofthree.models.Move;
import de.takeaway.gameofthree.models.Player;
import de.takeaway.gameofthree.repositories.GameRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class GameService {
  private final GameRepository gameRepository;
  private final MoveService moveService;
  private final PlayerService playerService;

  public GameService(GameRepository gameRepository, MoveService moveService,
                     PlayerService playerService) {
    this.gameRepository = gameRepository;
    this.moveService = moveService;
    this.playerService = playerService;
  }

  public MoveResponseDTO addMoveInGame(MoveRequestDTO moveRequest, Player loggedPlayer) {
    Game game = getGame(moveRequest, loggedPlayer);

    Move move = moveRequest.getGameId() == 0 ?
            moveService.buildFirstMoveForANewGame(moveRequest) :
            moveService.buildNextMoveForExistingGame(moveRequest, game);

    Player nextPlayer = getNextTurnPlayer(game);
    game.getMoves().add(move);
    if (nextPlayer.isAutomaticPlayEnabled()) {
      game.getMoves().add(moveService.buildAnAutomaticMove(move));
    }

    gameRepository.save(game);
    return MoveResponseDTO.builder().addedNumber(move.getAddedNumber())
            .resultingNumber(move.getNumber()).build();
  }

  private Game getGame(MoveRequestDTO moveRequest, Player loggedPlayer) {
    Game game = moveRequest.getGameId() > 0 ? findGameById(moveRequest.getGameId()) :
            buildNewGame(moveRequest, loggedPlayer);

    if (loggedPlayer.getId() != getCurrentTurnPlayer(game).getId()) {
      throw new InvalidInputException(String
              .format("It's not the turn of the player: %s", loggedPlayer.getUsername()));
    }

    return game;
  }

  private Game findGameById(long id) {
    Optional<Game> game = gameRepository.findById(id);
    if (game.isEmpty()) {
      throw new NotFoundException("Game id does not exist.");
    }

    return game.get();
  }

  private Game buildNewGame(MoveRequestDTO moveRequest, Player loggedPlayer) {
    if (loggedPlayer.getId() == moveRequest.getPlayerId()) {
      throw new InvalidInputException("Player cannot play alone.");
    }

    return Game.builder().player1(loggedPlayer).moves(new ArrayList<>())
            .player2(playerService.findById(moveRequest.getPlayerId())).build();
  }

  private Player getCurrentTurnPlayer(Game game) {
    return game.getMoves().size() % 2 == 0 ? game.getPlayer1() : game.getPlayer2();
  }

  private Player getNextTurnPlayer(Game game) {
    return game.getMoves().size() % 2 != 0 ? game.getPlayer1() : game.getPlayer2();
  }
}
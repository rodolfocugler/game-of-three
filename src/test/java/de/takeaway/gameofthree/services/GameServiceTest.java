package de.takeaway.gameofthree.services;

import de.takeaway.gameofthree.dtos.GameDTO;
import de.takeaway.gameofthree.dtos.MoveRequestDTO;
import de.takeaway.gameofthree.dtos.MoveResponseDTO;
import de.takeaway.gameofthree.dtos.PlayerDTO;
import de.takeaway.gameofthree.exceptions.InvalidInputException;
import de.takeaway.gameofthree.exceptions.NotFoundException;
import de.takeaway.gameofthree.models.Game;
import de.takeaway.gameofthree.models.Move;
import de.takeaway.gameofthree.models.Player;
import de.takeaway.gameofthree.repositories.GameRepository;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static java.util.Optional.of;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GameServiceTest {

  private final Player player1 = Player.builder().id(1).username("username1").build();
  private final Player player2 = Player.builder().id(2).build();

  @Mock
  private GameRepository gameRepository;

  @Mock
  private MoveService moveService;

  @Mock
  private PlayerService playerService;

  @InjectMocks
  private GameService gameService;

  @Test
  public void shouldAddAMoveToANewGameIfGameIdIsZero() {
    Move move = Move.builder().number(56).build();
    Game game = Game.builder().moves(Lists.newArrayList(move)).player1(player1).player2(player2)
            .build();

    Move dbMove = Move.builder().number(56).id(1).build();
    Game dbGame = Game.builder().moves(Lists.newArrayList(dbMove)).player1(player1).player2(player2)
            .id(1).build();

    MoveRequestDTO moveRequest = MoveRequestDTO.builder().playerId(player2.getId())
            .number(move.getNumber()).build();
    MoveResponseDTO dbMoveResponse = MoveResponseDTO.builder().addedNumber(move.getAddedNumber())
            .resultingNumber(move.getNumber()).build();

    when(gameRepository.save(game)).thenReturn(dbGame);
    when(playerService.findById(player2.getId())).thenReturn(player2);
    when(moveService.buildFirstMoveForANewGame(moveRequest)).thenReturn(move);

    MoveResponseDTO moveResponse = gameService.addMoveInGame(moveRequest, player1);

    assertThat(moveResponse).isEqualTo(dbMoveResponse);
  }

  @Test
  public void shouldAddAMoveToAnExistingGameIfGameIdIsNotZero() {
    ArrayList<Move> oldMoves = Lists.newArrayList(Move.builder().order(0).number(56).addedNumber(0)
            .build(), Move.builder().number(19).addedNumber(1).order(1).build());
    ArrayList<Move> newMoves = Lists.newArrayList(Move.builder().number(56).addedNumber(0)
                    .order(0).build(), Move.builder().order(1).number(19).addedNumber(1).build(),
            Move.builder().number(6).addedNumber(-1).order(2).build());
    ArrayList<Move> dbMoves = Lists.newArrayList(Move.builder().number(56).addedNumber(0)
                    .order(0).build(), Move.builder().order(1).number(19).addedNumber(1).build(),
            Move.builder().number(6).addedNumber(-1).id(3).order(2).build());

    Game oldGame = Game.builder().moves(oldMoves).player1(player1).player2(player2).id(1).build();
    Game newGame = Game.builder().moves(newMoves).player1(player1).player2(player2).id(1).build();
    Game dbGame = Game.builder().moves(dbMoves).player1(player1).player2(player2).id(1).build();

    MoveRequestDTO moveRequest = MoveRequestDTO.builder().playerId(player2.getId())
            .number(6).gameId(1).build();
    MoveResponseDTO expectedMoveResponse = MoveResponseDTO.builder().addedNumber(-1)
            .resultingNumber(6).build();

    when(gameRepository.findById(newGame.getId())).thenReturn(of(oldGame));
    when(moveService.buildNextMoveForExistingGame(moveRequest, oldGame))
            .thenReturn(newMoves.get(newMoves.size() - 1));
    when(gameRepository.save(newGame)).thenReturn(dbGame);

    MoveResponseDTO moveResponse = gameService.addMoveInGame(moveRequest, player1);

    assertThat(moveResponse).isEqualTo(expectedMoveResponse);
  }

  @Test
  public void shouldPropagateExceptionIfMoveServiceThrowsException() {
    MoveRequestDTO moveRequest = MoveRequestDTO.builder().playerId(player1.getId())
            .number(18).gameId(1).build();
    Game game = Game.builder().id(1).moves(Lists.newArrayList(Move.builder().build()))
            .player1(player1).player2(player2).build();

    when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));
    when(moveService.buildNextMoveForExistingGame(moveRequest, game))
            .thenThrow(new RuntimeException());

    assertThrows(RuntimeException.class, () -> gameService.addMoveInGame(moveRequest, player2));
  }

  @Test
  public void shouldThrowInvalidInputExceptionIfPlayerTryToPlayAlone() {
    MoveRequestDTO moveRequest = MoveRequestDTO.builder().playerId(player1.getId())
            .number(18).build();

    assertThrows(InvalidInputException.class, () -> gameService.addMoveInGame(moveRequest, player1));
  }

  @Test
  public void shouldThrowNotFoundExceptionIfGameDoesNotExist() {
    MoveRequestDTO moveRequest = MoveRequestDTO.builder().playerId(player1.getId())
            .number(18).gameId(1).build();

    when(gameRepository.findById(1L)).thenReturn(Optional.empty());

    assertThrows(NotFoundException.class, () -> gameService.addMoveInGame(moveRequest, player1));
  }

  @Test
  public void shouldPropagateExceptionIfPlayerServiceThrowsException() {
    MoveRequestDTO moveRequest = MoveRequestDTO.builder().playerId(player2.getId())
            .number(18).gameId(0).build();

    when(playerService.findById(player2.getId())).thenThrow(new InvalidInputException("dummy"));
    assertThrows(RuntimeException.class, () -> gameService.addMoveInGame(moveRequest, player1));
  }

  @Test
  public void shouldThrowInvalidInputExceptionIfPlayerMakeAMoveWhenItIsNotHisTurn() {
    MoveRequestDTO moveRequest = MoveRequestDTO.builder().playerId(player2.getId())
            .number(6).gameId(1).build();
    Game game = Game.builder().id(1).player1(player1).player2(player2)
            .moves(Lists.newArrayList(Move.builder().number(18).build())).build();

    when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));

    assertThrows(InvalidInputException.class, () -> gameService.addMoveInGame(moveRequest, player1));
  }

  @Test
  public void shouldAddANewMoveIfTheNextPlayerCanPlayAutomatic() {
    Player automaticPlayer2 = Player.builder().id(2).isAutomaticPlayEnabled(true).build();

    ArrayList<Move> oldMoves = Lists.newArrayList(Move.builder().order(0).number(56).addedNumber(0)
            .build());
    ArrayList<Move> newMoves = Lists.newArrayList(Move.builder().number(56).addedNumber(0)
                    .order(0).build(), Move.builder().order(1).number(19).addedNumber(1).build(),
            Move.builder().number(6).addedNumber(-1).order(2).build());
    ArrayList<Move> dbMoves = Lists.newArrayList(Move.builder().number(56).addedNumber(0)
                    .order(0).build(), Move.builder().order(1).number(19).addedNumber(1).build(),
            Move.builder().number(6).addedNumber(-1).id(3).order(2).build());

    Game oldGame = Game.builder().moves(oldMoves).player1(automaticPlayer2).player2(player1).id(1)
            .build();
    Game newGame = Game.builder().moves(newMoves).player1(automaticPlayer2).player2(player1).id(1)
            .build();
    Game dbGame = Game.builder().moves(dbMoves).player1(automaticPlayer2).player2(player1).id(1)
            .build();

    MoveRequestDTO moveRequest = MoveRequestDTO.builder().playerId(automaticPlayer2.getId())
            .number(19).gameId(1).build();
    MoveResponseDTO expectedMoveResponse = MoveResponseDTO.builder().resultingNumber(19)
            .addedNumber(1).build();

    when(gameRepository.findById(newGame.getId())).thenReturn(of(oldGame));
    when(moveService.buildNextMoveForExistingGame(moveRequest, oldGame))
            .thenReturn(newMoves.get(newMoves.size() - 2));
    when(moveService.buildAnAutomaticMove(newMoves.get(newMoves.size() - 2)))
            .thenReturn(newMoves.get(newMoves.size() - 1));
    when(gameRepository.save(newGame)).thenReturn(dbGame);

    MoveResponseDTO moveResponse = gameService.addMoveInGame(moveRequest, player1);

    assertThat(moveResponse).isEqualTo(expectedMoveResponse);
  }

  @Test
  public void shouldNotAddANewMoveIfTheNextPlayerCanPlayAutomaticButGameIsOver() {
    Player automaticPlayer2 = Player.builder().id(2).isAutomaticPlayEnabled(true).build();

    ArrayList<Move> oldMoves = Lists.newArrayList(Move.builder().order(0).number(3).addedNumber(0)
            .build());
    ArrayList<Move> newMoves = Lists.newArrayList(Move.builder().number(3).addedNumber(0)
            .order(0).build(), Move.builder().order(1).number(1).addedNumber(1).build());
    ArrayList<Move> dbMoves = Lists.newArrayList(Move.builder().number(3).addedNumber(0)
            .order(0).build(), Move.builder().order(1).number(1).addedNumber(1).build());

    Game oldGame = Game.builder().moves(oldMoves).player1(automaticPlayer2).player2(player1).id(1)
            .build();
    Game newGame = Game.builder().moves(newMoves).player1(automaticPlayer2).player2(player1).id(1)
            .winner(player1).build();
    Game dbGame = Game.builder().moves(dbMoves).player1(automaticPlayer2).player2(player1).id(1)
            .winner(player1).build();

    MoveRequestDTO moveRequest = MoveRequestDTO.builder().playerId(automaticPlayer2.getId())
            .number(1).gameId(1).build();
    MoveResponseDTO expectedMoveResponse = MoveResponseDTO.builder().resultingNumber(1)
            .addedNumber(1).build();

    when(gameRepository.findById(newGame.getId())).thenReturn(of(oldGame));
    when(moveService.buildNextMoveForExistingGame(moveRequest, oldGame))
            .thenReturn(newMoves.get(newMoves.size() - 1));
    when(gameRepository.save(newGame)).thenReturn(dbGame);

    MoveResponseDTO moveResponse = gameService.addMoveInGame(moveRequest, player1);

    assertThat(moveResponse).isEqualTo(expectedMoveResponse);
  }

  @Test
  public void shouldThrowInvalidInputExceptionIfGameIsOver() {
    MoveRequestDTO moveRequest = MoveRequestDTO.builder().playerId(player2.getId())
            .number(6).gameId(1).build();
    Game game = Game.builder().id(1).player1(player1).player2(player2).winner(player1).build();

    when(gameRepository.findById(game.getId())).thenReturn(Optional.of(game));

    assertThrows(InvalidInputException.class, () -> gameService.addMoveInGame(moveRequest, player1));
  }

  @Test
  public void shouldReturnAListOfAvailableGames() {
    ArrayList<Move> moves = Lists.newArrayList(Move.builder().order(0).number(3).addedNumber(0)
            .build());
    Game game = Game.builder().id(1).player1(player1).player2(player2).moves(moves)
            .build();
    PlayerDTO player1Dto = PlayerDTO.builder().id(1).username("username1").build();
    PlayerDTO player2Dto = PlayerDTO.builder().id(2).build();
    GameDTO gameDTO = GameDTO.builder().moves(moves).id(1).player1(player1Dto).player2(player2Dto)
            .build();

    when(playerService.mapToPlayerDTO(player1)).thenReturn(player1Dto);
    when(playerService.mapToPlayerDTO(player2)).thenReturn(player2Dto);
    when(gameRepository.findAllByPlayer1IdOrPlayer2Id_AndWinnerIsNull(1, 1))
            .thenReturn(List.of(game));

    assertThat(gameService.getAvailableGames(player1)).isEqualTo(List.of(gameDTO));
  }

  @Test
  public void shouldReturnAListOfAllGames() {
    ArrayList<Move> moves = Lists.newArrayList(Move.builder().order(0).number(3).addedNumber(0)
            .build());
    Game game = Game.builder().id(1).player1(player1).player2(player2).winner(player1).moves(moves)
            .build();
    PlayerDTO player1Dto = PlayerDTO.builder().id(1).username("username1").build();
    PlayerDTO player2Dto = PlayerDTO.builder().id(2).build();
    GameDTO gameDTO = GameDTO.builder().moves(moves).id(1).player1(player1Dto).player2(player2Dto)
            .winner(player1Dto).build();

    when(playerService.mapToPlayerDTO(player1)).thenReturn(player1Dto);
    when(playerService.mapToPlayerDTO(player2)).thenReturn(player2Dto);
    when(gameRepository.findAllByPlayer1IdOrPlayer2Id(1, 1)).thenReturn(List.of(game));

    assertThat(gameService.getAllGames(player1)).isEqualTo(List.of(gameDTO));
  }
}
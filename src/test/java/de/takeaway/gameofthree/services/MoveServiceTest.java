package de.takeaway.gameofthree.services;

import de.takeaway.gameofthree.dtos.MoveRequestDTO;
import de.takeaway.gameofthree.exceptions.InvalidInputException;
import de.takeaway.gameofthree.models.Game;
import de.takeaway.gameofthree.models.Move;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class MoveServiceTest {

  @InjectMocks
  private MoveService moveService;

  @Test
  public void shouldBuildTheFirstMove() {
    MoveRequestDTO moveRequest = MoveRequestDTO.builder().number(56).build();
    Move expectedMove = Move.builder().number(56).addedNumber(0).build();

    Move move = moveService.buildFirstMoveForANewGame(moveRequest);

    assertThat(move).isEqualTo(expectedMove);
  }

  @Test
  public void shouldAddAMoveAccordingToAnExistingGame() {
    ArrayList<Move> oldMoves = Lists.newArrayList(Move.builder().number(19).addedNumber(1).order(1)
            .build(), Move.builder().order(0).number(56).addedNumber(0).build());
    Game oldGame = Game.builder().moves(oldMoves).id(1).build();
    MoveRequestDTO moveRequest = MoveRequestDTO.builder().number(6).gameId(1).build();
    Move expectedMove = Move.builder().number(6).addedNumber(-1).order(2).build();

    Move move = moveService.buildNextMoveForExistingGame(moveRequest, oldGame);

    assertThat(move).isEqualTo(expectedMove);
  }

  @Test
  public void shouldThrowInvalidInputExceptionIfNumberIsInvalid() {
    ArrayList<Move> oldMoves = Lists.newArrayList(Move.builder().order(0).number(56).addedNumber(0)
            .build());

    Game oldGame = Game.builder().moves(oldMoves).id(1).build();

    MoveRequestDTO moveRequest = MoveRequestDTO.builder().number(18).gameId(1).build();

    assertThrows(InvalidInputException.class,
            () -> moveService.buildNextMoveForExistingGame(moveRequest, oldGame));
  }

  @Test
  public void shouldThrowInvalidInputExceptionIfNumberIsLessThan4() {
    MoveRequestDTO moveRequest = MoveRequestDTO.builder().number(3).gameId(1).build();

    assertThrows(InvalidInputException.class,
            () -> moveService.buildFirstMoveForANewGame(moveRequest));
  }
}
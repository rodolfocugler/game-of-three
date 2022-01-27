package de.takeaway.gameofthree.services;

import de.takeaway.gameofthree.dtos.MoveRequestDTO;
import de.takeaway.gameofthree.exceptions.InvalidInputException;
import de.takeaway.gameofthree.models.Game;
import de.takeaway.gameofthree.models.Move;
import org.springframework.stereotype.Service;

import java.util.Comparator;

@Service
public class MoveService {

  public Move buildFirstMoveForANewGame(MoveRequestDTO moveRequest) {
    return Move.builder().number(moveRequest.getNumber()).build();
  }

  public Move buildNextMoveForExistingGame(MoveRequestDTO moveRequest, Game game) {
    game.getMoves().sort(Comparator.comparing(Move::getOrder));

    Move lastMove = game.getMoves().get(game.getMoves().size() - 1);
    int addedNumber = moveRequest.getNumber() * 3 - lastMove.getNumber();

    if (addedNumber < -1 || addedNumber > 1) {
      throw new InvalidInputException("Added number must be {-1, 0 or 1}.");
    }

    return Move.builder().number(moveRequest.getNumber()).addedNumber(addedNumber)
            .order(lastMove.getOrder() + 1).build();
  }
}
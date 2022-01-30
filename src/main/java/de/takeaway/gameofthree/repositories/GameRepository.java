package de.takeaway.gameofthree.repositories;

import de.takeaway.gameofthree.models.Game;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {
  List<Game> findAllByPlayer1IdOrPlayer2Id_AndWinnerIsNull(long playerId1, long playerId2);

  List<Game> findAllByPlayer1IdOrPlayer2Id(long playerId1, long playerId2);
}
package de.takeaway.gameofthree.repositories;

import de.takeaway.gameofthree.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlayerRepository extends JpaRepository<Player, Long> {
  Player findByUsername(String username);
}
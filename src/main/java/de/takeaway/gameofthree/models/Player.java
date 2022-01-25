package de.takeaway.gameofthree.models;

import lombok.*;

import javax.persistence.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
public class Player {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id = 0;

  @Column(nullable = false, length = 20, unique = true)
  private String username;
}


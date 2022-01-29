package de.takeaway.gameofthree.integrations;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.takeaway.gameofthree.dtos.MoveRequestDTO;
import de.takeaway.gameofthree.models.Player;
import de.takeaway.gameofthree.utils.AuthenticationUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GameIntegrationTest {
  private final ObjectMapper mapper = new ObjectMapper();
  @Autowired
  private WebApplicationContext webApplicationContext;
  private MockMvc mockMvc;

  @BeforeEach
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    AuthenticationUtil.setAuthentication(1);
  }

  @Test
  public void shouldReturnAMoveWithNewGameIdWhenPlayerInitiateANewGame() throws Exception {
    createPlayers(2);

    MoveRequestDTO moveRequestDTO = MoveRequestDTO.builder().number(6).playerId(2).build();

    this.mockMvc.perform(post("/api/games/move")
            .content(mapper.writeValueAsString(moveRequestDTO))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.addedNumber").value(0))
            .andExpect(jsonPath("$.resultingNumber").value(6));
  }

  @Test
  public void shouldReturnAMoveWhenPlayerMakeAMoveInAnExistingGame() throws Exception {
    createPlayers(2);

    this.mockMvc.perform(post("/api/games/move")
            .content(mapper.writeValueAsString(MoveRequestDTO.builder().number(7).playerId(2)
                    .build()))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

    MoveRequestDTO moveRequestDTO = MoveRequestDTO.builder().number(2).gameId(1).build();
    AuthenticationUtil.setAuthentication(2);
    this.mockMvc.perform(post("/api/games/move")
            .content(mapper.writeValueAsString(moveRequestDTO))
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.addedNumber").value(-1))
            .andExpect(jsonPath("$.resultingNumber").value(2));
  }

  private void createPlayers(int quantity) throws Exception {
    for (int i = 0; i < quantity; i++) {
      Player player = Player.builder().username(String.format("Username%s", i)).password("123456")
              .build();
      this.mockMvc.perform(post("/api/players")
              .content(mapper.writeValueAsString(player))
              .contentType(MediaType.APPLICATION_JSON))
              .andExpect(status().isOk());
    }
  }

}
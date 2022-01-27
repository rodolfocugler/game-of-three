package de.takeaway.gameofthree.integrations;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.takeaway.gameofthree.configs.SpringSecurityTestConfig;
import de.takeaway.gameofthree.dtos.MoveRequestDTO;
import de.takeaway.gameofthree.models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SpringSecurityTestConfig.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class GameIntegrationTest {
  private final ObjectMapper mapper = new ObjectMapper();
  @Autowired
  private WebApplicationContext webApplicationContext;
  private MockMvc mockMvc;

  @BeforeEach
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
  }

  @Test
  @WithUserDetails("username1")
  public void shouldReturnAMoveWithNewGameIdWhenPlayerInitiateANewGame() throws Exception {
    createPlayers(2);

    MoveRequestDTO moveRequestDTO = MoveRequestDTO.builder().number(1).playerId(1).build();

    this.mockMvc.perform(post("/api/games/move")
            .content(mapper.writeValueAsString(moveRequestDTO))
            .header("Authorization", "Bearer zasdasdasd")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1));
  }

  private void createPlayers(int quantity) throws Exception {
    for (int i = 0; i < quantity; i++) {
      Player player = Player.builder().username(String.format("Username%s", i)).password("123456")
              .build();
      this.mockMvc.perform(post("/api/games/move")
              .content(mapper.writeValueAsString(player))
              .contentType(MediaType.APPLICATION_JSON))
              .andExpect(status().isOk());
    }
  }
}
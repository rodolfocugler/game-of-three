package de.takeaway.gameofthree.integrations;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.takeaway.gameofthree.models.Player;
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@WebAppConfiguration
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PlayerIntegrationTest {
  @Autowired
  private WebApplicationContext webApplicationContext;
  private MockMvc mockMvc;
  private final ObjectMapper mapper = new ObjectMapper();
  private final Player player = Player.builder().username("username").password("123456").build();

  @BeforeEach
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
  }

  @Test
  public void shouldReturnNewPlayerWhenANewPlayerRegisterYourself() throws Exception {
    this.mockMvc.perform(post("/api/players")
            .content(mapper.writeValueAsString(player))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value(player.getUsername()))
            .andExpect(jsonPath("$.id").value(1));
  }

  @Test
  public void shouldThrowInvalidInputExceptionIfPlayerAlreadyExists() throws Exception {
    this.mockMvc.perform(post("/api/players")
            .content(mapper.writeValueAsString(player))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());

    this.mockMvc.perform(post("/api/players")
            .content(mapper.writeValueAsString(player))
            .contentType(MediaType.APPLICATION_JSON))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(jsonPath("$.message").value("Username already exists."))
            .andExpect(status().isBadRequest());
  }
}

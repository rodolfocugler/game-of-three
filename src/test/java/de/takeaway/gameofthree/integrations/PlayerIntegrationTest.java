package de.takeaway.gameofthree.integrations;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.takeaway.gameofthree.controllers.PlayerController;
import de.takeaway.gameofthree.models.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {PlayerController.class})
@WebAppConfiguration
public class PlayerIntegrationTest {
  @Autowired
  private WebApplicationContext webApplicationContext;
  private MockMvc mockMvc;
  private final ObjectMapper mapper = new ObjectMapper();
  private final Player player = new Player();

  @BeforeEach
  public void setup() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
  }

  @Test
  public void shouldReturnNewPlayerWhenANewPlayerRegisterYourself() throws Exception {
    this.mockMvc.perform(post("/api/player")
            .content(mapper.writeValueAsString(player)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.username").value(player.getUsername()))
            .andExpect(jsonPath("$.id").value(1));
  }
}

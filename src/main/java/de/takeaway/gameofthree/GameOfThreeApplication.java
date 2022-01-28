package de.takeaway.gameofthree;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "Game Of Three", version = "1.0"))
@SecurityScheme(name = "Authorization", scheme = "bearer", type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT", in = SecuritySchemeIn.HEADER)
public class GameOfThreeApplication {

  public static void main(String[] args) {
    SpringApplication.run(GameOfThreeApplication.class, args);
  }

}
package ru.team2.lookingforhouse;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition
public class LookingForHouseApplication {

    public static void main(String[] args) {
        SpringApplication.run(LookingForHouseApplication.class, args);
    }

}

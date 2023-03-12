package ru.team2.lookingforhouse.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.team2.lookingforhouse.model.Animal;
import ru.team2.lookingforhouse.service.AnimalService;
import ru.team2.lookingforhouse.service.ValidateService;

@Tag(name="ANIMAL",description = "API для животного")
@RestController
@RequestMapping("animal")
public class AnimalController {
    private final AnimalService animalService;
    private final ValidateService validateService;
    public AnimalController(AnimalService animalService, ValidateService validateService) {
        this.animalService = animalService;
        this.validateService = validateService;
    }

    @Operation(summary = "Получение животного по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Животное, найденное по id",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Animal.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400",
                            description = "Животное по данному id не нашлась!")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Animal> getById(@Parameter(description = "id животного", example = "956120008L")
                                       @PathVariable Long id) {
        return ResponseEntity.of(animalService.getById(id));
    }
    @Operation(summary = "Создание животного",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody (
                    description = "Созданное животное",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Animal.class)
                    )
            )
    )
    @PostMapping()
    public ResponseEntity<Animal> create(@RequestBody Animal animal) {
        if (validateService.isNotValid(animal)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(animalService.create(animal));
    }
}

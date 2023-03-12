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
import ru.team2.lookingforhouse.model.Dog;
import ru.team2.lookingforhouse.service.DogService;
import ru.team2.lookingforhouse.service.ValidateService;

@Tag(name="DOG",description = "API для собаки")
@RestController
@RequestMapping("dog")
public class DogController {
    private final DogService dogService;
    private final ValidateService validateService;

    public DogController(DogService dogService, ValidateService validateService) {
        this.dogService = dogService;
        this.validateService = validateService;
    }

    @Operation(summary = "Получение собаки по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Собака, найденная по id",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Dog.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400",
                            description = "Собака по данному id не нашлась!")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Dog> getById(@Parameter(description = "id собаки", example = "956120008L")
                                       @PathVariable Long id) {
        return ResponseEntity.of(dogService.getById(id));
    }
    @Operation(summary = "Создание собаки",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody (
                    description = "Созданная собака",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Dog.class)
                    )
            )
    )
    @PostMapping()
    public ResponseEntity<Dog> create(@RequestBody Dog dog) {
        if (validateService.isNotValid(dog)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(dogService.create(dog));
    }
}

package ru.team2.lookingforhouse.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.team2.lookingforhouse.model.Animal;
import ru.team2.lookingforhouse.service.AnimalService;


/**
 * Класс контроллера объекта "Животное"
 *
 * @author Одокиенко Екатерина
 */

@Tag(name = "ANIMAL", description = "API для объекта \"Животное\"")
@RestController
@RequestMapping("animal")
public class AnimalController {
    private final AnimalService animalService;

    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @Operation(summary = "Получение объекта \"Животное\" по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Объект \"Животное\", найденный по id",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Animal.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400",
                            description = "Объект \"Животное\" по данному id не найден!")
            }
    )
    @GetMapping("/{id}")
    public Animal getById(@Parameter(description = "id объекта \"Животное\"", example = "956120008L")
                          @PathVariable Long id) {
        return animalService.getById(id);
    }

    @Operation(summary = "Создание объекта \"Животное\"",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Созданный объект \"Животное\"",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Animal.class)
                    )
            )
    )
    @PostMapping()
    public Animal create(@RequestBody Animal animal) {
        return this.animalService.create(animal);
    }
}

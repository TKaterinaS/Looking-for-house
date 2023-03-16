package ru.team2.lookingforhouse.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.team2.lookingforhouse.service.DogService;

/**
 * Класс контроллера объекта "Собака"
 * @author Одокиенко Екатерина
 */


@Tag(name="DOG",description = "API для объекта \"Собака\"")
@RestController
@RequestMapping("dog")
public class DogController {
    private final DogService dogService;
    public DogController(DogService dogService) {
        this.dogService = dogService;
    }

    @Operation(summary = "Получение объекта \"Собака\" по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Объект \"Собака\", найденный по id",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Dog.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400",
                            description = "Объект \"Собака\" по данному id не найден!")
            }
    )
    @GetMapping("/{id}")
    public Dog getById(@Parameter(description = "id объекта \"Собака\"", example = "956120008L")
                                       @PathVariable Long id) {
        return dogService.getById(id);
    }
    @Operation(summary = "Создание объекта \"Собака\"",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody (
                    description = "Созданный объекта \"Собака\"",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Dog.class)
                    )
            )
    )
    @PostMapping()
    public Dog create(@RequestBody Dog dog) {
        return this.dogService.create(dog);
    }
}

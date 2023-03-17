package ru.team2.lookingforhouse.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.team2.lookingforhouse.model.AdopterDog;
import ru.team2.lookingforhouse.service.AdopterDogService;


/**
 * Класс контроллера объекта "Усыновитель собаки"
 *
 * @author Одокиенко Екатерина
 */

@Tag(name = "ADOPTER DOG", description = "API для объекта \"Усыновитель собаки\"")
@RestController
@RequestMapping("adopter_dog")
public class AdopterDogController {
    private final AdopterDogService adopterDogService;
    public AdopterDogController(AdopterDogService adopterDogService) {
        this.adopterDogService = adopterDogService;
    }
    @Operation(summary = "Получение объекта \"Усыновитель собаки\" по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Объект \"Усыновитель собаки\", найденный по id",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AdopterDog.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400",
                            description = "Объект \"Усыновитель собаки\" по данному id не найден!")
            }
    )
    @GetMapping("/{id}")
    public AdopterDog getById(@Parameter(description = "id объекта \"Усыновитель собаки\"", example = "956120008L")
                              @PathVariable Long id) {
        return adopterDogService.getById(id);
    }

    @Operation(summary = "Создание объекта \"Усыновитель собаки\"",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Созданный объект \"Усыновитель собаки\"",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AdopterDog.class)
                    )
            )
    )
    @PostMapping()
    public AdopterDog create(@RequestBody AdopterDog adopterDog) {
        return this.adopterDogService.create(adopterDog);
    }
}

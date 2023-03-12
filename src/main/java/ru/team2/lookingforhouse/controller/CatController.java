package ru.team2.lookingforhouse.controller;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.team2.lookingforhouse.model.Cat;
import ru.team2.lookingforhouse.service.CatService;


/**
 * Класс контроллера объекта "Кот"
 *
 * @author Одокиенко Екатерина
 */

@Tag(name = "CAT", description = "API для объекта \"Кот\"")
@RestController
@RequestMapping("сat")
public class CatController {
    private final CatService catService;

    public CatController(CatService catService) {
        this.catService = catService;
    }

    @Operation(summary = "Получение объекта \"Кот\" по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Объект \"Кот\", найденный по id",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Cat.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400",
                            description = "Объект \"Кот\" по данному id не найден!")
            }
    )
    @GetMapping("/{id}")
    public Cat getById(@Parameter(description = "id объекта \"Кот\"", example = "956120008L")
                       @PathVariable Long id) {
        return catService.getById(id);
    }

    @Operation(summary = "Создание объекта \"Кот\"",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Созданный объект \"Кот\"",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Cat.class)
                    )
            )
    )
    @PostMapping()
    public Cat create(@RequestBody Cat cat) {
        return this.catService.create(cat);
    }
}

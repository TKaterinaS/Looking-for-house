package ru.team2.lookingforhouse.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.team2.lookingforhouse.model.AdopterCat;
import ru.team2.lookingforhouse.service.AdopterCatService;

/**
 * Класс контроллера объекта "Усыновитель кота"
 *
 * @author Одокиенко Екатерина
 */

@Tag(name = "ADOPTER CAT", description = "API для объекта \"Усыновитель кота\"")
@RestController
@RequestMapping("adopter_cat")
public class AdopterCatController {
    private final AdopterCatService adopterCatService;
    public AdopterCatController(AdopterCatService adopterCatService) {
        this.adopterCatService = adopterCatService;
    }
    @Operation(summary = "Получение объекта \"Усыновитель кота\" по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Объект \"Усыновитель кота\", найденный по id",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = AdopterCat.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400",
                            description = "Объект \"Усыновитель кота\" по данному id не найден!")
            }
    )
    @GetMapping("/{id}")
    public AdopterCat getById(@Parameter(description = "id объекта \"Усыновитель кота\"", example = "956120008L")
                        @PathVariable Long id) {
        return adopterCatService.getById(id);
    }

    @Operation(summary = "Создание объекта \"Усыновитель кота\"",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Созданный объект \"Усыновитель кота\"",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = AdopterCat.class)
                    )
            )
    )
    @PostMapping()
    public AdopterCat create(@RequestBody AdopterCat adopterCat) {
        return this.adopterCatService.create(adopterCat);
    }
}

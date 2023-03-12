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
import ru.team2.lookingforhouse.model.Cat;
import ru.team2.lookingforhouse.service.CatService;
import ru.team2.lookingforhouse.service.ValidateService;


@Tag(name="CAT",description = "API для кошки")
@RestController
@RequestMapping("сat")
public class CatController {
    private final CatService catService;
    private final ValidateService validateService;

    public CatController(CatService catService, ValidateService validateService) {
        this.catService = catService;
        this.validateService = validateService;
    }

    @Operation(summary = "Получение кошки по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Кошка, найденная по id",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Cat.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400",
                            description = "Кошка по данному id не нашлась!")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<Cat> getById(@Parameter(description = "id кошки", example = "956120008L")
                                           @PathVariable Long id) {
        return ResponseEntity.of(catService.getById(id));
    }
    @Operation(summary = "Создание кошки",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody (
                    description = "Созданная кошка",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = Cat.class)
                    )
            )
    )
    @PostMapping()
    public ResponseEntity<Cat> create(@RequestBody Cat cat) {
        if (validateService.isNotValid(cat)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(catService.create(cat));
    }

}

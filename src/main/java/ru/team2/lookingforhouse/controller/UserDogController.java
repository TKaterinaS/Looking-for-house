package ru.team2.lookingforhouse.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.team2.lookingforhouse.model.UserDog;
import ru.team2.lookingforhouse.service.UserDogService;


/**
 * Класс контроллера объекта "Пользователь, интересующийся собакой"
 *
 * @author Одокиенко Екатерина
 */

@Tag(name = "USER DOG", description = "API для объекта \"Пользователь, интересующийся собакой\"")
@RestController
@RequestMapping("user_dog")
public class UserDogController {
    private final UserDogService userDogService;

    public UserDogController(UserDogService userDogService) {
        this.userDogService = userDogService;
    }

    @Operation(summary = "Получение объекта \"Пользователь, интересующийся собакой\" по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Объект \"Пользователь, интересующийся собакой\", найденный по id",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserDog.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400",
                            description = "Объект \"Пользователь, интересующийся собакой\" по данному id не найден!")
            }
    )
    @GetMapping("/{id}")
    public UserDog getById(@Parameter(description = "id объекта \"Пользователь, интересующийся собакой\"", example = "956120008L")
                           @PathVariable Long id) {
        return userDogService.getById(id);
    }

    @Operation(summary = "Создание объекта \"Пользователь, интересующийся собакой\"",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Созданный объект \"Пользователь, интересующийся собакой\"",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserDog.class)
                    )
            )
    )
    @PostMapping()
    public UserDog create(@RequestBody UserDog userDog) {
        return this.userDogService.create(userDog);
    }
}

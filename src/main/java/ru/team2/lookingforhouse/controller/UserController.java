package ru.team2.lookingforhouse.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.team2.lookingforhouse.model.User;
import ru.team2.lookingforhouse.service.UserService;

/**
 * Класс контроллера объекта "Пользователь"
 *
 * @author Одокиенко Екатерина
 */

@Tag(name = "USER", description = "API для объекта \"Пользователь\"")
@RestController
@RequestMapping("user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Получение объекта \"Пользователь\" по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Объект \"Пользователь\", найденный по id",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = User.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400",
                            description = "Объект \"Пользователь\" по данному id не найден!")
            }
    )
    @GetMapping("/{id}")
    public User getById(@Parameter(description = "id объекта \"Пользователь\"", example = "956120008L")
                        @PathVariable Long id) {
        return userService.getById(id);
    }

    @Operation(summary = "Создание объекта \"Пользователь\"",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Созданный объекта \"Пользователь\"",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = User.class)
                    )
            )
    )
    @PostMapping()
    public User create(@RequestBody User user) {
        return this.userService.create(user);
    }
}

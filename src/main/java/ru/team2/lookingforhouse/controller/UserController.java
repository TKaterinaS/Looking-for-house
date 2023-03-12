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
import ru.team2.lookingforhouse.model.User;
import ru.team2.lookingforhouse.service.UserService;
import ru.team2.lookingforhouse.service.ValidateService;

@Tag(name = "USER", description = "API для пользователя")
@RestController
@RequestMapping("user")
public class UserController {
    private final UserService userService;
    private final ValidateService validateService;

    public UserController(UserService userService, ValidateService validateService) {
        this.userService = userService;
        this.validateService = validateService;
    }

    @Operation(summary = "Получение пользователя по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Пользователь, найденный по id",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = User.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400",
                            description = "Пользователя по данному id не нашли!")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<User> getById(@Parameter(description = "id пользователя", example = "956120008L")
                                        @PathVariable Long id) {
        return ResponseEntity.of(userService.getById(id));
    }

    @Operation(summary = "Создание пользователя",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Созданный пользователь",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = User.class)
                    )
            )
    )
    @PostMapping()
    public ResponseEntity<User> create(@RequestBody User user) {
        if (validateService.isNotValid(user)) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(userService.create(user));
    }

}

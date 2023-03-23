package ru.team2.lookingforhouse.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.team2.lookingforhouse.model.UserCat;
import ru.team2.lookingforhouse.service.UserCatService;


/**
 * Класс контроллера объекта "Пользователь, интересующийся котом"
 *
 * @author Одокиенко Екатерина
 */

@Tag(name = "USER CAT", description = "API для объекта \"Пользователь, интересующийся котом\"")
@RestController
@RequestMapping("user_cat")
public class UserCatController {
    private final UserCatService userCatService;

    public UserCatController(UserCatService userCatService) {
        this.userCatService = userCatService;
    }

    @Operation(summary = "Получение объекта \"Пользователь, интересующийся котом\" по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Объект \"Пользователь, интересующийся котом\", найденный по id",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = UserCat.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400",
                            description = "Объект \"Пользователь, интересующийся котом\" по данному id не найден!")
            }
    )
    @GetMapping("/{chatId}")
    public UserCat getByChatId(@Parameter(description = "id объекта \"Пользователь, интересующийся котом\"", example = "956120008L")
                           @PathVariable Long chatId) {
        return (UserCat) userCatService.findByChatId(chatId);
    }

    @Operation(summary = "Создание объекта \"Пользователь, интересующийся котом\"",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Созданный объект \"Пользователь, интересующийся котом\"",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = UserCat.class)
                    )
            )
    )
    @PostMapping()
    public UserCat create(@RequestBody UserCat userCat) {
        return this.userCatService.create(userCat);
    }
}

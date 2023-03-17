package ru.team2.lookingforhouse.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.team2.lookingforhouse.model.ContactUserCat;
import ru.team2.lookingforhouse.service.ContactUserCatService;


/**
 * Класс контроллера объекта "Контакты пользователя, интересующегося котом"
 *
 * @author Одокиенко Екатерина
 */

@Tag(name = "CONTACT USER CAT", description = "API для объекта \"Контакты пользователя, интересующегося котом\"")
@RestController
@RequestMapping("contact_user_cat")
public class ContactUserCatController {
    private final ContactUserCatService contactUserCatService;
    public ContactUserCatController(ContactUserCatService contactUserCatService) {
        this.contactUserCatService = contactUserCatService;
    }
    @Operation(summary = "Получение объекта \"Контакты пользователя, интересующегося котом\" по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Объект \"Контакты пользователя, интересующегося котом\", найденный по id",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ContactUserCat.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400",
                            description = "Объект \"Контакты пользователя, интересующегося котом\" по данному id не найден!")
            }
    )
    @GetMapping("/{id}")
    public ContactUserCat getById(@Parameter(description = "id объекта \"Контакты пользователя, интересующегося котом\"", example = "956120008L")
                              @PathVariable Long id) {
        return contactUserCatService.getById(id);
    }

    @Operation(summary = "Создание объекта \"Контакты пользователя, интересующегося котом\"",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Созданный объект \"Контакты пользователя, интересующегося котом\"",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ContactUserCat.class)
                    )
            )
    )
    @PostMapping()
    public ContactUserCat create(@RequestBody ContactUserCat contactUserCat) {
        return this.contactUserCatService.create(contactUserCat);
    }

}

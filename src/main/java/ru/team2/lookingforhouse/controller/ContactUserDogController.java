package ru.team2.lookingforhouse.controller;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import ru.team2.lookingforhouse.model.ContactUserDog;
import ru.team2.lookingforhouse.service.ContactUserDogService;


/**
 * Класс контроллера объекта "Контакты пользователя, интересующегося собакой"
 *
 * @author Одокиенко Екатерина
 */

@Tag(name = "CONTACT USER DOG", description = "API для объекта \"Контакты пользователя, интересующегося собакой\"")
@RestController
@RequestMapping("contact_user_dog")
public class ContactUserDogController {
    private final ContactUserDogService contactUserDogService;
    public ContactUserDogController(ContactUserDogService contactUserDogService) {
        this.contactUserDogService = contactUserDogService;
    }
    @Operation(summary = "Получение объекта \"Контакты пользователя, интересующегося собакой\" по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Объект \"Контакты пользователя, интересующегося собакой\", найденный по id",
                            content = @Content(mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ContactUserDog.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400",
                            description = "Объект \"Контакты пользователя, интересующегося собакой\" по данному id не найден!")
            }
    )
    @GetMapping("/{id}")
    public ContactUserDog getById(@Parameter(description = "id объекта \"Контакты пользователя, интересующегося собакой\"", example = "956120008L")
                                  @PathVariable Long id) {
        return contactUserDogService.getById(id);
    }

    @Operation(summary = "Создание объекта \"Контакты пользователя, интересующегося собакой\"",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Созданный объект \"Контакты пользователя, интересующегося собакой\"",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_JSON_VALUE,
                            schema = @Schema(implementation = ContactUserDog.class)
                    )
            )
    )
    @PostMapping()
    public ContactUserDog create(@RequestBody ContactUserDog contactUserDog) {
        return this.contactUserDogService.create(contactUserDog);
    }
}

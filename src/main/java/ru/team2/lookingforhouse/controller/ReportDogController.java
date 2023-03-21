package ru.team2.lookingforhouse.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.team2.lookingforhouse.model.ReportDog;
import ru.team2.lookingforhouse.service.ReportDogService;
import ru.team2.lookingforhouse.service.TelegramBot;
/**
 * Класс контроллера объекта "Отчет данных пользователя, интересующегося собакой"
 *
 * @author Одокиенко Екатерина
 */

@Tag(name = "REPORT DOG", description = "API для объекта \"Отчет данных пользователя, интересующегося собакой\"")
@RestController
@RequestMapping("report_dog")
public class ReportDogController {
    private final TelegramBot telegramBot;
    private final ReportDogService reportDogService;

    public ReportDogController(TelegramBot telegramBot, ReportDogService reportDogService) {
        this.telegramBot = telegramBot;
        this.reportDogService = reportDogService;
    }
    @Operation(summary = "Просмотр объекта \"Отчет о данных пользователя, интересующегося собакой\" по айди",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Объект \"Отчет о данных пользователя, интересующегося собакой\", найденный по айди",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ReportDog.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400",
                            description = "Объект \"Отчет о данных пользователя, интересующегося собакой\" по данному id не найден!")
            }
    )
    @GetMapping("/{id}")
    public ReportDog downloadReport(@Parameter(description = "айди объекта \"Отчет о данных пользователя, интересующегося собакой\"", example = "956120008L")
                                    @PathVariable Long id) {
        return this.reportDogService.findById(id);
    }

    @Operation(summary = "Просмотр фото по айди объекта \"Отчет о данных пользователя, интересующегося собакой\"",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Фото, найденное по чат-айди объекта \"Отчет о данных пользователя, интересующегося собакой\""
            )
    )
    @GetMapping("/{id}/photo_from_db")
    public ResponseEntity<String> downloadPhotoFromDB(@Parameter(description = "айди объекта \"Отчет о данных пользователя, интересующегося собакой\"", example = "956120008L")
                                                      @PathVariable Long id) {
        ReportDog report = this.reportDogService.findById(id);
        HttpHeaders headers = new HttpHeaders();
        final String fileType = "image/jpeg";
        headers.setContentType(MediaType.parseMediaType(fileType));
        headers.setContentLength(report.getPhotoId().length());
        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(report.getPhotoId());
    }

    @Operation(summary = "Отправить сообщение пользователю",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Сообщение определенному пользователю",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ReportDog.class)
                            )
                    )
            }
    )
    @GetMapping("/message_to_person")
    public void sendMessageToPerson(@Parameter(description = "айди пользователя", example = "956120008L")
                                    @RequestParam Long id,
                                    @Parameter(description = "Ваше сообщение", example = "Приветствую вас, пурпурный человек!")
                                    @RequestParam String message) {
        this.telegramBot.sendMessage(id, message);
    }
}

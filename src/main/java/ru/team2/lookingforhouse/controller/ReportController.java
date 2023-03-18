package ru.team2.lookingforhouse.controller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import ru.team2.lookingforhouse.model.Report;
import ru.team2.lookingforhouse.service.ReportService;
import ru.team2.lookingforhouse.service.TelegramBot;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;


/**
 * Класс контроллера объекта "Отчет данных"
 *
 * @author Одокиенко Екатерина
 */

@Tag(name = "REPORT", description = "API для объекта \"Отчет данных\"")
@RestController
@RequestMapping("reports")
public class ReportController {
    private final TelegramBot telegramBot;
    private final ReportService reportService;

    public ReportController(TelegramBot telegramBot, ReportService reportService) {
        this.telegramBot = telegramBot;
        this.reportService = reportService;
    }

    @Operation(summary = "Просмотр объекта \"Отчет о данных\" по чат-айди",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Объект \"Отчет о данных\", найденный по чат-айди",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Report.class)
                            )
                    )
            }
    )
    @GetMapping("/{chatId}/report")
    public Report downloadReport(@Parameter(description = "чат-айди объекта \"Отчет о данных\"", example = "956120008L") @PathVariable Long chatId) {
        return this.reportService.findByChatId(chatId);
    }

    @Operation(summary = "Просмотр фото по чат-айди объекта \"Отчет о данных\"",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Фото, найденное по чат-айди объекта \"Отчет о данных\""
            )
    )
    @GetMapping("/{chatId}/photo_from_db")
    public ResponseEntity<String> downloadPhotoFromDB(@Parameter(description = "чат-айди объекта \"Отчет о данных\"", example = "956120008L") @PathVariable Long chatId) {
        Report report = this.reportService.findByChatId(chatId);
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
                                    schema = @Schema(implementation = Report.class)
                            )
                    )
            }
    )
    @GetMapping("/message_to_person")
    public void sendMessageToPerson(@Parameter(description = "чат-айди пользователя", example = "956120008L")
                                    @RequestParam Long chatId,
                                    @Parameter(description = "Ваше сообщение",  example = "Приветствую вас, пурпурный человек!")
                                    @RequestParam String message) {
        this.telegramBot.sendMessage(chatId, message);
    }
}

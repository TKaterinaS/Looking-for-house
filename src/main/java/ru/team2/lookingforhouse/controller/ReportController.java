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
    private final String fileType = "image/jpeg";

    public ReportController(TelegramBot telegramBot, ReportService reportService) {
        this.telegramBot = telegramBot;
        this.reportService = reportService;
    }
    @Operation(summary = "Просмотр объекта \"Отчет о данных\" по id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Объект \"Отчет о данных\", найденный по id",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = Report.class)
                            )
                    )
            }
    )
    @GetMapping("/{id}/report")
    public Report downloadReport(@Parameter (description = "id объекта \"Отчет о данных\"") @PathVariable Long id) {
        return this.reportService.findById(id);
    }

/*    @Operation(summary = "Просмотр фото по id объекта \"Отчет о данных\"",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody (
                    description = "Фото, найденное по id объекта \"Отчет о данных\""
            )
    )
    @GetMapping("/{id}/photo_from_db")
    public ResponseEntity<byte[]> downloadPhotoFromDB(@Parameter (description = "id объекта \"Отчет о данных\"") @PathVariable Long id) {
        Report report = this.reportService.findById(id);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(fileType));
        headers.setContentLength(report.getPhotoId().length);

        return ResponseEntity.status(HttpStatus.OK).headers(headers).body(report.getPhotoId());
    }*/

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
    public void sendMessageToPerson(@Parameter(description = "чат id пользователя", example = "956120008L")
                                    @RequestParam Long chat_Id,
                                    @Parameter(description = "Ваше сообщение")
                                    @RequestParam String message) {
        this.telegramBot.sendMessage(chat_Id, message);
    }
}

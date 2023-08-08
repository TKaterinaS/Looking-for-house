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
import ru.team2.lookingforhouse.model.ReportCat;
import ru.team2.lookingforhouse.service.ReportCatService;
import ru.team2.lookingforhouse.service.TelegramBot;

/**
 * Класс контроллера объекта "Отчет данных пользователя, интересующегося котом"
 *
 * @author Одокиенко Екатерина
 */

@Tag(name = "REPORT CAT", description = "API для объекта \"Отчет данных пользователя, интересующегося котом\"")
@RestController
@RequestMapping("report_cat")
public class ReportCatController {
    private final TelegramBot telegramBot;
    private final ReportCatService reportCatService;


    public ReportCatController(TelegramBot telegramBot, ReportCatService reportCatService) {
        this.telegramBot = telegramBot;
        this.reportCatService = reportCatService;
    }

    @Operation(summary = "Просмотр объекта \"Отчет данных пользователя, интересующегося котом\" по айди",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Объект \"Отчет данных пользователя, интересующегося котом\", найденный по айди",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ReportCat.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400",
                            description = "Объект \"Отчет данных пользователя, интересующегося котом\" по данному id не найден!")
            }
    )
    @GetMapping("/{id}")
    public ReportCat downloadReport(@Parameter(description = "айди объекта \"Отчет данных пользователя, интересующегося котом\"", example = "956120008L")
                                    @PathVariable Long id) {
        return this.reportCatService.findById(id);
    }

    @Operation(summary = "Просмотр объекта \"Отчет данных пользователя, интересующегося котом\" по чат-айди",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Объект \"Отчет данных пользователя, интересующегося котом\", найденный по чат-айди",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ReportCat.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400",
                            description = "Объект \"Отчет данных пользователя, интересующегося котом\" по данному чат-айди не найден!")
            }
    )
    @GetMapping("/{chatId}")
    public ReportCat downloadReportByChatId(@Parameter(description = "чат-айди объекта \"Отчет данных пользователя, интересующегося котом\"", example = "956120008L")
                                            @PathVariable Long chatId) {
        return this.reportCatService.findByUserCat_ChatId(chatId);
    }

    @Operation(summary = "Просмотр списка объектов \"Отчет данных пользователя, интересующегося котом\" по чат-айди",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Список объектов \"Отчет данных пользователя, интересующегося котом\", найденный по чат-айди",
                            content = @Content(
                                    mediaType = MediaType.APPLICATION_JSON_VALUE,
                                    schema = @Schema(implementation = ReportCat.class)
                            )
                    ),
                    @ApiResponse(responseCode = "400",
                            description = "Список объектов \"Отчет данных пользователя, интересующегося котом\" по данному чат-айди не найден!")
            }
    )
    @GetMapping("/{all_reports_by_chatId}")
    public ReportCat downloadAllReportByChatId(@Parameter(description = "чат-айди объекта \"Отчет данных пользователя, интересующегося котом\"", example = "956120008L")
                                               @PathVariable Long chatId) {
        return (ReportCat) this.reportCatService.findAllByUserCat_ChatId(chatId);
    }

    @Operation(summary = "Просмотр фото по айди объекта \"Отчет данных пользователя, интересующегося котом\"",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Фото, найденное по айди объекта \"Отчет данных пользователя, интересующегося котом\""
            )
    )
    @GetMapping("/{id}/photo_from_db")
    public ResponseEntity<String> downloadPhotoFromDB(@Parameter(description = "айди объекта \"Отчет данных пользователя, интересующегося котом\"", example = "956120008L")
                                                      @PathVariable Long id) {
        ReportCat report = this.reportCatService.findById(id);
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
                                    schema = @Schema(implementation = ReportCat.class)
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


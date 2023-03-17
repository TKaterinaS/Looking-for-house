package ru.team2.lookingforhouse.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * Класс исключения ReportNotFoundException.
 * @author Одокиенко Екатерина
 */

/** Механизм для обработки базовых ошибок для REST API с кодом ответа "404 - Не найден". */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ReportNotFoundException extends RuntimeException{
    public ReportNotFoundException() {
        super("Мы не нашли такой отчет данных!");
    }
}

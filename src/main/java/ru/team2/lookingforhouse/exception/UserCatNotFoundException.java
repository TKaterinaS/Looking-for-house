package ru.team2.lookingforhouse.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
/**
 * Класс исключения UserCatNotFoundException.
 * @author Одокиенко Екатерина
 */

/** Spring автоматически возвращает код состояния запроса, если ресурс не найден на сервере */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class UserCatNotFoundException extends RuntimeException{
    public UserCatNotFoundException() {
        super("Мы не нашли такого пользователя!");
    }
}

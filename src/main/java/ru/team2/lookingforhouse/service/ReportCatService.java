package ru.team2.lookingforhouse.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.team2.lookingforhouse.exception.ReportNotFoundException;
import ru.team2.lookingforhouse.model.ReportCat;
import ru.team2.lookingforhouse.model.UserCat;
import ru.team2.lookingforhouse.repository.ReportCatRepository;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
/**
 * Класс сервиса объекта "Отчет данных пользователя, интересующегося котом"
 *
 * @author Одокиенко Екатерина
 */

/**
 * Платформа для регистрации сообщений в Java
 */
@Log4j2
/** Сервис для реализации бизнес-логики */
@Service
public class ReportCatService {
    /**
     * Поле создания слоя репозитория
     */
    private final ReportCatRepository reportCatRepository;

    public ReportCatService(ReportCatRepository reportCatRepository) {
        this.reportCatRepository = reportCatRepository;
    }

    /**
     * Конструктор - создание нового объекта.
     *
     * @see ReportCatService#ReportCatService(ReportCatRepository, ReportCat)
     */
    /**
     * Метод загрузки объекта "Отчет данных пользователя, интересующегося котом"
     *
     * @param id
     * @param infoMessage
     * @param photoId
     * @throws IOException
     * @see ReportCatService
     */
    public void uploadReport(long id, String infoMessage, String photoId, UserCat userCat) throws IOException {
        log.info("Был вызван метод загрузки объекта \"Отчет данных пользователя, интересующегося котом\"");
        ReportCat report = new ReportCat();
        report.setId(id);
        report.setInfoMessage(infoMessage);
        report.setPhotoId(photoId);
        report.setUserCat(userCat);
        this.reportCatRepository.save(report);
    }

    /**
     * Метод получения объекта "Отчет данных пользователя, интересующегося котом" по айди.
     *
     * @param id
     * @return {@link ReportCatRepository#findById(Object)}
     * @throws ReportNotFoundException, если объект "Отчет данных пользователя, интересующегося котом" с указанным id не был найден в БД
     * @see ReportCatService
     */
    public ReportCat findById(Long id) {
        log.info("Был вызван метод получения объекта \"Отчет данных пользователя, интересующегося котом\" по id={}", id);
        return this.reportCatRepository.findById(id).orElseThrow(ReportNotFoundException::new);
    }

    /**
     * Метод получения объекта "Отчет данных пользователя, интересующегося котом" по чат-айди.
     *
     * @param chatId
     * @return {@link ReportCatRepository#findByUserCat_ChatId(Long)}
     * @see ReportCatService
     */
    public ReportCat findByUserCat_ChatId(Long chatId) {
        log.info("Был вызван метод получения объекта \"Отчет данных пользователя, интересующегося котом\" по chatId={}", chatId);
        return this.reportCatRepository.findByUserCat_ChatId(chatId);
    }

    /**
     * Метод сохранения объекта "Отчет данных пользователя, интересующегося котом".
     *
     * @param reportCat
     * @see ReportCatService
     */
    public ReportCat save(ReportCat reportCat) {
        log.info("Был вызван метод сохранения объекта \"Отчет данных пользователя, интересующегося котом\"");
        return this.reportCatRepository.save(reportCat);
    }

    /**
     * Метод удаления объекта "Отчет данных пользователя, интересующегося котом" по айди.
     *
     * @param id
     * @return {@link ReportCatRepository#deleteById(Object)}
     * @see ReportCatService
     */
    public void deleteById(Long id) {
        log.info("Был вызван метод удаления объекта \"Отчет данных пользователя, интересующегося котом\" по id={}", id);
        this.reportCatRepository.deleteById(id);
    }

    /**
     * Метод получения списка всех объектов "Отчет данных пользователя, интересующегося котом" по чат-айди.
     *
     * @return {@link ReportCatRepository#findAllByUserCat_ChatId(Long)} ()}
     * @see ReportCatService
     */
    public Collection <ReportCat> findAllByUserCat_ChatId(Long chatId) {
        log.info("Был вызван метод  получения списка всех объектов \"Отчет данных пользователя, интересующегося котом\" по chatId={}", chatId);
        return this.reportCatRepository.findAllByUserCat_ChatId(chatId);
    }

}

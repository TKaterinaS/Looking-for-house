package ru.team2.lookingforhouse.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.team2.lookingforhouse.exception.ReportNotFoundException;
import ru.team2.lookingforhouse.model.ReportDog;
import ru.team2.lookingforhouse.model.UserDog;
import ru.team2.lookingforhouse.repository.ReportDogRepository;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
/**
 * Класс сервиса объекта "Отчет данных пользователя, интересующегося собакой"
 *
 * @author Одокиенко Екатерина
 */

/**
 * Платформа для регистрации сообщений в Java
 */
@Log4j2
/** Сервис для реализации бизнес-логики */
@Service
public class ReportDogService {
    /**
     * Поле создания слоя репозитория
     */
    private final ReportDogRepository reportDogRepository;

    /**
     * Конструктор - создание нового объекта.
     *
     * @see ReportDogService#ReportDogService(ReportDogRepository reportDogRepository)
     */
    public ReportDogService(ReportDogRepository reportDogRepository) {
        this.reportDogRepository = reportDogRepository;
    }

    /**
     * Метод загрузки объекта "Отчет данных пользователя, интересующегося собакой"
     *
     * @param id
     * @param infoMessage
     * @param photoId
     * @param userDog
     * @throws IOException
     * @see ReportDogService
     */
    public void uploadReport(long id, String infoMessage, String photoId, UserDog userDog) throws IOException {
        log.info("Был вызван метод загрузки объекта \"Отчет данных пользователя, интересующегося собакой\"");
        ReportDog report = new ReportDog();
        report.setId(id);
        report.setInfoMessage(infoMessage);
        report.setPhotoId(photoId);
        report.setUserDog(userDog);
        this.reportDogRepository.save(report);
    }

    /**
     * Метод получения объекта "Отчет данных пользователя, интересующегося собакой" по айди.
     *
     * @param id
     * @return {@link ReportDogRepository#findById(Object)}
     * @throws ReportNotFoundException, если объект "Отчет данных пользователя, интересующегося собакой" с указанным id не был найден в БД
     * @see ReportDogService
     */
    public ReportDog findById(Long id) {
        log.info("Был вызван метод получения объекта \"Отчет данных пользователя, интересующегося собакой\" по id={}", id);
        return this.reportDogRepository.findById(id).orElseThrow(ReportNotFoundException::new);
    }

    /**
     * Метод получения объекта "Отчет данных пользователя, интересующегося собакой" по чат-айди.
     *
     * @param chatId
     * @return {@link ReportDogRepository#findByUserDog_ChatId(Long)}
     * @see ReportDogService
     */
    public ReportDog findByUserDog_ChatId(Long chatId) {
        log.info("Был вызван метод получения объекта \"Отчет данных пользователя, интересующегося собакой\" по chatId={}", chatId);
        return (ReportDog) this.reportDogRepository.findByUserDog_ChatId(chatId);
    }

    /**
     * Метод сохранения объекта "Отчет данных пользователя, интересующегося собакой".
     *
     * @param reportDog
     * @see ReportDogService
     */
    public ReportDog save(ReportDog reportDog) {
        log.info("Был вызван метод сохранения объекта \"Отчет данных пользователя, интересующегося собакой\"");
        return this.reportDogRepository.save(reportDog);
    }

    /**
     * Метод удаления объекта "Отчет данных пользователя, интересующегося собакой" по айди.
     *
     * @param id
     * @return {@link ReportDogRepository#deleteById(Object)}
     * @see ReportCatService
     */
    public void deleteById(Long id) {
        log.info("Был вызван метод удаления объекта \"Отчет данных пользователя, интересующегося собакой\" по id={}", id);
        this.reportDogRepository.deleteById(id);
    }

    /**
     * Метод получения списка всех объектов "Отчет данных пользователя, интересующегося собакой" по чат-айди.
     *
     * @return {@link ReportDogRepository#findAllByUserDog_ChatId(Long)} ()}
     * @see ReportDogService
     */
    public Collection<ReportDog> findAllByUserDog_ChatId(Long chatId) {
        log.info("Был вызван метод  получения списка всех объектов \"Отчет данных пользователя, интересующегося собакой\" по chatId={}", chatId);
        return this.reportDogRepository.findAllByUserDog_ChatId(chatId);
    }

//Возможно и не понадобится этот метод...

    /**
     * Метод получения списка всех объектов "Отчет данных пользователя, интересующегося собакой" с параметрами: номер страницы и количество страниц.
     *
     * @param pageNumber
     * @param pageSize
     * @return {@link ReportDogRepository#findAll()}
     * @see ReportDogService
     */
    public List<ReportDog> getAllReports(Integer pageNumber, Integer pageSize) {
        log.info("Был вызван метод получения списка всех объектов \"Отчет данных пользователя, интересующегося собакой\" с параметрами");
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        return this.reportDogRepository.findAll(pageRequest).getContent();
    }

//Возможно и не понадобится этот метод...

    /**
     * Метод получения расширения файла.
     *
     * @param fileName
     * @see ReportDogService
     */
    private String getExtensions(String fileName) {
        log.info("Был вызван метод получения расширения файла");
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}

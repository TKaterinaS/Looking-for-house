package ru.team2.lookingforhouse.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.team2.lookingforhouse.exception.ReportNotFoundException;
import ru.team2.lookingforhouse.model.ReportCat;
import ru.team2.lookingforhouse.model.UserCat;
import ru.team2.lookingforhouse.repository.ReportCatRepository;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
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
     * Метод получение всех объектов "Отчет данных пользователя, интересующегося котом".
     *
     * @return {@link ReportCatRepository#findAll()}
     * @see ReportCatService
     */
    public List<ReportCat> getAll() {
        log.info("Был вызван метод получения списка всех объектов \"Отчет данных пользователя, интересующегося котом\"");
        return this.reportCatRepository.findAll();
    }

//Возможно и не понадобится этот метод...

    /**
     * Метод получения списка всех объектов "Отчет данных пользователя, интересующегося котом" с параметрами: номер страницы и количество страниц.
     *
     * @param pageNumber
     * @param pageSize
     * @return {@link ReportCatRepository#findAll()}
     * @see ReportCatService
     */
    public List<ReportCat> getAllReports(Integer pageNumber, Integer pageSize) {
        log.info("Был вызван метод получения списка всех объектов \"Отчет данных пользователя, интересующегося котом\" с параметрами");
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        return this.reportCatRepository.findAll(pageRequest).getContent();
    }

//Возможно и не понадобится этот метод...

    /**
     * Метод получения расширения файла.
     *
     * @param fileName
     * @see ReportCatService
     */
    private String getExtensions(String fileName) {
        log.info("Был вызван метод получения расширения файла");
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}

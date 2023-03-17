package ru.team2.lookingforhouse.service;

import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.team2.lookingforhouse.exception.ReportNotFoundException;
import ru.team2.lookingforhouse.model.Report;
import ru.team2.lookingforhouse.repository.ReportRepository;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
/**
 * Класс сервиса объекта "Отчет данных"
 *
 * @author Одокиенко Екатерина
 */

/**
 * Платформа для регистрации сообщений в Java
 */
@Log4j2
/** Сервис для реализации бизнес-логики */
@Service
public class ReportService {

    /**
     * Поле создания слоя репозитория
     */
    private final ReportRepository reportRepository;

    /**
     * Конструктор - создание нового объекта.
     *
     * @see ReportService#ReportService(ReportRepository reportRepository)
     */

    public ReportService(ReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    /**
     * Метод загрузки объекта "Отчет данных"
     *
     * @param chatId
     * @param infoMessage
     * @param photoId
     * @throws IOException
     * @see ReportService
     */
    public void uploadReport(Long chatId, String infoMessage, byte[] photoId) throws IOException {
        log.info("Был вызван метод загрузки объекта \"Отчет данных\"");
        Report report = new Report();
        report.setInfoMessage(infoMessage);
        report.setChatId(chatId);
        report.setPhotoId(photoId);
        this.reportRepository.save(report);
    }

    /**
     * Метод получения объекта "Отчет данных" по айди.
     *
     * @param id
     * @return {@link ReportRepository#findById(Object)}
     * @throws ReportNotFoundException, если объект "Отчет данных" с указанным id не был найден в БД
     * @see ReportService
     */
    public Report findById(Long id) {
        log.info("Был вызван метод получения объекта \"Отчет данных\" по id={}", id);
        return this.reportRepository.findById(id).orElseThrow(ReportNotFoundException::new);
    }

    /**
     * Метод получения объекта "Отчет данных" по чат-айди.
     * @param chatId
     * @return {@link ReportRepository#findByChatId(Long)}
     * @see ReportService
     */
    public Report findByChatId(Long chatId) {
        log.info("Был вызван метод получения объекта \"Отчет данных\" по chatId={}", chatId);
        return this.reportRepository.findByChatId(chatId);
    }

    /**
     * Метод получения списка у объекта "Отчет данных" по чат-айди.
     * @param chatId
     * @return {@link ReportRepository#findListByChatId(Long)}
     * @see ReportService
     */
    public Collection<Report> findListByChatId(Long chatId) {
        log.info("Был вызван метод получения списка у объекта \"Отчет данных\" id={}", chatId);
        return this.reportRepository.findListByChatId(chatId);
    }

    /**
     * Метод сохранения объекта "Отчет данных".
     * @param report
     * @return {@link ReportRepository#findListByChatId(Long)}
     * @see ReportService
     */
    public Report save(Report report) {
        log.info("Был вызван метод сохранения объекта \"Отчет данных\"");
        return this.reportRepository.save(report);
    }

    /**
     * Метод удаления объекта "Отчет данных" по айди.
     * @param id
     * @return {@link ReportRepository#deleteById(Object)}
     * @see ReportService
     */
    public void deleteById(Long id) {
        log.info("Был вызван метод удаления объекта \"Отчет данных\" id={}", id);
        this.reportRepository.deleteById(id);
    }

    /**
     * Метод получение списка всех объектов "Отчет данных".
     *
     * @return {@link ReportRepository#findAll()}
     * @see ReportService
     */
    public List<Report> getAll() {
        log.info("Был вызван метод получения списка всех объектов \"Отчет данных\"");
        return this.reportRepository.findAll();
    }

    /**
     * Метод получение списка всех объектов "Отчет данных" с параметрами: номер страницы и количество страниц.
     * @param pageNumber
     * @param pageSize
     * @return {@link ReportRepository#findAll()}
     * @see ReportService
     */
    public List<Report> getAllReports(Integer pageNumber, Integer pageSize) {
        log.info("Был вызван метод получения списка всех объектов \"Отчет данных\" с параметрами");
        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
        return this.reportRepository.findAll(pageRequest).getContent();
    }

    /**
     * Метод получения расширения файла.
     * @param fileName
     * @see ReportService
     */
    private String getExtensions(String fileName) {
        log.info("Былвызван метод получения расширения файла");
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }
}

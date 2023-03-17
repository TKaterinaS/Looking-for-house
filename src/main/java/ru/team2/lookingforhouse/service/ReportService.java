//package ru.team2.lookingforhouse.service;
//
//import lombok.extern.log4j.Log4j2;
//import org.springframework.stereotype.Service;
//import ru.team2.lookingforhouse.model.ReportData;
//import ru.team2.lookingforhouse.repository.ReportDataRepository;
//
//import java.io.IOException;
//import java.util.Collection;
///**
// * Класс сервиса объекта "Отчет данных"
// *
// * @author Одокиенко Екатерина
// */
///**
// * Платформа для регистрации сообщений в Java
// */
//@Log4j2
///** Сервис для реализации бизнес-логики */
//@Service
//public class ReportDataService {
//
//    /**
//     * Поле создания слоя репозитория
//     */
//    private final ReportDataRepository reportDataRepository;
//
//    /**
//     * Конструктор - создание нового объекта.
//     *
//     * @see ReportDataService#ReportDataService(ReportDataRepository reportDataRepository)
//     */
//
//    public ReportDataService(ReportDataRepository reportDataRepository) {
//        this.reportDataRepository = reportDataRepository;
//    }
//
//    /**
//     * Method to uploadReportData.
//     * @param chatId
//     * @param infoMessage
//     * @param photoId
//     * @throws IOException
//     * @see ReportDataService
//     */
//    public void uploadReportData(Long chatId, String infoMessage, byte[] photoId) throws IOException {
//        log.info("Was invoked method to uploadReportData");
//
//        ReportData report = new ReportData();
//
//        report.setInfoMessage();
//        report.setDays(daysOfReports);
//        report.setFilePath(filePath);
//        report.setFileSize(file.fileSize());
//        report.setLastMessageMs(timeDate);
//        report.setChatId(personId);
//        report.setData(pictureFile);
//        report.setRation(ration);
//        report.setHealth(health);
//        report.setHabits(habits);
//        this.reportRepository.save(report);
//    }
//
//    /**
//     * Method to uploadReportData.
//     * @param personId
//     * @param pictureFile
//     * @param file
//     * @param caption
//     * @param filePath
//     * @param dateSendMessage
//     * @param timeDate
//     * @param daysOfReports
//     * @throws IOException
//     * @see ReportDataService
//     */
//    public void uploadReportData(Long personId, byte[] pictureFile, File file,
//                                 String caption, String filePath, Date dateSendMessage, Long timeDate, long daysOfReports) throws IOException {
//        logger.info("Was invoked method to uploadReportData");
//
//        ReportData report = new ReportData();//findById(personId);
//        report.setLastMessage(dateSendMessage);
//        report.setDays(daysOfReports);
//        report.setFilePath(filePath);
//        report.setChatId(personId);
//        report.setFileSize(file.fileSize());
//        report.setData(pictureFile);
//        report.setCaption(caption);
//        report.setLastMessageMs(timeDate);
//        this.reportRepository.save(report);
//    }
//
//    /**
//     * Method to get a report by id.
//     * @param id
//     * @return {@link ReportDataRepository#findById(Object)}
//     * @see ReportDataService
//     * @exception PersonCatNotFoundException
//     */
//    public ReportData findById(Long id) {
//        logger.info("Was invoked method to get a report by id={}", id);
//
//        return this.reportRepository.findById(id)
//                .orElseThrow(ReportDataNotFoundException::new);
//    }
//
//    /**
//     * Method to get a report by chatId.
//     * @param chatId
//     * @return {@link ReportDataRepository#findByChatId(Long)}
//     * @see ReportDataService
//     */
//    public ReportData findByChatId(Long chatId) {
//        logger.info("Was invoked method to get a report by chatId={}", chatId);
//
//        return this.reportRepository.findByChatId(chatId);
//    }
//
//    /**
//     * Method to findListByChatId a report by id.
//     * @param chatId
//     * @return {@link ReportDataRepository#findListByChatId(Long)}
//     * @see ReportDataService
//     */
//    public Collection<ReportData> findListByChatId(Long chatId) {
//        logger.info("Was invoked method to findListByChatId a report by id={}", chatId);
//
//        return this.reportRepository.findListByChatId(chatId);
//    }
//
//    /**
//     * Method to save a report.
//     * @param report
//     * @return {@link ReportDataRepository#findListByChatId(Long)}
//     * @see ReportDataService
//     */
//    public ReportData save(ReportData report) {
//        logger.info("Was invoked method to save a report");
//
//        return this.reportRepository.save(report);
//    }
//
//    /**
//     * Method to remove a report by id.
//     * @param id
//     * @see ReportDataService
//     */
//    public void remove(Long id) {
//        logger.info("Was invoked method to remove a report by id={}", id);
//
//        this.reportRepository.deleteById(id);
//    }
//
//    /**
//     * Method to get all reports.
//     * @return {@link ReportDataRepository#findAll()}
//     * @see ReportDataService
//     */
//    public List<ReportData> getAll() {
//        logger.info("Was invoked method to get all reports");
//
//        return this.reportRepository.findAll();
//    }
//
//    /**
//     * Method to get all reports.
//     * @param pageNumber
//     * @param pageSize
//     * @return {@link ReportDataRepository#findAll()}
//     * @see ReportDataService
//     */
//    public List<ReportData> getAllReports(Integer pageNumber, Integer pageSize) {
//        logger.info("Was invoked method to get all reports");
//
//        PageRequest pageRequest = PageRequest.of(pageNumber - 1, pageSize);
//        return this.reportRepository.findAll(pageRequest).getContent();
//    }
//
//    /**
//     * Method to getExtensions.
//     * @param fileName
//     * @see ReportDataService
//     */
//    private String getExtensions(String fileName) {
//        logger.info("Was invoked method to getExtensions");
//
//        return fileName.substring(fileName.lastIndexOf(".") + 1);
//    }
//}

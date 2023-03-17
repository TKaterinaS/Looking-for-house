package ru.team2.lookingforhouse.model;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;


    @Entity(name = "Report")
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public class ReportData {
        @Id
        private Long chatId;
        private  String infoMessage;
        @Lob
        private byte[]  photoId;
    }


package ru.team2.lookingforhouse.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.team2.lookingforhouse.model.ReportCat;
import ru.team2.lookingforhouse.model.UserCat;
import ru.team2.lookingforhouse.repository.ReportCatRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReportCatServiceTest {
    @Mock
    private ReportCatRepository reportCatRepository;
    @InjectMocks
    private ReportCatService reportCatService;

    @Test
    public void uploadReportTest() throws IOException {
        reportCatService.uploadReport(1L, "infoMessageTest", "photoIdTest", new UserCat());

        verify(reportCatRepository).save(any(ReportCat.class));
    }

    @Test
    public void findReportByIdTest() {
        ReportCat expected = new ReportCat(1L, "infoMessageTest", "photoIdTest", new UserCat());
        when(reportCatRepository.findById(anyLong())).thenReturn(Optional.of(expected));

        ReportCat actual = reportCatService.findById(1L);

        assertThat(actual).isEqualTo(expected);
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getInfoMessage()).isEqualTo(expected.getInfoMessage());
        assertThat(actual.getPhotoId()).isEqualTo(expected.getPhotoId());
        assertThat(actual.getUserCat()).isEqualTo(expected.getUserCat());
    }

    @Test
    public void findReportByUserCatChatIdTest() {
        ReportCat expected = new ReportCat(1L, "infoMessageTest", "photoIdTest", new UserCat());
        expected.getUserCat().setChatId(123L);
        when(reportCatRepository.findByUserCat_ChatId(anyLong())).thenReturn(expected);

        ReportCat actual = reportCatService.findByUserCat_ChatId(123L);

        assertThat(actual).isEqualTo(expected);
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getInfoMessage()).isEqualTo(expected.getInfoMessage());
        assertThat(actual.getPhotoId()).isEqualTo(expected.getPhotoId());
        assertThat(actual.getUserCat()).isEqualTo(expected.getUserCat());
    }

    @Test
    public void saveReportTest() {
        ReportCat expected = new ReportCat(1L, "infoMessageTest", "photoIdTest", new UserCat());
        when(reportCatRepository.save(any(ReportCat.class))).thenReturn(expected);

        ReportCat actual = reportCatService.save(expected);

        assertThat(actual).isEqualTo(expected);
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getInfoMessage()).isEqualTo(expected.getInfoMessage());
        assertThat(actual.getPhotoId()).isEqualTo(expected.getPhotoId());
        assertThat(actual.getUserCat()).isEqualTo(expected.getUserCat());
    }

    @Test
    public void deleteReportByIdTest() {
        reportCatService.deleteById(anyLong());
        verify(reportCatRepository).deleteById(anyLong());
    }

    @Test
    public void findAllReportsByUserCatChatIdTest() {
        ReportCat reportCat1 = new ReportCat(1L, "infoMessageTest", "photoIdTest", new UserCat());
        ReportCat reportCat2 = new ReportCat(2L, "infoMessageTest2", "photoIdTest2", new UserCat());
        ReportCat reportCat3 = new ReportCat(3L, "infoMessageTest3", "photoIdTest3", new UserCat());
        Collection<ReportCat> expected = List.of(reportCat1,reportCat2,reportCat3);
        when(reportCatRepository.findAllByUserCat_ChatId(anyLong())).thenReturn(expected);

        Collection<ReportCat> actual = reportCatService.findAllByUserCat_ChatId(anyLong());
        verify(reportCatRepository).findAllByUserCat_ChatId(anyLong());
        assertThat(actual).isEqualTo(expected);
    }
}

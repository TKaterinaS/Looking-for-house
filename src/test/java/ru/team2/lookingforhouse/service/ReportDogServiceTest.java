package ru.team2.lookingforhouse.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.team2.lookingforhouse.model.ReportCat;
import ru.team2.lookingforhouse.model.ReportDog;
import ru.team2.lookingforhouse.model.UserCat;
import ru.team2.lookingforhouse.model.UserDog;
import ru.team2.lookingforhouse.repository.ReportDogRepository;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReportDogServiceTest {
    @Mock
    private ReportDogRepository reportDogRepository;
    @InjectMocks
    private ReportDogService reportDogService;

    @Test
    public void uploadReportTest() throws IOException {
        reportDogService.uploadReport(1L, "infoMessageTest", "photoIdTest", new UserDog());

        verify(reportDogRepository).save(any(ReportDog.class));
    }

    @Test
    public void findReportByIdTest() {
        ReportDog expected = new ReportDog(1L, "infoMessageTest", "photoIdTest", new UserDog());
        when(reportDogRepository.findById(anyLong())).thenReturn(Optional.of(expected));

        ReportDog actual = reportDogService.findById(1L);

        assertThat(actual).isEqualTo(expected);
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getInfoMessage()).isEqualTo(expected.getInfoMessage());
        assertThat(actual.getPhotoId()).isEqualTo(expected.getPhotoId());
        assertThat(actual.getUserDog()).isEqualTo(expected.getUserDog());
    }

    @Test
    public void findReportByUserDogChatIdTest() {
        ReportDog expected = new ReportDog(1L, "infoMessageTest", "photoIdTest", new UserDog());
        when(reportDogRepository.findByUserDog_ChatId(anyLong())).thenReturn(expected);

        ReportDog actual = reportDogService.findByUserDog_ChatId(anyLong());
        assertThat(actual).isEqualTo(expected);
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getInfoMessage()).isEqualTo(expected.getInfoMessage());
        assertThat(actual.getPhotoId()).isEqualTo(expected.getPhotoId());
        assertThat(actual.getUserDog()).isEqualTo(expected.getUserDog());
    }

    @Test
    public void saveReportTest() {
        ReportDog expected = new ReportDog(1L, "infoMessageTest", "photoIdTest", new UserDog());
        when(reportDogRepository.save(any(ReportDog.class))).thenReturn(expected);

        ReportDog actual = reportDogService.save(expected);
        assertThat(actual).isEqualTo(expected);
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getInfoMessage()).isEqualTo(expected.getInfoMessage());
        assertThat(actual.getPhotoId()).isEqualTo(expected.getPhotoId());
        assertThat(actual.getUserDog()).isEqualTo(expected.getUserDog());
    }

    @Test
    public void deleteReportByIdTest() {
        reportDogService.deleteById(anyLong());
        verify(reportDogRepository).deleteById(anyLong());
    }

    @Test
    public void findAllReportsByUserDogChatIdTest() {
        ReportDog reportDog1 = new ReportDog(1L, "infoMessageTest", "photoIdTest", new UserDog());
        ReportDog reportDog2 = new ReportDog(2L, "infoMessageTest2", "photoIdTest2", new UserDog());
        ReportDog reportDog3 = new ReportDog(3L, "infoMessageTest3", "photoIdTest3", new UserDog());
        Collection<ReportDog> expected = List.of(reportDog1,reportDog2,reportDog3);
        when(reportDogRepository.findAllByUserDog_ChatId(anyLong())).thenReturn(expected);

        Collection<ReportDog> actual = reportDogService.findAllByUserDog_ChatId(anyLong());
        verify(reportDogRepository).findAllByUserDog_ChatId(anyLong());
        assertThat(actual).isEqualTo(expected);
    }
}

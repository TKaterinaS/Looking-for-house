package ru.team2.lookingforhouse.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.team2.lookingforhouse.exception.UserCatNotFoundException;
import ru.team2.lookingforhouse.exception.UserDogNotFoundException;
import ru.team2.lookingforhouse.model.UserCat;
import ru.team2.lookingforhouse.repository.UserCatRepository;
import ru.team2.lookingforhouse.util.UserStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)

public class UserCatServiceTest {
    @Mock
    private UserCatRepository userCatRepository;
    @InjectMocks
    private UserCatService userCatService;

    @Test
    public void findUserCatByChatIdTest() {
        UserCat expected = new UserCat(123L,
                "testFirstName",
                "testLastName",
                "testUserName",
                "+79123456789",
                LocalDateTime.now(),
                UserStatus.JUST_USER,
                null);
        when(userCatRepository.findUserCatByChatId(anyLong())).thenReturn(expected);

        UserCat actual = userCatService.findByChatId(123L);

        assertThat(actual.getChatId()).isEqualTo(expected.getChatId());
        assertThat(actual.getFirstName()).isEqualTo(expected.getFirstName());
        assertThat(actual.getLastName()).isEqualTo(expected.getLastName());
        assertThat(actual.getUserName()).isEqualTo(expected.getUserName());
        assertThat(actual.getPhoneNumber()).isEqualTo(expected.getPhoneNumber());
        assertThat(actual.getRegisteredAt()).isEqualTo(expected.getRegisteredAt());
        assertThat(actual.getUserStatus()).isEqualTo(expected.getUserStatus());
        assertThat(actual.getReports()).isEqualTo(expected.getReports());
    }

    @Test
    public void createUserCatTest() {
        UserCat expected = new UserCat(123L,
                "testFirstName",
                "testLastName",
                "testUserName",
                "+79123456789",
                LocalDateTime.now(),
                UserStatus.JUST_USER,
                null);
        when(userCatRepository.save(any(UserCat.class))).thenReturn(expected);

        UserCat actual = userCatService.create(expected);

        assertThat(actual.getChatId()).isEqualTo(expected.getChatId());
        assertThat(actual.getFirstName()).isEqualTo(expected.getFirstName());
        assertThat(actual.getLastName()).isEqualTo(expected.getLastName());
        assertThat(actual.getUserName()).isEqualTo(expected.getUserName());
        assertThat(actual.getPhoneNumber()).isEqualTo(expected.getPhoneNumber());
        assertThat(actual.getRegisteredAt()).isEqualTo(expected.getRegisteredAt());
        assertThat(actual.getUserStatus()).isEqualTo(expected.getUserStatus());
        assertThat(actual.getReports()).isEqualTo(expected.getReports());
    }

    @Test
    public void updateUserCatTest() {
        UserCat expected = new UserCat(123L,
                "testFirstName",
                "testLastName",
                "testUserName",
                "+79123456789",
                LocalDateTime.now(),
                UserStatus.JUST_USER,
                null);
        when(userCatRepository.findUserCatByChatId(anyLong())).thenReturn(expected);
        when(userCatRepository.save(any(UserCat.class))).thenReturn(expected);

        UserCat actual = userCatService.update(expected);

        assertThat(actual.getChatId()).isEqualTo(expected.getChatId());
        assertThat(actual.getFirstName()).isEqualTo(expected.getFirstName());
        assertThat(actual.getLastName()).isEqualTo(expected.getLastName());
        assertThat(actual.getUserName()).isEqualTo(expected.getUserName());
        assertThat(actual.getPhoneNumber()).isEqualTo(expected.getPhoneNumber());
        assertThat(actual.getRegisteredAt()).isEqualTo(expected.getRegisteredAt());
        assertThat(actual.getUserStatus()).isEqualTo(expected.getUserStatus());
        assertThat(actual.getReports()).isEqualTo(expected.getReports());
    }

    @Test
    public void getAllByChatIdTest() {
        List<UserCat> expected = new ArrayList<>();
        UserCat userCat1 = new UserCat(123L,
                "testFirstName",
                "testLastName",
                "testUserName",
                "+79123456789",
                LocalDateTime.now(),
                UserStatus.JUST_USER,
                null);

        UserCat userCat2 = new UserCat(1234L,
                "testFirstName2",
                "testLastName2",
                "testUserName2",
                "+79223456789",
                LocalDateTime.now(),
                UserStatus.JUST_USER,
                null);

        UserCat userCat3 = new UserCat(12345L,
                "testFirstName3",
                "testLastName",
                "testUserName3",
                "+79323456789",
                LocalDateTime.now(),
                UserStatus.JUST_USER,
                null);

        expected.add(userCat1);
        expected.add(userCat2);
        expected.add(userCat3);

        when(userCatRepository.findAll()).thenReturn(expected);

        Collection<UserCat> actual = userCatService.getAll();

        assertThat(actual.size()).isEqualTo(expected.size());
        assertThat(actual).isEqualTo(expected);

    }

    @Test
    public void deleteByIdTest() {
        userCatService.deleteById(anyLong());
        verify(userCatRepository).deleteById(anyLong());
    }

    @Test
    public void updateWithExceptionTest() {
        UserCat expected = new UserCat();
        expected.setChatId(null);
        assertThatThrownBy(() -> userCatService.update(expected))
                .isInstanceOf(UserCatNotFoundException.class)
                .hasMessage("Мы не нашли такого пользователя!");
        verify(userCatRepository, never()).save(expected);
    }
}

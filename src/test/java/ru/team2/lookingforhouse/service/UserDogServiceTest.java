package ru.team2.lookingforhouse.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.team2.lookingforhouse.exception.UserDogNotFoundException;
import ru.team2.lookingforhouse.model.UserDog;
import ru.team2.lookingforhouse.repository.UserDogRepository;
import ru.team2.lookingforhouse.util.UserStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserDogServiceTest {
    @Mock
    private UserDogRepository userDogRepository;
    @InjectMocks
    private UserDogService userDogService;

    @Test
    public void findUserDogByChatIdTest() {
        UserDog expected = new UserDog(123L,
                "testFirstName",
                "testLastName",
                "testUserName",
                "+79123456789",
                LocalDateTime.now(),
                UserStatus.JUST_USER,
                null);
        when(userDogRepository.findUserDogByChatId(anyLong())).thenReturn(expected);

        UserDog actual = userDogService.findByChatId(123L);

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
        UserDog expected = new UserDog(123L,
                "testFirstName",
                "testLastName",
                "testUserName",
                "+79123456789",
                LocalDateTime.now(),
                UserStatus.JUST_USER,
                null);
        when(userDogRepository.save(any(UserDog.class))).thenReturn(expected);

        UserDog actual = userDogService.create(expected);

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
        UserDog expected = new UserDog(123L,
                "testFirstName",
                "testLastName",
                "testUserName",
                "+79123456789",
                LocalDateTime.now(),
                UserStatus.JUST_USER,
                null);
        when(userDogRepository.findUserDogByChatId(anyLong())).thenReturn(expected);
        when(userDogRepository.save(any(UserDog.class))).thenReturn(expected);

        UserDog actual = userDogService.update(expected);

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
    public void findAllByChatIdTest() {
        List<UserDog> expected = new ArrayList<>();
        UserDog userDog1 = new UserDog(123L,
                "testFirstName",
                "testLastName",
                "testUserName",
                "+79123456789",
                LocalDateTime.now(),
                UserStatus.JUST_USER,
                null);

        UserDog userDog2 = new UserDog(1234L,
                "testFirstName2",
                "testLastName2",
                "testUserName2",
                "+79223456789",
                LocalDateTime.now(),
                UserStatus.JUST_USER,
                null);

        UserDog userDog3 = new UserDog(12345L,
                "testFirstName3",
                "testLastName",
                "testUserName3",
                "+79323456789",
                LocalDateTime.now(),
                UserStatus.JUST_USER,
                null);

        expected.add(userDog1);
        expected.add(userDog2);
        expected.add(userDog3);

        when(userDogRepository.findAll()).thenReturn(expected);

        Collection<UserDog> actual = userDogService.getAll();

        assertThat(actual.size()).isEqualTo(expected.size());
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    public void deleteByIdTest() {
        userDogService.deleteById(anyLong());
        verify(userDogRepository).deleteById(anyLong());
    }

    @Test
    public void updateWithExceptionTest() {
        UserDog expected = new UserDog();
        expected.setChatId(null);
        assertThatThrownBy(() -> userDogService.update(expected))
                .isInstanceOf(UserDogNotFoundException.class)
                .hasMessage("Мы не нашли такого пользователя!");
        verify(userDogRepository, never()).save(expected);
    }
}

package ru.team2.lookingforhouse.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.internal.verification.Times;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Chat;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.team2.lookingforhouse.config.BotConfig;
import com.vdurmont.emoji.EmojiParser;
import ru.team2.lookingforhouse.model.UserCat;
import ru.team2.lookingforhouse.model.UserDog;
import ru.team2.lookingforhouse.repository.UserCatRepository;
import ru.team2.lookingforhouse.repository.UserDogRepository;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TelegramBotTest {
    @Mock
    private BotConfig botConfig;
    @Mock
    private UserCatRepository userCatRepository;
    @Mock
    private UserDogRepository userDogRepository;
    @Spy
    @InjectMocks
    private TelegramBot telegramBot;

    @Test
    public void handlesStartTest() throws TelegramApiException {
        Update update = new Update();
        update.setMessage(new Message());
        update.getMessage().setChat(new Chat());
        update.getMessage().getChat().setFirstName("Name");
        update.getMessage().setText("/start");
        update.getMessage().getChat().setId(123L);

        telegramBot.onUpdateReceived(update);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot, new Times(2)).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        String name = "Name";
        String txtMsg = EmojiParser.parseToUnicode("Выберете приют собак 🐶  или кошек  🐱");
        assertThat(actual.getChatId()).isEqualTo(update.getMessage().getChatId().toString());
        assertThat(actual.getText()).isEqualTo(txtMsg);

    }

    @Test
    public void handlesDogButtonTest() throws TelegramApiException {
        Update update = new Update();
        update.setMessage(new Message());
        update.getMessage().setChat(new Chat());
        update.getMessage().getChat().setFirstName("Name");
        update.getMessage().setText("/dog");
        update.getMessage().getChat().setId(123L);

        telegramBot.onUpdateReceived(update);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(userDogRepository).save(any(UserDog.class));
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        String name = "Name";
        String txtMsg = EmojiParser.parseToUnicode("Что хотите узнать?");
        assertThat(actual.getChatId()).isEqualTo(update.getMessage().getChatId().toString());
        assertThat(actual.getText()).isEqualTo(txtMsg);
    }

    @Test
    public void handlesCatButtonTest() throws TelegramApiException {
        Update update = new Update();
        update.setMessage(new Message());
        update.getMessage().setChat(new Chat());
        update.getMessage().getChat().setFirstName("Name");
        update.getMessage().setText("/cat");
        update.getMessage().getChat().setId(123L);

        telegramBot.onUpdateReceived(update);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(userCatRepository).save(any(UserCat.class));
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        String name = "Name";
        String txtMsg = EmojiParser.parseToUnicode("Что хотите узнать?");
        assertThat(actual.getChatId()).isEqualTo(update.getMessage().getChatId().toString());
        assertThat(actual.getText()).isEqualTo(txtMsg);
    }

    @Test
    public void handlesCallVolunteerTest() throws TelegramApiException {
        Update update = new Update();
        update.setMessage(new Message());
        update.getMessage().setChat(new Chat());
        update.getMessage().getChat().setFirstName("Name");
        update.getMessage().setText("/call_volunteer");
        update.getMessage().getChat().setId(123L);

        when(telegramBot.generateRandomChatId()).thenReturn(123L);
        telegramBot.onUpdateReceived(update);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot, times(2)).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        String txtMsg = EmojiParser.parseToUnicode(
                "Требуется консультация волонтёра для пользователя по имени Name. Чат-айди пользователя - 123");
        assertThat(actual.getChatId()).isEqualTo(update.getMessage().getChatId().toString());
        assertThat(actual.getText()).isEqualTo(txtMsg);

    }

}

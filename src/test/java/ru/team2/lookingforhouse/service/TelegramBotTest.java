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

import static org.assertj.core.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class TelegramBotTest {
    @Mock
    private BotConfig botConfig;
    @Mock
    private UserCatService userCatService;
    @Mock
    private UserDogService userDogService;
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
        Mockito.verify(telegramBot, new Times(2)).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        String name = "Name";
        String txtMsg = EmojiParser.parseToUnicode("Выберете приют собак 🐶  или кошек  🐱");
        assertThat(actual.getChatId()).isEqualTo(update.getMessage().getChatId().toString());
        assertThat(actual.getText()).isEqualTo(txtMsg);

    }
}

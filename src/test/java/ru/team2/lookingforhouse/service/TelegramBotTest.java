package ru.team2.lookingforhouse.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.internal.verification.Times;
import org.mockito.junit.jupiter.MockitoExtension;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.CallbackQuery;
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

import static ru.team2.lookingforhouse.util.Constant.*;

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
        update.getMessage().setText("/start");
        update.getMessage().getChat().setId(123L);

        telegramBot.onUpdateReceived(update);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot, new Times(2)).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        String txtMsg = EmojiParser.parseToUnicode("Выберете приют собак 🐶  или кошек  🐱");
        assertThat(actual.getChatId()).isEqualTo(update.getMessage().getChatId().toString());
        assertThat(actual.getText()).isEqualTo(txtMsg);

    }

    @Test
    public void handlesDogButtonTest() throws TelegramApiException {
        Update update = new Update();
        update.setMessage(new Message());
        update.getMessage().setChat(new Chat());
        update.getMessage().setText("/dog");
        update.getMessage().getChat().setId(123L);

        telegramBot.onUpdateReceived(update);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(userDogRepository).save(any(UserDog.class));
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getChatId()).isEqualTo(update.getMessage().getChatId().toString());
        assertThat(actual.getText()).isEqualTo("Что хотите узнать?");
    }

    @Test
    public void handlesCatButtonTest() throws TelegramApiException {
        Update update = new Update();
        update.setMessage(new Message());
        update.getMessage().setChat(new Chat());
        update.getMessage().setText("/cat");
        update.getMessage().getChat().setId(123L);

        telegramBot.onUpdateReceived(update);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(userCatRepository).save(any(UserCat.class));
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getChatId()).isEqualTo(update.getMessage().getChatId().toString());
        assertThat(actual.getText()).isEqualTo("Что хотите узнать?");
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
        assertThat(actual.getChatId()).isEqualTo(update.getMessage().getChatId().toString());
        assertThat(actual.getText()).isEqualTo(
                "Требуется консультация волонтёра для пользователя по имени Name. Чат-айди пользователя - 123");

    }

    @Test
    public void handlesUnknownCommandTest() throws TelegramApiException {
        Update update = new Update();
        update.setMessage(new Message());
        update.getMessage().setText("Any unknown command");
        update.getMessage().setChat(new Chat());
        update.getMessage().getChat().setId(123L);

        telegramBot.onUpdateReceived(update);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getChatId()).isEqualTo(update.getMessage().getChatId().toString());
        assertThat(actual.getText()).isEqualTo("Нераспознанная команда, попробуйте ещё раз");
    }

    @Test
    public void handlesAddressDogButtonTest() throws TelegramApiException {
        Update update = new Update();
        update.setCallbackQuery(new CallbackQuery());
        update.getCallbackQuery().setMessage(new Message());
        update.getCallbackQuery().getMessage().setMessageId(1);
        update.getCallbackQuery().setData(ADDRESS_DOG_BUTTON);
        update.getCallbackQuery().getMessage().setChat(new Chat());
        update.getCallbackQuery().getMessage().getChat().setId(123L);

        telegramBot.onUpdateReceived(update);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getChatId()).isEqualTo(update.getCallbackQuery().getMessage().getChatId().toString());
        assertThat(actual.getText()).isEqualTo("""
                            ЖК Инфинити, Кабанбай батыра, 40, Астана 010000, Казахстан
                            Телефон: +7 707 818 0770
                            Часы работы: 9:00 - 21:00
                            """);
    }

    @Test
    public void handlesAddressCatButtonTest() throws TelegramApiException {
        Update update = new Update();
        update.setCallbackQuery(new CallbackQuery());
        update.getCallbackQuery().setData(ADDRESS_CAT_BUTTON);
        update.getCallbackQuery().setMessage(new Message());
        update.getCallbackQuery().getMessage().setMessageId(1);
        update.getCallbackQuery().getMessage().setChat(new Chat());
        update.getCallbackQuery().getMessage().getChat().setId(123L);

        telegramBot.onUpdateReceived(update);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getChatId()).isEqualTo(update.getCallbackQuery().getMessage().getChatId().toString());
        assertThat(actual.getText()).isEqualTo("""
                            улица Бейбитшилик 67, Астана 010000, Казахстан
                            Телефон: +7 701 874 3939
                            Часы работы: 10:00 - 20:00
                            """);
    }

    @Test
    public void handlesDrivingDirectionsDogButtonTest() throws TelegramApiException {
        Update update = new Update();
        update.setCallbackQuery(new CallbackQuery());
        update.getCallbackQuery().setData(DRIVING_DIRECTIONS_DOG_BUTTON);
        update.getCallbackQuery().setMessage(new Message());
        update.getCallbackQuery().getMessage().setMessageId(1);
        update.getCallbackQuery().getMessage().setChat(new Chat());
        update.getCallbackQuery().getMessage().getChat().setId(123L);

        telegramBot.onUpdateReceived(update);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getChatId()).isEqualTo(update.getCallbackQuery().getMessage().getChatId().toString());
        assertThat(actual.getText()).isEqualTo(DRIVING_DIRECTIONS_DOG_LINK);
    }

    @Test
    public void handlesDrivingDirectionsCatButtonTest() throws TelegramApiException {
        Update update = new Update();
        update.setCallbackQuery(new CallbackQuery());
        update.getCallbackQuery().setData(DRIVING_DIRECTIONS_CAT_BUTTON);
        update.getCallbackQuery().setMessage(new Message());
        update.getCallbackQuery().getMessage().setMessageId(1);
        update.getCallbackQuery().getMessage().setChat(new Chat());
        update.getCallbackQuery().getMessage().getChat().setId(123L);

        telegramBot.onUpdateReceived(update);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getChatId()).isEqualTo(update.getCallbackQuery().getMessage().getChatId().toString());
        assertThat(actual.getText()).isEqualTo(DRIVING_DIRECTIONS_CAT_LINK);
    }

    @Test
    public void safetyPrecautionsDogButtonTest() throws TelegramApiException {
        Update update = new Update();
        update.setCallbackQuery(new CallbackQuery());
        update.getCallbackQuery().setData(SAFETY_PRECAUTIONS_DOG_BUTTON);
        update.getCallbackQuery().setMessage(new Message());
        update.getCallbackQuery().getMessage().setMessageId(1);
        update.getCallbackQuery().getMessage().setChat(new Chat());
        update.getCallbackQuery().getMessage().getChat().setId(123L);

        telegramBot.onUpdateReceived(update);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getChatId()).isEqualTo(update.getCallbackQuery().getMessage().getChatId().toString());
        assertThat(actual.getText()).isEqualTo("""
                            Правила поведения с собаками.
                            В приюте обязательно слушайте, что говорит работник или волонтер.
                            Никогда не открывайте калитки вольеров без разрешения!
                            Не надо пытаться погладить животное через сетку! Все животные в приюте очень разные, среди них есть и агрессивные.\s
                            Не соблюдение техники безопасности может привезти к очень плачевным и трагическим ситуациям.
                            Так же вы можете случайно выпустить животных, что создаст дополнительные неприятности.\s
                            Будьте осторожны!""");
    }

    @Test
    public void safetyPrecautionsCatButtonTest() throws TelegramApiException {
        Update update = new Update();
        update.setCallbackQuery(new CallbackQuery());
        update.getCallbackQuery().setData(SAFETY_PRECAUTIONS_CAT_BUTTON);
        update.getCallbackQuery().setMessage(new Message());
        update.getCallbackQuery().getMessage().setMessageId(1);
        update.getCallbackQuery().getMessage().setChat(new Chat());
        update.getCallbackQuery().getMessage().getChat().setId(123L);

        telegramBot.onUpdateReceived(update);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getChatId()).isEqualTo(update.getCallbackQuery().getMessage().getChatId().toString());
        assertThat(actual.getText()).isEqualTo("""
                            Правила поведения с кошками.
                            Общаясь с кошками, помните об их желаниях и потребностях.
                            Если вы пришли с ребенком, показывайте ребенку на своем примере как общаться с кошкой.
                            Любое животное, даже самое ласковое и пушистое, имеет когти и зубы.
                            Прикасайтесь к кошкам бережно. Двигайтесь спокойно, без резких движений.
                            Не пугайте кошек криками и резкими звуками, не бросайте.
                            Не трогайте глаза и уши животных, не дергайте за шерсть и хвост.
                            Не кормите кошек продуктами, принесенными посетителями с собой. Уточните у администратора, что лучше принести в подарок.""");
    }


}

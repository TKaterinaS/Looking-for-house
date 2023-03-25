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
    public void handlesDogCommandTest() throws TelegramApiException {
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
    public void handlesCatCommandTest() throws TelegramApiException {
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

    @Test
    public void handlesCallVolunteerButtonTest() throws TelegramApiException {
        Update update = new Update();
        update.setCallbackQuery(new CallbackQuery());
        update.getCallbackQuery().setData(CALL_VOLUNTEER_BUTTON);
        update.getCallbackQuery().setMessage(new Message());
        update.getCallbackQuery().getMessage().setMessageId(1);
        update.getCallbackQuery().getMessage().setChat(new Chat());
        update.getCallbackQuery().getMessage().getChat().setId(123L);
        update.getCallbackQuery().getMessage().getChat().setFirstName("Name");

        when(telegramBot.generateRandomChatId()).thenReturn(123L);
        telegramBot.onUpdateReceived(update);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot, times(2)).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getChatId()).isEqualTo(update.getCallbackQuery().getMessage().getChatId().toString());
        assertThat(actual.getText()).isEqualTo(
                "Требуется консультация волонтёра для пользователя по имени Name. Чат-айди пользователя - 123");
    }

    @Test
    public void handlesDogButtonTest() throws TelegramApiException {
        Update update = new Update();
        update.setCallbackQuery(new CallbackQuery());
        update.getCallbackQuery().setData(DOG_BUTTON);
        update.getCallbackQuery().setMessage(new Message());
        update.getCallbackQuery().getMessage().setMessageId(1);
        update.getCallbackQuery().getMessage().setChat(new Chat());
        update.getCallbackQuery().getMessage().getChat().setId(123L);

        telegramBot.onUpdateReceived(update);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(userDogRepository).save(any(UserDog.class));
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getChatId()).isEqualTo(update.getCallbackQuery().getMessage().getChatId().toString());
        assertThat(actual.getText()).isEqualTo("Что хотите узнать?");
    }

    @Test
    public void handlesCatButtonTest() throws TelegramApiException {
        Update update = new Update();
        update.setCallbackQuery(new CallbackQuery());
        update.getCallbackQuery().setData(CAT_BUTTON);
        update.getCallbackQuery().setMessage(new Message());
        update.getCallbackQuery().getMessage().setMessageId(1);
        update.getCallbackQuery().getMessage().setChat(new Chat());
        update.getCallbackQuery().getMessage().getChat().setId(123L);

        telegramBot.onUpdateReceived(update);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(userCatRepository).save(any(UserCat.class));
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getChatId()).isEqualTo(update.getCallbackQuery().getMessage().getChatId().toString());
        assertThat(actual.getText()).isEqualTo("Что хотите узнать?");
    }

    @Test
    public void handlesInfoDogButtonTest() throws TelegramApiException {
        Update update = new Update();
        update.setCallbackQuery(new CallbackQuery());
        update.getCallbackQuery().setData(INFO_DOG_BUTTON);
        update.getCallbackQuery().setMessage(new Message());
        update.getCallbackQuery().getMessage().setMessageId(1);
        update.getCallbackQuery().getMessage().setChat(new Chat());
        update.getCallbackQuery().getMessage().getChat().setId(123L);

        telegramBot.onUpdateReceived(update);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getChatId()).isEqualTo(update.getCallbackQuery().getMessage().getChatId().toString());
        assertThat(actual.getText()).isEqualTo("Общая информация о приюте");
    }

    @Test
    public void handlesInfoCatButtonTest() throws TelegramApiException {
        Update update = new Update();
        update.setCallbackQuery(new CallbackQuery());
        update.getCallbackQuery().setData(INFO_CAT_BUTTON);
        update.getCallbackQuery().setMessage(new Message());
        update.getCallbackQuery().getMessage().setMessageId(1);
        update.getCallbackQuery().getMessage().setChat(new Chat());
        update.getCallbackQuery().getMessage().getChat().setId(123L);

        telegramBot.onUpdateReceived(update);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getChatId()).isEqualTo(update.getCallbackQuery().getMessage().getChatId().toString());
        assertThat(actual.getText()).isEqualTo("Общая информация о приюте");
    }

    @Test
    public void handlesToAdoptDogButtonTest() throws TelegramApiException {
        Update update = new Update();
        update.setCallbackQuery(new CallbackQuery());
        update.getCallbackQuery().setData(TO_ADOPT_DOG_BUTTON);
        update.getCallbackQuery().setMessage(new Message());
        update.getCallbackQuery().getMessage().setMessageId(1);
        update.getCallbackQuery().getMessage().setChat(new Chat());
        update.getCallbackQuery().getMessage().getChat().setId(123L);

        telegramBot.onUpdateReceived(update);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getChatId()).isEqualTo(update.getCallbackQuery().getMessage().getChatId().toString());
        assertThat(actual.getText()).isEqualTo("Как взять собаку из приюта?");
    }

    @Test
    public void handlesToAdoptCatButtonTest() throws TelegramApiException {
        Update update = new Update();
        update.setCallbackQuery(new CallbackQuery());
        update.getCallbackQuery().setData(TO_ADOPT_CAT_BUTTON);
        update.getCallbackQuery().setMessage(new Message());
        update.getCallbackQuery().getMessage().setMessageId(1);
        update.getCallbackQuery().getMessage().setChat(new Chat());
        update.getCallbackQuery().getMessage().getChat().setId(123L);

        telegramBot.onUpdateReceived(update);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getChatId()).isEqualTo(update.getCallbackQuery().getMessage().getChatId().toString());
        assertThat(actual.getText()).isEqualTo("Как взять кота из приюта?");
    }

    @Test
    public void handlesRulesDogButtonTest() throws TelegramApiException {
        Update update = new Update();
        update.setCallbackQuery(new CallbackQuery());
        update.getCallbackQuery().setData(RULES_DOG_BUTTON);
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
                1. Перед тем как принять решение завести домашнего питомца, вам нужно узнать о наличии аллергии на животных у себя или у проживающих с вами родственников.
                2. Чтобы наладить взаимоотношения с выбранным для пристройства песиком, можно взять его на прогулку, угостить собачьим лакомством, попробовать взять на руки. Постепенно животное привыкнет и пойдет навстречу. В отдельных ситуациях можно пригласить кинолога или зоопсихолога.
                3. при выборе конкретного животного стоит обратить внимание на чувства, возникающие при общении с ним.""");
    }

    @Test
    public void handlesRulesCatButtonTest() throws TelegramApiException {
        Update update = new Update();
        update.setCallbackQuery(new CallbackQuery());
        update.getCallbackQuery().setData(RULES_CAT_BUTTON);
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
                1.Перед тем как принять решение завести домашнего питомца, вам нужно узнать о наличии аллергии на животных у себя или у проживающих с вами родственников.
                2.Чтобы наладить взаимоотношения с выбранным Котиком можно прикормить его кошачьими вкусняшками, попробовать взять на руки. Постепенно животное привыкнет и пойдет навстречу. В отдельных ситуациях можно пригласить зоопсихолога.
                3.При выборе конкретного животного стоит обратить внимание на чувства, возникающие при общении с ним.""");
    }

    @Test
    public void handlesDocDogButtonTest() throws TelegramApiException {
        Update update = new Update();
        update.setCallbackQuery(new CallbackQuery());
        update.getCallbackQuery().setData(DOC_DOG_BUTTON);
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
                1. Паспорт.
                2. Договор заключённый между приютом и новым хозяином
                3. Оформленный Ветпаспорт.
                3. Контактные данные.
                4. Документ о пройденном собеседовании с волонтёром приюта.
                5. Справка об отсутствии аллергических реакциях на животных.""");
    }

    @Test
    public void handlesDocCatButtonTest() throws TelegramApiException {
        Update update = new Update();
        update.setCallbackQuery(new CallbackQuery());
        update.getCallbackQuery().setData(DOC_CAT_BUTTON);
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
                1. Паспорт.
                2. Договор заключённый между приютом и новым хозяином
                3. Оформленный Ветпаспорт.
                3. Контактные данные.
                4. Документ о пройденном собеседовании с волонтёром приюта.
                5. Справка об отсутствии аллергических реакциях на животных.""");
    }

    @Test
    public void handlesTransportationDogButtonTest() throws TelegramApiException {
        Update update = new Update();
        update.setCallbackQuery(new CallbackQuery());
        update.getCallbackQuery().setData(TRANSPORTATION_DOG_BUTTON);
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
                1. Если это взрослая собака, необходимо заранее приобрести поводок и ошейник.
                2. Если это щинок - при транспортировке в машине, необходимо предусмотреть, что бы переноска была пристёгнута ремнём безопасности.
                3. если это взрослая собака, в автомобиле необходимо установить авто гамак""");
    }

    @Test
    public void handlesTransportationCatButtonTest() throws TelegramApiException {
        Update update = new Update();
        update.setCallbackQuery(new CallbackQuery());
        update.getCallbackQuery().setData(TRANSPORTATION_CAT_BUTTON);
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
                1. Если это котёнок, необходимо приобрести переноску.
                2. При транспортировке в машине, необходимо предусмотреть, что бы переноска была пристёгнута ремнём безопасности.
                3. Для взрослой кошки (кота) так же необходима переноска""");
    }

    @Test
    public void handlesRecommendationPuppyButtonTest() throws TelegramApiException {
        Update update = new Update();
        update.setCallbackQuery(new CallbackQuery());
        update.getCallbackQuery().setData(RECOMMENDATION_PUPPY_BUTTON);
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
                1. Установите небольшие замки на кухонные шкафчики на уровне пола, особенно если там хранятся чистящие средства.
                2. Установите специальный детский барьер что бы собака не заходило туда, куда ей не следует.
                3. Избегайте нахождения в доме токсинов или уберите их куда-нибудь подальше.
                4. Держите дверцы духовки, холодильника, микроволновой печи, сушильной и стиральной машин всегда закрытыми.
                5. Постарайтесь найти такое место, где ваш питомец сможет играть и бегать, не рискуя своим здоровьем.""");
    }

    @Test
    public void handlesRecommendationKittyButtonTest() throws TelegramApiException {
        Update update = new Update();
        update.setCallbackQuery(new CallbackQuery());
        update.getCallbackQuery().setData(RECOMMENDATION_KITTY_BUTTON);
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
                1. Установите небольшие замки на кухонные шкафчики на уровне пола, особенно если там хранятся чистящие средства.
                2.Держите двери в доме запертыми что бы животное  не заходило туда, куда ему не следует.
                3. Избегайте нахождения в доме токсинов или уберите их куда-нибудь подальше.
                4. Держите дверцы духовки, холодильника, микроволновой печи, сушильной и стиральной машин всегда закрытыми.
                5. Постарайтесь найти такое место, где ваш питомец сможет играть и бегать, не рискуя своим здоровьем.
                6. Форточки в окнах должны быть обтянутыми москитной сеткой, что бы кошка или кот не выпали из окна в погоне за птицей.""");
    }

    @Test
    public void handlesRecommendationDogButton() throws TelegramApiException {
        Update update = new Update();
        update.setCallbackQuery(new CallbackQuery());
        update.getCallbackQuery().setData(RECOMMENDATION_DOG_BUTTON);
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
                1. Установите небольшие замки на кухонные шкафчики на уровне пола, особенно если там хранятся чистящие средства.
                2. Установите специальный детский барьер что бы собака не заходило туда, куда ей не следует.
                3. Избегайте нахождения в доме токсинов или уберите их куда-нибудь подальше.
                4. Держите дверцы духовки, холодильника, микроволновой печи, сушильной и стиральной машин всегда закрытыми.
                5. Постарайтесь найти такое место, где ваш питомец сможет играть и бегать, не рискуя своим здоровьем.""");
    }

    @Test
    public void handlesRecommendationCatButtonTest() throws TelegramApiException {
        Update update = new Update();
        update.setCallbackQuery(new CallbackQuery());
        update.getCallbackQuery().setData(RECOMMENDATION_CAT_BUTTON);
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
                1. Установите небольшие замки на кухонные шкафчики на уровне пола, особенно если там хранятся чистящие средства.
                2.Держите двери в доме запертыми что бы животное  не заходило туда, куда ему не следует.
                3. Избегайте нахождения в доме токсинов или уберите их куда-нибудь подальше.
                4. Держите дверцы духовки, холодильника, микроволновой печи, сушильной и стиральной машин всегда закрытыми.
                5. Постарайтесь найти такое место, где ваш питомец сможет играть и бегать, не рискуя своим здоровьем.
                6. Форточки в окнах должны быть обтянутыми москитной сеткой, что бы кошка или кот не выпали из окна в погоне за птицей.""");
    }

    @Test
    public void handlesRecHandicappedDogButtonTest() throws TelegramApiException {
        Update update = new Update();
        update.setCallbackQuery(new CallbackQuery());
        update.getCallbackQuery().setData(REC_HANDICAPPED_DOG_BUTTON);
        update.getCallbackQuery().setMessage(new Message());
        update.getCallbackQuery().getMessage().setMessageId(1);
        update.getCallbackQuery().getMessage().setChat(new Chat());
        update.getCallbackQuery().getMessage().getChat().setId(123L);

        telegramBot.onUpdateReceived(update);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot, times(2)).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getChatId()).isEqualTo(update.getCallbackQuery().getMessage().getChatId().toString());
        assertThat(actual.getText()).isEqualTo("""
                Рекомендации для животного с ограниченными возможностями по передвижению:
                Отсутствие лапы, паралич конечностей и другие проблемы со здоровьем не забирают у собак их любопытство и жизнерадостность. Таким питомцам нужен особый уход, но в остальном такие собаки не отличаются от тех, кто бегает на четырех лапах.
                Это заблуждение, считать, что животное, которое ограничено в движении из-за проблем со здоровьем, не имеет шансов на счастливую жизнь. Если дать собаке возможность передвигаться с помощью специальной коляски, она не заметит разницу и будет также бегать за игрушками и знакомиться с сородичами.
                В целом воспитание таких собак ничем не отличается от того, которое используют для здоровых питомцев. Хозяину важно только грамотно распределять нагрузку на животное, чтобы питомец не устал во время прогулки, игры или дрессировки.
                Собакам с ограниченными возможностями здоровья, безусловно требуется больше заботы, внимания и любви со стороны хозяина. Не нужно жалеть собаку, адаптируйтесь вместе с ней, используйте те органы чувств, которыми обладает питомец.""");
    }

    @Test
    public void handlesRecHandicappedCatButtonTest() throws TelegramApiException {
        Update update = new Update();
        update.setCallbackQuery(new CallbackQuery());
        update.getCallbackQuery().setData(REC_HANDICAPPED_CAT_BUTTON);
        update.getCallbackQuery().setMessage(new Message());
        update.getCallbackQuery().getMessage().setMessageId(1);
        update.getCallbackQuery().getMessage().setChat(new Chat());
        update.getCallbackQuery().getMessage().getChat().setId(123L);

        telegramBot.onUpdateReceived(update);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot, times(2)).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getChatId()).isEqualTo(update.getCallbackQuery().getMessage().getChatId().toString());
        assertThat(actual.getText()).isEqualTo("""
                Рекомендации для животного с ограниченными возможностями по передвижению:
                Отсутствие лапы, паралич конечностей и другие проблемы со здоровьем не забирают у кошек их любопытство и жизнерадостность. Таким питомцам нужен особый уход, но в остальном такие кошки не отличаются от тех, кто бегает на четырех лапах.
                Котам с ограниченными возможностями здоровья, безусловно требуется больше заботы, внимания и любви со стороны хозяина. Не нужно жалеть кошку, адаптируйтесь вместе с ней, используйте те органы чувств, которыми обладает питомец.""");

    }

    @Test
    public void handlesAdvicesCynologistsButtonTest() throws TelegramApiException {
        Update update = new Update();
        update.setCallbackQuery(new CallbackQuery());
        update.getCallbackQuery().setData(ADVICES_CYNOLOGISTS_BUTTON);
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
                1. Не навязывайте собаке своё общество.
                2. Не мешайте животному самостоятельно исследовать новое окружение.
                3. Не устраивайте “смотрины”.
                4. Не торопитесь ухаживать за собакой - кормить её, поить водой, гладить или затаскивать в ванну.
                5. Если вы столкнулись со страхами собаки, действуйте спокойно и ласково.""");
    }

    @Test
    public void handlesCynologistsButtonTest() throws TelegramApiException {
        Update update = new Update();
        update.setCallbackQuery(new CallbackQuery());
        update.getCallbackQuery().setData(CYNOLOGISTS_BUTTON);
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
                Проверенные кинологи для дальнейшего общения:
                1.Центр кинологии и фелинологии, ветеринарная клиника, зоомагазин"Зоосфера".
                """);
    }

    @Test
    public void handlesVetCenterButtonTest() throws TelegramApiException {
        Update update = new Update();
        update.setCallbackQuery(new CallbackQuery());
        update.getCallbackQuery().setData(VET_CENTER_BUTTON);
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
                Проверенные Ветеринарные центры для дальнейшего общения:
                1.Вет Клиника "Зоолюкс".
                2.Клиника ветеринарной медицины "Византия".
                3.Ветеринарная клиника "Astana".
                4.Ветеринарная клиника "Aqtaban".""");
    }

    @Test
    public void handlesReasonsForRefusalButtonTest() throws TelegramApiException {
        Update update = new Update();
        update.setCallbackQuery(new CallbackQuery());
        update.getCallbackQuery().setData(REASONS_FOR_REFUSAL_BUTTON);
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
                Список причин, почему могут отказать и не дать забрать животное из приюта:
                1. Большое количество животных дома.
                2. Нестабильные отношения в семье.
                3. Наличие маленьких детей.
                4. Съемное жилье
                5. Животное в подарок или для работы.
                """);
    }

    @Test
    public void handlesSaveContactButtonTest() throws TelegramApiException {
        Update update = new Update();
        update.setCallbackQuery(new CallbackQuery());
        update.getCallbackQuery().setData(SAVE_CONTACT_BUTTON);
        update.getCallbackQuery().setMessage(new Message());
        update.getCallbackQuery().getMessage().setMessageId(1);
        update.getCallbackQuery().getMessage().setChat(new Chat());
        update.getCallbackQuery().getMessage().getChat().setId(123L);

        telegramBot.onUpdateReceived(update);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getChatId()).isEqualTo(update.getCallbackQuery().getMessage().getChatId().toString());
        assertThat(actual.getText()).isEqualTo("Выберите, что хотите отправить");
    }

    @Test
    public void handlesSubmitReportButtonTest() throws TelegramApiException {
        Update update = new Update();
        update.setCallbackQuery(new CallbackQuery());
        update.getCallbackQuery().setData(SUBMIT_REPORT_BUTTON);
        update.getCallbackQuery().setMessage(new Message());
        update.getCallbackQuery().getMessage().setMessageId(1);
        update.getCallbackQuery().getMessage().setChat(new Chat());
        update.getCallbackQuery().getMessage().getChat().setId(123L);

        telegramBot.onUpdateReceived(update);

        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        verify(telegramBot, times(2)).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        assertThat(actual.getChatId()).isEqualTo(update.getCallbackQuery().getMessage().getChatId().toString());
        assertThat(actual.getText()).isEqualTo("""
                Рацион: ваш текст;
                Самочувствие: ваш текст;
                Поведение: ваш текст;""");
    }

}
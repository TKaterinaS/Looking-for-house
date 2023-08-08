package ru.team2.lookingforhouse.service;

import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import ru.team2.lookingforhouse.config.BotConfig;
import ru.team2.lookingforhouse.model.ReportCat;
import ru.team2.lookingforhouse.model.ReportDog;
import ru.team2.lookingforhouse.model.UserCat;
import ru.team2.lookingforhouse.model.UserDog;
import ru.team2.lookingforhouse.repository.ReportCatRepository;
import ru.team2.lookingforhouse.repository.ReportDogRepository;
import ru.team2.lookingforhouse.repository.UserCatRepository;
import ru.team2.lookingforhouse.repository.UserDogRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static ru.team2.lookingforhouse.util.Constant.*;
import static ru.team2.lookingforhouse.util.UserStatus.*;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {
    private final UserCatRepository userCatRepository;
    private final UserDogRepository userDogRepository;
    private final ReportCatRepository reportCatRepository;
    private final ReportDogRepository reportDogRepository;
    private final BotConfig config;
    private final Pattern patternDog = Pattern.compile(REGEX_MESSAGE_DOG);
    private final Pattern patternCat = Pattern.compile(REGEX_MESSAGE_CAT);

    @Autowired
    public TelegramBot(BotConfig config, UserCatRepository userCatRepository, UserDogRepository userDogRepository,
                       ReportCatRepository reportCatRepository, ReportDogRepository reportDogRepository) {
        this.userCatRepository = userCatRepository;
        this.userDogRepository = userDogRepository;
        this.reportCatRepository = reportCatRepository;
        this.reportDogRepository = reportDogRepository;
        this.config = config;
//         Создание кнопки меню (все команды должны быть написаны в нижнем регистре)
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "Приветствует пользователя"));
        listOfCommands.add(new BotCommand("/dog", "Приют для собак"));
        listOfCommands.add(new BotCommand("/cat", "Приют для котов"));
        listOfCommands.add(new BotCommand("/call_volunteer", "Вызвать волотера"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Ошибка: " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getBotToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            String userName = update.getMessage().getChat().getFirstName();
            long chatId = update.getMessage().getChatId();

            switch (messageText) {
//                При выполнении команды старт Бот приветствует пользователя и предлагает ему выбрать,
//                какой приют его интересует.
                case "/start" -> {
                    startCommandReceived(chatId, userName);
                    dogOrCat(chatId);
                }
//                    если выбрали приют собак, происходит регистрация пользователя и запись его данных в таблицу UserDog
                case "/dog" -> {
                    registerUserDog(update.getMessage());
                    startDog(chatId);
                }
//                    если выбрали приют кошек, происходит регистрация пользователя и запись его данных в таблицу UserCat
                case "/cat" -> {
                    registerUserCat(update.getMessage());
                    startCat(chatId);
                }
//                выполнение команды /call_volunteer (вызов волонтера)
                case "/call_volunteer" -> sendMsgToVolunteer(chatId, userName);
//                дефолтное сообщение, если бот получит неизвестную ему команду
                default -> sendMessage(chatId, "Нераспознанная команда, попробуйте ещё раз");
            }
        }
//        Обработка запросов из кнопок бота
        else if (update.hasCallbackQuery()) {
            String callbackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            String userName = update.getCallbackQuery().getMessage().getChat().getFirstName();
//            Адрес, контакты, часы работы приюта для собак
            switch (callbackData) {
                case ADDRESS_DOG_BUTTON -> sendMessage(chatId, """
                        ЖК Инфинити, Кабанбай батыра, 40, Астана 010000, Казахстан
                        Телефон: +7 707 818 0770
                        Часы работы: 9:00 - 21:00
                        """);

//                Адрес, контакты, часы работы приюта для котов
                case ADDRESS_CAT_BUTTON -> sendMessage(chatId, """
                        улица Бейбитшилик 67, Астана 010000, Казахстан
                        Телефон: +7 701 874 3939
                        Часы работы: 10:00 - 20:00
                        """);

                /*Схема проезда в приют для собак*/
                case DRIVING_DIRECTIONS_DOG_BUTTON -> sendMessage(chatId, DRIVING_DIRECTIONS_DOG_LINK);

                /*Схема проезда в приют для котов*/
                case DRIVING_DIRECTIONS_CAT_BUTTON -> sendMessage(chatId, DRIVING_DIRECTIONS_CAT_LINK);

                /*Правила поведения с собаками*/
                case SAFETY_PRECAUTIONS_DOG_BUTTON -> {
                    String rules = """
                            Правила поведения с собаками.
                            В приюте обязательно слушайте, что говорит работник или волонтер.
                            Никогда не открывайте калитки вольеров без разрешения!
                            Не надо пытаться погладить животное через сетку! Все животные в приюте очень разные, среди них есть и агрессивные.\s
                            Не соблюдение техники безопасности может привезти к очень плачевным и трагическим ситуациям.
                            Так же вы можете случайно выпустить животных, что создаст дополнительные неприятности.\s
                            Будьте осторожны!""";
                    sendMessage(chatId, rules);

                    break;
                }

                /*Правила поведения с котами*/
                case SAFETY_PRECAUTIONS_CAT_BUTTON -> {
                    String rules = """
                            Правила поведения с кошками.
                            Общаясь с кошками, помните об их желаниях и потребностях.
                            Если вы пришли с ребенком, показывайте ребенку на своем примере как общаться с кошкой.
                            Любое животное, даже самое ласковое и пушистое, имеет когти и зубы.
                            Прикасайтесь к кошкам бережно. Двигайтесь спокойно, без резких движений.
                            Не пугайте кошек криками и резкими звуками, не бросайте.
                            Не трогайте глаза и уши животных, не дергайте за шерсть и хвост.
                            Не кормите кошек продуктами, принесенными посетителями с собой. Уточните у администратора, что лучше принести в подарок.""";
                    sendMessage(chatId, rules);
                    break;
                }

                /*Вызов волонтера*/
                case CALL_VOLUNTEER_BUTTON -> sendMsgToVolunteer(chatId, userName);

                /*Вызов кнопок, содержащих информацию по приюту для собак*/
                case DOG_BUTTON -> {
                    registerUserDog(update.getCallbackQuery().getMessage());
                    startDog(chatId);
                }
                /*Вызов кнопок, содержащих информацию по приюту для котов*/
                case CAT_BUTTON -> {
                    registerUserCat(update.getCallbackQuery().getMessage());
                    startCat(chatId);
                }
                /*Общая информация о приюте для собак*/
                case INFO_DOG_BUTTON -> infoDog(chatId);

                /*Общая информация о приюте для котов*/
                case INFO_CAT_BUTTON -> infoCat(chatId);

                /*Вызов кнопок. Как взять собаку из приюта*/
                case TO_ADOPT_DOG_BUTTON -> howToAdoptDog(chatId);

                /*Вызов кнопок. Как взять кота из приюта*/
                case TO_ADOPT_CAT_BUTTON -> howToAdoptCat(chatId);

                /*Правила знакомства с собакой до того, как можно забрать ее из приюта.*/
                case RULES_DOG_BUTTON -> {
                    String rules = """
                            1. Перед тем как принять решение завести домашнего питомца, вам нужно узнать о наличии аллергии на животных у себя или у проживающих с вами родственников.
                            2. Чтобы наладить взаимоотношения с выбранным для пристройства песиком, можно взять его на прогулку, угостить собачьим лакомством, попробовать взять на руки. Постепенно животное привыкнет и пойдет навстречу. В отдельных ситуациях можно пригласить кинолога или зоопсихолога.
                            3. при выборе конкретного животного стоит обратить внимание на чувства, возникающие при общении с ним.""";
                    sendMessage(chatId, rules);
                    break;
                }

                /*Правила знакомства с котом до того, как можно забрать его из приюта.*/
                case RULES_CAT_BUTTON -> {
                    String rules = """
                            1.Перед тем как принять решение завести домашнего питомца, вам нужно узнать о наличии аллергии на животных у себя или у проживающих с вами родственников.
                            2.Чтобы наладить взаимоотношения с выбранным Котиком можно прикормить его кошачьими вкусняшками, попробовать взять на руки. Постепенно животное привыкнет и пойдет навстречу. В отдельных ситуациях можно пригласить зоопсихолога.
                            3.При выборе конкретного животного стоит обратить внимание на чувства, возникающие при общении с ним.""";
                    sendMessage(chatId, rules);
                    break;
                }

                /*Документы для того, чтобы забрать собаку из приюта*/
                case DOC_DOG_BUTTON -> {
                    String doc = """
                            1. Паспорт.
                            2. Договор заключённый между приютом и новым хозяином
                            3. Оформленный Ветпаспорт.
                            3. Контактные данные.
                            4. Документ о пройденном собеседовании с волонтёром приюта.
                            5. Справка об отсутствии аллергических реакциях на животных.""";
                    sendMessage(chatId, doc);
                    break;
                }

                /*Документы для того, чтобы забрать кота из приюта*/
                case DOC_CAT_BUTTON -> {
                    String doc = """
                            1. Паспорт.
                            2. Договор заключённый между приютом и новым хозяином
                            3. Оформленный Ветпаспорт.
                            3. Контактные данные.
                            4. Документ о пройденном собеседовании с волонтёром приюта.
                            5. Справка об отсутствии аллергических реакциях на животных.""";
                    sendMessage(chatId, doc);
                    break;
                }

                /*Рекомендации по транспортировке собаки*/
                case TRANSPORTATION_DOG_BUTTON -> {
                    String recTrans = """
                            1. Если это взрослая собака, необходимо заранее приобрести поводок и ошейник.
                            2. Если это щинок - при транспортировке в машине, необходимо предусмотреть, что бы переноска была пристёгнута ремнём безопасности.
                            3. если это взрослая собака, в автомобиле необходимо установить авто гамак""";
                    sendMessage(chatId, recTrans);
                    break;
                }

                /*Рекомендации по транспортировке кота*/
                case TRANSPORTATION_CAT_BUTTON -> {
                    String recTrans = """
                            1. Если это котёнок, необходимо приобрести переноску.
                            2. При транспортировке в машине, необходимо предусмотреть, что бы переноска была пристёгнута ремнём безопасности.
                            3. Для взрослой кошки (кота) так же необходима переноска""";
                    sendMessage(chatId, recTrans);
                    break;
                }

                /*Рекомендации по обустройству дома для щенка*/
                case RECOMMENDATION_PUPPY_BUTTON -> {
                    String recPuppy = """
                            1. Установите небольшие замки на кухонные шкафчики на уровне пола, особенно если там хранятся чистящие средства.
                            2. Установите специальный детский барьер что бы собака не заходило туда, куда ей не следует.
                            3. Избегайте нахождения в доме токсинов или уберите их куда-нибудь подальше.
                            4. Держите дверцы духовки, холодильника, микроволновой печи, сушильной и стиральной машин всегда закрытыми.
                            5. Постарайтесь найти такое место, где ваш питомец сможет играть и бегать, не рискуя своим здоровьем.""";
                    sendMessage(chatId, recPuppy);
                }
                /*Рекомендации по обустройству дома для котенка*/
                case RECOMMENDATION_KITTY_BUTTON -> {
                    String recKitty = """
                            1. Установите небольшие замки на кухонные шкафчики на уровне пола, особенно если там хранятся чистящие средства.
                            2.Держите двери в доме запертыми что бы животное  не заходило туда, куда ему не следует.
                            3. Избегайте нахождения в доме токсинов или уберите их куда-нибудь подальше.
                            4. Держите дверцы духовки, холодильника, микроволновой печи, сушильной и стиральной машин всегда закрытыми.
                            5. Постарайтесь найти такое место, где ваш питомец сможет играть и бегать, не рискуя своим здоровьем.
                            6. Форточки в окнах должны быть обтянутыми москитной сеткой, что бы кошка или кот не выпали из окна в погоне за птицей.""";
                    sendMessage(chatId, recKitty);
                }
                /*Рекомендации по обустройству дома для кота*/
                case RECOMMENDATION_CAT_BUTTON -> {
                    String recCat = """
                            1. Установите небольшие замки на кухонные шкафчики на уровне пола, особенно если там хранятся чистящие средства.
                            2.Держите двери в доме запертыми что бы животное  не заходило туда, куда ему не следует.
                            3. Избегайте нахождения в доме токсинов или уберите их куда-нибудь подальше.
                            4. Держите дверцы духовки, холодильника, микроволновой печи, сушильной и стиральной машин всегда закрытыми.
                            5. Постарайтесь найти такое место, где ваш питомец сможет играть и бегать, не рискуя своим здоровьем.
                            6. Форточки в окнах должны быть обтянутыми москитной сеткой, что бы кошка или кот не выпали из окна в погоне за птицей.""";
                    sendMessage(chatId, recCat);
                }
                /*Рекомендации по обустройству дома для собаки*/
                case RECOMMENDATION_DOG_BUTTON -> {
                    String recDog = EmojiParser.parseToUnicode
                            ("""
                                    1. Установите небольшие замки на кухонные шкафчики на уровне пола, особенно если там хранятся чистящие средства.
                                    2. Установите специальный детский барьер что бы собака не заходило туда, куда ей не следует.
                                    3. Избегайте нахождения в доме токсинов или уберите их куда-нибудь подальше.
                                    4. Держите дверцы духовки, холодильника, микроволновой печи, сушильной и стиральной машин всегда закрытыми.
                                    5. Постарайтесь найти такое место, где ваш питомец сможет играть и бегать, не рискуя своим здоровьем.""");
                    sendMessage(chatId, recDog);
                }
                /*Рекомендации для собак с ограниченными возможностями*/
                case REC_HANDICAPPED_DOG_BUTTON -> {
                    String vision = "Рекомендации для животного с ограниченными возможностями по зрению:\n" +
                            "Зрение - Может показаться, что отсутствие зрения у собаки станет сильной преградой " +
                            "для ее полноценной жизни, но слепой питомец достаточно быстро адаптируется к этой проблеме " +
                            "и начинает использовать слух и нюх, чтобы ориентироваться в окружающей среде. " +
                            "Для такого питомца очень важно обеспечить безопасную среду: в первую очередь внимательно " +
                            "осмотрите квартиру, уберите все торчащие предметы, которые могут поранить собаку, огородите лестницы, " +
                            "закройте люки и ямы во дворе частного дома и уберите все провода. Чтобы собака легко ориентировалась дома, " +
                            "вы можете использовать узкие ковровые дорожки, по которым питомец будет ходить к своему месту, к миске, к дверям. " +
                            "Убедитесь, что, когда вы уходите из дома, собака остается в безопасном пространстве.\n" +
                            "В общении с вами такая собака будет ориентироваться по вашему голосу и запаху. " +
                            "Дрессировать таких собак следует используя хорошо понятный для нее звуковой сигнал, " +
                            "это может быть писк игрушки или любой другой звук. Собака должна понимать основные сигналы, " +
                            "способные защитить ее в нужный момент, например, «Стой!», «Осторожно!». Чтобы она запомнила эти команды, " +
                            "произносите их громко и понятно и сопровождайте их ограничением движения (натянутый поводок, рука перед грудью собаки). " +
                            "При прогулках старайтесь двигаться только несколькими постоянными маршрутами. Со временем собака начнет привыкать к звукам, " +
                            "запахам и тактильным ощущениям. Вы можете удивиться, но вскоре питомец запомнит каждый маршрут и сможет гулять без вашей " +
                            "постоянной помощи и подсказок.\n" +
                            "Дрессировка слепой собаки потребует от вас много времени и терпения, помните, что для нее ключевыми сигналами будут звуки, прикосновения.";
                    String movement = "Рекомендации для животного с ограниченными возможностями по передвижению:\n" +
                            "Отсутствие лапы, паралич конечностей и другие проблемы со здоровьем не забирают у собак их любопытство" +
                            " и жизнерадостность. Таким питомцам нужен особый уход, но в остальном такие собаки не отличаются от тех, " +
                            "кто бегает на четырех лапах.\n" +
                            "Это заблуждение, считать, что животное, которое ограничено в движении из-за проблем со здоровьем, " +
                            "не имеет шансов на счастливую жизнь. Если дать собаке возможность передвигаться с помощью специальной коляски," +
                            " она не заметит разницу и будет также бегать за игрушками и знакомиться с сородичами.\n" +
                            "В целом воспитание таких собак ничем не отличается от того, которое используют для здоровых питомцев. " +
                            "Хозяину важно только грамотно распределять нагрузку на животное, чтобы питомец не устал во время прогулки, " +
                            "игры или дрессировки.\n" +
                            "Собакам с ограниченными возможностями здоровья, безусловно требуется больше заботы, внимания и любви " +
                            "со стороны хозяина. Не нужно жалеть собаку, адаптируйтесь вместе с ней, используйте те органы чувств, " +
                            "которыми обладает питомец.";
                    sendMessage(chatId, vision);
                    sendMessage(chatId, movement);
                    break;
                }

                /*Рекомендации для котов с ограниченными возможностями*/
                case REC_HANDICAPPED_CAT_BUTTON -> {
                    String vision = "Рекомендации для животного с ограниченными возможностями по зрению:\n" +
                            "Зрение - Может показаться, что отсутствие зрения у кота станет сильной преградой " +
                            "для ее полноценной жизни, но слепой питомец достаточно быстро адаптируется к этой проблеме " +
                            "и начинает использовать слух и нюх, чтобы ориентироваться в окружающей среде. " +
                            "Для такого питомца очень важно обеспечить безопасную среду: в первую очередь внимательно " +
                            "осмотрите квартиру, уберите все торчащие предметы, которые могут поранить кота, огородите лестницы, " +
                            "закройте люки и ямы во дворе частного дома и уберите все провода. Чтобы кот легко ориентировалась дома, " +
                            "вы можете использовать узкие ковровые дорожки, по которым питомец будет ходить к своему месту, к миске, к дверям. " +
                            "Убедитесь, что, когда вы уходите из дома, кот остается в безопасном пространстве.\n" +
                            "В общении с вами такой кот будет ориентироваться по вашему голосу и запаху. ";
                    String movement = "Рекомендации для животного с ограниченными возможностями по передвижению:\n" +
                            "Отсутствие лапы, паралич конечностей и другие проблемы со здоровьем не забирают у кошек их любопытство" +
                            " и жизнерадостность. Таким питомцам нужен особый уход, но в остальном такие кошки не отличаются от тех, " +
                            "кто бегает на четырех лапах.\n" +
                            "Котам с ограниченными возможностями здоровья, безусловно требуется больше заботы, внимания и любви " +
                            "со стороны хозяина. Не нужно жалеть кошку, адаптируйтесь вместе с ней, используйте те органы чувств, " +
                            "которыми обладает питомец.";
                    sendMessage(chatId, vision);
                    sendMessage(chatId, movement);
                    break;
                }

                /*Советы кинолога по первичному общению с собакой*/
                case ADVICES_CYNOLOGISTS_BUTTON -> {
                    String advices = """
                            1. Не навязывайте собаке своё общество.
                            2. Не мешайте животному самостоятельно исследовать новое окружение.
                            3. Не устраивайте “смотрины”.
                            4. Не торопитесь ухаживать за собакой - кормить её, поить водой, гладить или затаскивать в ванну.
                            5. Если вы столкнулись со страхами собаки, действуйте спокойно и ласково.""";
                    sendMessage(chatId, advices);
                }
                /*Проверенные кинологи*/
                case CYNOLOGISTS_BUTTON -> {
                    String cynolog = """
                            Проверенные кинологи для дальнейшего общения:
                            1.Центр кинологии и фелинологии, ветеринарная клиника, зоомагазин"Зоосфера".
                            """;
                    sendMessage(chatId, cynolog);
                }
                /*Проверенные вет.центры (для кошек)*/
                case VET_CENTER_BUTTON -> {
                    String veterinaryCenter = """
                            Проверенные Ветеринарные центры для дальнейшего общения:
                            1.Вет Клиника "Зоолюкс".
                            2.Клиника ветеринарной медицины "Византия".
                            3.Ветеринарная клиника "Astana".
                            4.Ветеринарная клиника "Aqtaban".""";
                    sendMessage(chatId, veterinaryCenter);
                }
                /*Причины отказа*/
                case REASONS_FOR_REFUSAL_BUTTON -> {
                    String refusal = """
                            Список причин, почему могут отказать и не дать забрать животное из приюта:
                            1. Большое количество животных дома.
                            2. Нестабильные отношения в семье.
                            3. Наличие маленьких детей.
                            4. Съемное жилье
                            5. Животное в подарок или для работы.
                            """;
                    sendMessage(chatId, refusal);
                }
                /*Сохранить контакты того, кто выбрал приют собак*/
                case SAVE_CONTACT_BUTTON -> createSaveContactButtons(chatId);
                case SUBMIT_REPORT_DOG_BUTTON -> {
                    String infoAboutReport = """
                            Для отчета нужна следующая информация:
                            - Фото животного.  
                            - Рацион животного
                            - Общее самочувствие и привыкание к новому месту
                            - Изменение в поведении: отказ от старых привычек, приобретение новых.
                            Скопируйте следующий пример. Не забудьте прикрепить фото
                            """;
                    String reportExample = """
                            Отчёт для приюта собак:
                            Рацион: ваш текст;
                            Самочувствие: ваш текст;
                            Поведение: ваш текст;""";
                    sendMessage(chatId, infoAboutReport);
                    sendMessage(chatId, reportExample);
                }
                case SUBMIT_REPORT_CAT_BUTTON -> {
                    String infoAboutReport = """
                            Для отчета нужна следующая информация:
                            - Фото животного.  
                            - Рацион животного
                            - Общее самочувствие и привыкание к новому месту
                            - Изменение в поведении: отказ от старых привычек, приобретение новых.
                            Скопируйте следующий пример. Не забудьте прикрепить фото
                            """;
                    String reportExample = """
                            Отчёт для приюта кошек:
                            Рацион: ваш текст;
                            Самочувствие: ваш текст;
                            Поведение: ваш текст;""";
                    sendMessage(chatId, infoAboutReport);
                    sendMessage(chatId, reportExample);
                }
            }
        } else if (update.hasMessage() && update.getMessage().hasContact()) {
            Message inMsg = update.getMessage();
            long chatID = update.getMessage().getChatId();
            if (userDogRepository.findUserDogByChatId(chatID) != null
                    && userDogRepository.findUserDogByChatId(chatID).getPhoneNumber() == null) {
                saveContactDog(inMsg);
                sendMessage(chatID, "Данные в приют собак успешно сохранены");
            } else if (userCatRepository.findUserCatByChatId(chatID) != null
                    && userCatRepository.findUserCatByChatId(chatID).getPhoneNumber() == null) {
                saveContactCat(inMsg);
                sendMessage(chatID, "Данные в приют кошек успешно сохранены");
            }

        } else if (update.hasMessage() && update.getMessage().hasPhoto() && update.getMessage().getCaption() != null) {
            String caption = update.getMessage().getCaption();
            long chatId = update.getMessage().getChatId();
            getReport(update, caption);

        } else if (update.hasMessage() && update.getMessage().hasPhoto() && update.getMessage().getCaption() == null) {
            long chatId = update.getMessage().getChatId();
            sendMessage(chatId, "Необходимо к фото питомца добавить текстовый отчёт по шаблону");

        }
    }


    /**
     * Создание кнопок выбора приюта (собаки или коты)
     *
     * @param chatId в качесте параметра используется чат-айди пользователя
     */
    private void dogOrCat(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        String answer = EmojiParser.parseToUnicode("Выберете приют собак " + ":dog: " + " или кошек " + " :cat:");
        message.setText(answer);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();
        var dogButton = new InlineKeyboardButton();
        dogButton.setText("Собаки");

        dogButton.setCallbackData("DOG_BUTTON");

        var catButton = new InlineKeyboardButton();
        catButton.setText("Кошки");
        catButton.setCallbackData("CAT_BUTTON");

        List<InlineKeyboardButton> row = List.of(dogButton, catButton);
        rowsInLine.add(row);

        markup.setKeyboard(rowsInLine);
        message.setReplyMarkup(markup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Возникла ошибка: " + e.getMessage());
        }
    }

    /**
     * Кнопки информации о приюте для собак
     *
     * @param chatId в качесте параметра используется чат-айди пользователя
     */
    private void startDog(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        String answer = EmojiParser.parseToUnicode("Что хотите узнать?");
        message.setText(answer);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();

        var infoButton = new InlineKeyboardButton();
        infoButton.setText("Узнать информацию о приюте для собак");
        infoButton.setCallbackData(INFO_DOG_BUTTON);

        var toAdoptButton = new InlineKeyboardButton();
        toAdoptButton.setText("Как взять собаку из приюта?");
        toAdoptButton.setCallbackData(TO_ADOPT_DOG_BUTTON);

        var submitReportButton = new InlineKeyboardButton();
        submitReportButton.setText("Прислать отчет о питомце");
        submitReportButton.setCallbackData(SUBMIT_REPORT_DOG_BUTTON);

        var callVolunteerButton = new InlineKeyboardButton();
        callVolunteerButton.setText("Позвать волонтера");
        callVolunteerButton.setCallbackData(CALL_VOLUNTEER_BUTTON);

        List<InlineKeyboardButton> row1 = List.of(infoButton);
        List<InlineKeyboardButton> row2 = List.of(toAdoptButton);
        List<InlineKeyboardButton> row3 = List.of(submitReportButton);
        List<InlineKeyboardButton> row4 = List.of(callVolunteerButton);

        rowsInLine.add(row1);
        rowsInLine.add(row2);
        rowsInLine.add(row3);
        rowsInLine.add(row4);

        markup.setKeyboard(rowsInLine);
        message.setReplyMarkup(markup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Возникла ошибка: " + e.getMessage());
        }
    }

    /**
     * Кнопки информации о приюте для кошек
     *
     * @param chatId в качесте параметра используется чат-айди пользователя
     */

    private void startCat(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        String answer = EmojiParser.parseToUnicode("Что хотите узнать?");
        message.setText(answer);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();

        var infoButton = new InlineKeyboardButton();
        infoButton.setText("Узнать информацию о приюте для котов");
        infoButton.setCallbackData(INFO_CAT_BUTTON);

        var toAdoptButton = new InlineKeyboardButton();
        toAdoptButton.setText("Как взять кота из приюта?");
        toAdoptButton.setCallbackData(TO_ADOPT_CAT_BUTTON);

        var submitReportButton = new InlineKeyboardButton();
        submitReportButton.setText("Прислать отчет о питомце");
        submitReportButton.setCallbackData(SUBMIT_REPORT_CAT_BUTTON);

        var callVolunteerButton = new InlineKeyboardButton();
        callVolunteerButton.setText("Позвать волонтера");
        callVolunteerButton.setCallbackData(CALL_VOLUNTEER_BUTTON);

        List<InlineKeyboardButton> row1 = List.of(infoButton);
        List<InlineKeyboardButton> row2 = List.of(toAdoptButton);
        List<InlineKeyboardButton> row3 = List.of(submitReportButton);
        List<InlineKeyboardButton> row4 = List.of(callVolunteerButton);

        rowsInLine.add(row1);
        rowsInLine.add(row2);
        rowsInLine.add(row3);
        rowsInLine.add(row4);

        markup.setKeyboard(rowsInLine);
        message.setReplyMarkup(markup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Возникла ошибка: " + e.getMessage());
        }
    }

    private void infoDog(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Общая информация о приюте");
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();

        var addressDogButton = new InlineKeyboardButton();
        addressDogButton.setText("Адрес, контакты и часы приёма");
        addressDogButton.setCallbackData(ADDRESS_DOG_BUTTON);

        var drivingDirectionsButton = new InlineKeyboardButton();
        drivingDirectionsButton.setText("Схема проезда");
        drivingDirectionsButton.setCallbackData(DRIVING_DIRECTIONS_DOG_BUTTON);

        var safetyPrecautionsButton = new InlineKeyboardButton();
        safetyPrecautionsButton.setText("Правила на территории приюта");
        safetyPrecautionsButton.setCallbackData(SAFETY_PRECAUTIONS_DOG_BUTTON);

        var callVolunteerButton = new InlineKeyboardButton();
        callVolunteerButton.setText("Позвать волонтера");
        callVolunteerButton.setCallbackData(CALL_VOLUNTEER_BUTTON);

        List<InlineKeyboardButton> row1 = List.of(addressDogButton);
        List<InlineKeyboardButton> row2 = List.of(drivingDirectionsButton);
        List<InlineKeyboardButton> row3 = List.of(safetyPrecautionsButton);
        List<InlineKeyboardButton> row4 = List.of(callVolunteerButton);

        rowsInLine.add(row1);
        rowsInLine.add(row2);
        rowsInLine.add(row3);
        rowsInLine.add(row4);

        markup.setKeyboard(rowsInLine);
        message.setReplyMarkup(markup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Возникла ошибка: " + e.getMessage());
        }
    }

    private void infoCat(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Общая информация о приюте");
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();

        var addressDogButton = new InlineKeyboardButton();
        addressDogButton.setText("Адрес, контакты и часы приёма");
        addressDogButton.setCallbackData(ADDRESS_CAT_BUTTON);

        var drivingDirectionsButton = new InlineKeyboardButton();
        drivingDirectionsButton.setText("Схема проезда");
        drivingDirectionsButton.setCallbackData(DRIVING_DIRECTIONS_CAT_BUTTON);

        var safetyPrecautionsButton = new InlineKeyboardButton();
        safetyPrecautionsButton.setText("Правила поведения на территории приюта");
        safetyPrecautionsButton.setCallbackData(SAFETY_PRECAUTIONS_CAT_BUTTON);

        var callVolunteerButton = new InlineKeyboardButton();
        callVolunteerButton.setText("Позвать волонтера");
        callVolunteerButton.setCallbackData(CALL_VOLUNTEER_BUTTON);

        List<InlineKeyboardButton> row1 = List.of(addressDogButton);
        List<InlineKeyboardButton> row2 = List.of(drivingDirectionsButton);
        List<InlineKeyboardButton> row3 = List.of(safetyPrecautionsButton);
        List<InlineKeyboardButton> row4 = List.of(callVolunteerButton);

        rowsInLine.add(row1);
        rowsInLine.add(row2);
        rowsInLine.add(row3);
        rowsInLine.add(row4);

        markup.setKeyboard(rowsInLine);
        message.setReplyMarkup(markup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Возникла ошибка: " + e.getMessage());
        }
    }

    private void howToAdoptDog(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Как взять собаку из приюта?");
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();

        var rulesDogButton = new InlineKeyboardButton();
        rulesDogButton.setText("Правила знакомства с животным до того, как можно забрать его из приюта.");
        rulesDogButton.setCallbackData(RULES_DOG_BUTTON);

        var docDogButton = new InlineKeyboardButton();
        docDogButton.setText("Список документов");
        docDogButton.setCallbackData(DOC_DOG_BUTTON);

        var transportationDogButton = new InlineKeyboardButton();
        transportationDogButton.setText("Рекомендации по транспортировке собаки.");
        transportationDogButton.setCallbackData(TRANSPORTATION_DOG_BUTTON);

        var recommendationsPuppyButton = new InlineKeyboardButton();
        recommendationsPuppyButton.setText("Как обустроить дом для щенка.");
        recommendationsPuppyButton.setCallbackData(RECOMMENDATION_PUPPY_BUTTON);

        var recommendationsDogButton = new InlineKeyboardButton();
        recommendationsDogButton.setText("Как обустроить дом для взрослой собаки.");
        recommendationsDogButton.setCallbackData(RECOMMENDATION_DOG_BUTTON);

        var recommendationsHandicappedDogButton = new InlineKeyboardButton();
        recommendationsHandicappedDogButton.setText("Собака с ограниченными возможностями.");
        recommendationsHandicappedDogButton.setCallbackData(REC_HANDICAPPED_DOG_BUTTON);

        var adviсesСynologistsButton = new InlineKeyboardButton();
        adviсesСynologistsButton.setText("Советы по первичному общению с собакой.");
        adviсesСynologistsButton.setCallbackData(ADVICES_CYNOLOGISTS_BUTTON);

        var cynologistsButton = new InlineKeyboardButton();
        cynologistsButton.setText("Проверенные Ветеринарные центры.");
        cynologistsButton.setCallbackData(CYNOLOGISTS_BUTTON);

        var reasonsForRefusalButton = new InlineKeyboardButton();
        reasonsForRefusalButton.setText("Список причин, почему могут отказать.");
        reasonsForRefusalButton.setCallbackData(REASONS_FOR_REFUSAL_BUTTON);

        var saveContactButton = new InlineKeyboardButton();
        saveContactButton.setText("Могу записать Ваши контактные данные.");
        saveContactButton.setCallbackData(SAVE_CONTACT_BUTTON);

        var callVolunteerButton = new InlineKeyboardButton();
        callVolunteerButton.setText("Позвать волонтера");
        callVolunteerButton.setCallbackData(CALL_VOLUNTEER_BUTTON);

        List<InlineKeyboardButton> row1 = List.of(rulesDogButton);
        List<InlineKeyboardButton> row2 = List.of(docDogButton);
        List<InlineKeyboardButton> row3 = List.of(transportationDogButton);
        List<InlineKeyboardButton> row4 = List.of(recommendationsPuppyButton);
        List<InlineKeyboardButton> row5 = List.of(recommendationsDogButton);
        List<InlineKeyboardButton> row6 = List.of(recommendationsHandicappedDogButton);
        List<InlineKeyboardButton> row7 = List.of(adviсesСynologistsButton);
        List<InlineKeyboardButton> row8 = List.of(cynologistsButton);
        List<InlineKeyboardButton> row9 = List.of(reasonsForRefusalButton);
        List<InlineKeyboardButton> row10 = List.of(saveContactButton);
        List<InlineKeyboardButton> row11 = List.of(callVolunteerButton);

        rowsInLine.add(row1);
        rowsInLine.add(row2);
        rowsInLine.add(row3);
        rowsInLine.add(row4);
        rowsInLine.add(row5);
        rowsInLine.add(row6);
        rowsInLine.add(row7);
        rowsInLine.add(row8);
        rowsInLine.add(row9);
        rowsInLine.add(row10);
        rowsInLine.add(row11);

        markup.setKeyboard(rowsInLine);
        message.setReplyMarkup(markup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Возникла ошибка: " + e.getMessage());
        }
    }

    private void howToAdoptCat(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Как взять кота из приюта?");
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();

        var rulesCatButton = new InlineKeyboardButton();
        rulesCatButton.setText("Правила знакомства с кошкой");
        rulesCatButton.setCallbackData(RULES_CAT_BUTTON);

        var docCatButton = new InlineKeyboardButton();
        docCatButton.setText("Список документов");
        docCatButton.setCallbackData(DOC_CAT_BUTTON);

        var transportationCatButton = new InlineKeyboardButton();
        transportationCatButton.setText("Рекомендации по транспортировке кота");
        transportationCatButton.setCallbackData(TRANSPORTATION_CAT_BUTTON);

        var recommendationsKittyButton = new InlineKeyboardButton();
        recommendationsKittyButton.setText("Как обустроить дом для котенка.");
        recommendationsKittyButton.setCallbackData(RECOMMENDATION_KITTY_BUTTON);

        var recommendationsCatButton = new InlineKeyboardButton();
        recommendationsCatButton.setText("Как обустроить дом для кошки.");
        recommendationsCatButton.setCallbackData(RECOMMENDATION_CAT_BUTTON);

        var recommendationsHandicappedCatButton = new InlineKeyboardButton();
        recommendationsHandicappedCatButton.setText("кошка с ограниченными возможностями..");
        recommendationsHandicappedCatButton.setCallbackData(REC_HANDICAPPED_CAT_BUTTON);

        var vetCenterButton = new InlineKeyboardButton();
        vetCenterButton.setText("Проверенные Ветеринарные центры.");
        vetCenterButton.setCallbackData(VET_CENTER_BUTTON);

        var reasonsForRefusalButton = new InlineKeyboardButton();
        reasonsForRefusalButton.setText("Список причин, почему могут отказать.");
        reasonsForRefusalButton.setCallbackData(REASONS_FOR_REFUSAL_BUTTON);

        var saveContactButton = new InlineKeyboardButton();
        saveContactButton.setText("Могу записать Ваши контактные данные.");
        saveContactButton.setCallbackData(SAVE_CONTACT_BUTTON);

        var callVolunteerButton = new InlineKeyboardButton();
        callVolunteerButton.setText("Позвать волонтера");
        callVolunteerButton.setCallbackData(CALL_VOLUNTEER_BUTTON);

        List<InlineKeyboardButton> row1 = List.of(rulesCatButton);
        List<InlineKeyboardButton> row2 = List.of(docCatButton);
        List<InlineKeyboardButton> row3 = List.of(transportationCatButton);
        List<InlineKeyboardButton> row4 = List.of(recommendationsKittyButton);
        List<InlineKeyboardButton> row5 = List.of(recommendationsCatButton);
        List<InlineKeyboardButton> row6 = List.of(recommendationsHandicappedCatButton);
        List<InlineKeyboardButton> row7 = List.of(vetCenterButton);
        List<InlineKeyboardButton> row8 = List.of(reasonsForRefusalButton);
        List<InlineKeyboardButton> row9 = List.of(saveContactButton);
        List<InlineKeyboardButton> row10 = List.of(callVolunteerButton);

        rowsInLine.add(row1);
        rowsInLine.add(row2);
        rowsInLine.add(row3);
        rowsInLine.add(row4);
        rowsInLine.add(row5);
        rowsInLine.add(row6);
        rowsInLine.add(row7);
        rowsInLine.add(row8);
        rowsInLine.add(row9);
        rowsInLine.add(row10);

        markup.setKeyboard(rowsInLine);
        message.setReplyMarkup(markup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Возникла ошибка: " + e.getMessage());
        }
    }

    /**
     * Метод, который регистрирует пользователя, заинтересовавшегося приютом для кошек.
     * После нажатия кнопки "Кошки" в меню бота, данные пользователя автоматически сохраняются в таблице UserCat.
     *
     * @param msg в качестве параметра используется входящее сообщение, из которого берутся данные пользователя
     */
    private void registerUserCat(Message msg) {
//        по чат-айди проверяем, что такого пользователя в таблице ещё нет
        if (userCatRepository.findById(msg.getChatId()).isEmpty()) {
            var chatId = msg.getChatId();
            var chat = msg.getChat();
//            создаем нового пользователя и присваиваем ему данные пользователя, которого регистрируем
            UserCat userCat = new UserCat();
            userCat.setChatId(chatId);
            userCat.setFirstName(chat.getFirstName());
            userCat.setLastName(chat.getLastName());
            userCat.setRegisteredAt(LocalDateTime.now());
//            по умолчанию каждый пользователь заносится в таблицу, как просто обычный пользователь,
//            статус, в дальнейшем, может поменять волонтёр вручную
            userCat.setUserStatus(JUST_USER);
//            сохраняем пользователя в таблицу
            userCatRepository.save(userCat);
            log.info("Сохранен пользователь: " + userCat);
        }
    }

    /**
     * Метод, который регистрирует пользователя, заинтересовавшегося приютом для собак.
     * После нажатия кнопки "Собаки" в меню бота, данные пользователя автоматически сохраняются в таблице UserDog.
     *
     * @param msg в качестве параметра используется входящее сообщение, из которого берутся данные пользователя
     */
    private void registerUserDog(Message msg) {
//        по чат-айди проверяем, что такого пользователя в таблице ещё нет
        if (userDogRepository.findById(msg.getChatId()).isEmpty()) {
            long chatId = msg.getChatId();
            Chat chat = msg.getChat();
//            создаем нового пользователя и присваиваем ему данные пользователя, которого регистрируем
            UserDog userDog = new UserDog();
            userDog.setChatId(chatId);
            userDog.setFirstName(chat.getFirstName());
            userDog.setLastName(chat.getLastName());
            userDog.setRegisteredAt(LocalDateTime.now());
//            по умолчанию каждый пользователь заносится в таблицу, как просто обычный пользователь,
//            статус, в дальнейшем, может поменять волонтёр вручную
            userDog.setUserStatus(JUST_USER);
//            сохраняем пользователя в таблицу
            userDogRepository.save(userDog);
            log.info("Сохранен пользователь: " + userDog);
        }
    }

    private void startCommandReceived(long chatId, String name) {
        String answer = EmojiParser.parseToUnicode("Привет, " + name + "! Это чат-бот приюта животных из Астаны,\n" +
                " который хочет помочь людям, которые задумываются о том," +
                " чтобы забрать собаку или кошку домой." + ":dog: " + " :cat:");
        log.info("Replied to user " + name);
        sendMessage(chatId, answer);
    }

    public void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage(String.valueOf(chatId), textToSend);
        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Возникла ошибка: " + e.getMessage());
        }
    }

    /**
     * * метод, который вызывается при выполнении команды /call_volunteer
     *
     * @param chatId в качестве первого параметра используется chatId пользователя
     * @param name   второй параметр - имя пользователя, который вызывает волонтера
     */
    private void sendMsgToVolunteer(long chatId, String name) {
//        сообщаем пользователю, что с ним сейчас свяжется волонтёр
        sendMessage(chatId, "С Вами свяжется волонтер");
//        генерируем рандомный чат-айди одного из волонтёров
        long randomChatId = generateRandomChatId();
//        прописываем сообщение, которое будет отправлено волонтёру
        String answer = "Требуется консультация волонтёра для пользователя по имени "
                + name + ". Чат-айди пользователя - " + chatId;
//        отправляем сообщение рандомно-выбранному волонтёру
        sendMessage(randomChatId, answer);
    }

    /**
     * метод, который генерирует рандомный чат-айди одного из волонтёров
     */
    public long generateRandomChatId() {
        List<Long> chatIdList = List.of(956120008L, 198498708L, 921797425L, 1911144874L, 5242769604L);
        int randValue = (int) (Math.random() * chatIdList.size());
        return chatIdList.get(randValue);
    }

    /**
     * метод создания кнопки "Отправить свои контакты", которая запрашивает у пользователя его контактные данные
     *
     * @param chatId
     */
    public void createSaveContactButtons(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        KeyboardRow keyboardRow1 = new KeyboardRow();
        KeyboardButton contactDog = new KeyboardButton("Button1");
        contactDog.setText("Отправить свои контакты");
        contactDog.setRequestContact(true);
        keyboardRow1.add(contactDog);

        List<KeyboardRow> keyboard = new ArrayList<>();
        keyboard.add(keyboardRow1);

        replyKeyboardMarkup.setKeyboard(keyboard);

        SendMessage sndMsg = new SendMessage();
        sndMsg.setChatId(String.valueOf(chatId));
        sndMsg.setText("Чтобы Я сохранил ваш номер телефона, нажмите на кнопку \"Отправить свои контакты\"");
        sndMsg.setReplyMarkup(replyKeyboardMarkup);

        try {
            execute(sndMsg);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }

    /**
     * метод, который сохраняет в базу данных приюта собак номер телефона пользователя
     *
     * @param message в качестве параметра входящее сообщение
     */
    public void saveContactDog(Message message) {
        UserDog userDog1 = userDogRepository.findUserDogByChatId(message.getChatId());
        if (userDog1 != null) {
            userDog1.setUserName(message.getFrom().getUserName());
            userDog1.setPhoneNumber(message.getContact().getPhoneNumber());
            userDogRepository.save(userDog1);
        }
    }

    /**
     * метод, который сохраняет в базу данных приюта кошек номер телефона пользователя
     *
     * @param message в качестве параметра входящее сообщение
     */
    public void saveContactCat(Message message) {
        User thisUser = message.getFrom();
        UserCat userCat1 = userCatRepository.findUserCatByChatId(thisUser.getId());
        if (userCat1 != null) {
            userCat1.setUserName(thisUser.getUserName());
            userCat1.setPhoneNumber(message.getContact().getPhoneNumber());
            userCatRepository.save(userCat1);
        }
    }

    /**
     * метод сохранения отчёта о питомце
     * @param update первый параметр это сам апдейт
     * @param caption второй параметр - описание к изображению, которое содержится в сообщении
     */
    public void getReport(Update update, String caption) {
        Matcher matcherDog = patternDog.matcher(caption);
        Matcher matcherCat = patternCat.matcher(caption);
        long chatId = update.getMessage().getChatId();
        String photoId = update.getMessage().getPhoto().get(1).getFileId();
        UserDog userDog = userDogRepository.findUserDogByChatId(chatId);
        UserCat userCat = userCatRepository.findUserCatByChatId(chatId);

        if (matcherDog.find()) {
            String ration = matcherDog.group(3);
            String health = matcherDog.group(7);
            String habits = matcherDog.group(11);
            String infoMessage = ration + "\n" + "\n" + habits + "\n" + health;
            ReportDog reportDog = new ReportDog();
            reportDog.setPhotoId(photoId);
            reportDog.setInfoMessage(infoMessage);
            reportDog.setUserDog(userDog);
            reportDogRepository.save(reportDog);
            sendMessage(chatId,
                    "Ваш отчёт для приюта собак принят! " +
                            "Не забывайте отправлять отчёт каждый день в течение испытательного срока (30 дней)");
        } else if (matcherCat.find()) {
            String ration = matcherCat.group(3);
            String health = matcherCat.group(7);
            String habits = matcherCat.group(11);
            String infoMessage = ration + "\n" + "\n" + habits + "\n" + health;
            ReportCat reportCat = new ReportCat();
            reportCat.setPhotoId(photoId);
            reportCat.setInfoMessage(infoMessage);
            reportCat.setUserCat(userCat);
            reportCatRepository.save(reportCat);
            sendMessage(chatId,
                    "Ваш отчёт для приюта кошек принят! " +
                            "Не забывайте отправлять отчёт каждый день в течение испытательного срока (30 дней)");
        }
    }

}



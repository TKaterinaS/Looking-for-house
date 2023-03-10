package ru.team2.lookingforhouse.service;

import com.vdurmont.emoji.EmojiParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.team2.lookingforhouse.config.BotConfig;
import ru.team2.lookingforhouse.model.User;
import ru.team2.lookingforhouse.model.UserRepository;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TelegramBot extends TelegramLongPollingBot {

    @Autowired
    private UserRepository userRepository;
    final BotConfig config;

    public TelegramBot(BotConfig config) {
        this.config = config;
        /** Создание кнопки меню
         * (все команды должны быть написаны в нижнем регистре)*/
        List<BotCommand> listOfCommands = new ArrayList<>();
        listOfCommands.add(new BotCommand("/start", "Приветствует пользователя"));
        listOfCommands.add(new BotCommand("/info", "Выводит информацию о приюте"));
        listOfCommands.add(new BotCommand("/to_adopt", "Выводит информацию о том, как взять питомца из приюта"));
        listOfCommands.add(new BotCommand("/submit_report", "Выводит информацию о том, как прислать отчет о питомце"));
        listOfCommands.add(new BotCommand("/call_volunteer", "Вызвать волотера"));
        try {
            this.execute(new SetMyCommands(listOfCommands, new BotCommandScopeDefault(), null));
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
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
                /**Приветствие пользователя. Регистрация в БД нового пользователя*/
                case "/start":
                    registerUser(update.getMessage());
                    start(chatId, userName);
                    break;
                /**Вывод общей информации и кнопок под сообщением*/
                case "/info":
                    info(chatId);
                    break;
                /**выполнение команды /call_volunteer (вызов волонтера)*/
                case "/call_volunteer":
                    sendMsgToVolunteer(chatId, userName);
                    sendMessage(chatId, "С Вами свяжется волонтер");
                    break;
                /**дефолтное сообщение, если бот получит неизвестную ему команду*/
                default:
                    sendMessage(chatId, "Нераспознанная команда, попробуйте ещё раз");
            }
        }
        else if (update.hasCallbackQuery()){
            String callbackData = update.getCallbackQuery().getData();
            long messageId = update.getCallbackQuery().getMessage().getMessageId();
            long chatId = update.getCallbackQuery().getMessage().getChatId();
            String userName = update.getCallbackQuery().getMessage().getChat().getFirstName();

            if(callbackData.equals("ADDRESS_BUTTON")){
                sendMessage(chatId, "ЖК Инфинити, Кабанбай батыра, 40, Астана 010000, Казахстан\n" +
                        "Телефон: +7 707 818 0770\n" +
                        "Часы работы: 9:00 - 21:00\n");
            } else if (callbackData.equals("DRIVING_DIRECTIONS_BUTTON")) {
                sendMessage(chatId, "https://www.google.com/maps/dir//%D0%96%D0%9A+%D0%98%D0%BD%D1%84%D0%B8%D0%BD%D0%B8%D1%82%D0%B8,+%D0%9A%D0%B0%D0%B1%D0%B0%D0%BD%D0%B1%D0%B0%D0%B9+%D0%B1%D0%B0%D1%82%D1%8B%D1%80%D0%B0,+40,+%D0%90%D1%81%D1%82%D0%B0%D0%BD%D0%B0+010000,+%D0%9A%D0%B0%D0%B7%D0%B0%D1%85%D1%81%D1%82%D0%B0%D0%BD/@51.1206701,71.3445223,12z/data=!4m8!4m7!1m0!1m5!1m1!1s0x4245850dcda03545:0x7016f889b087939e!2m2!1d71.4145953!2d51.12077");
            } else if (callbackData.equals("SAFETY_PRECAUTIONS_BUTTON")) {
                sendMessage(chatId, "Правила ТБ");
            } else if (callbackData.equals("CALL_VOLUNTEER_BUTTON")) {
                sendMsgToVolunteer(chatId, userName);
                sendMessage(chatId, "С Вами свяжется волонтер");
            } else if(callbackData.equals("INFO_BUTTON")) {
                info(chatId);
            } else if(callbackData.equals("TO_ADOPT_BUTTON")) {
                sendMessage(chatId, "Скоро здесь будет информация");
            } else if (callbackData.equals("SUBMIT_REPORT_BUTTON")) {
                sendMessage(chatId, "Скоро здесь будет информация");
            }
        }
    }

    /**Метод выводит общую информацию и кнопки в сообщении*/
    private void info(long chatId) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText("Общая информация о приюте");
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();

        var addressButton = new InlineKeyboardButton();
        addressButton.setText("Адрес, контакты и часы приёма");
        addressButton.setCallbackData("ADDRESS_BUTTON");

        var drivingDirectionsButton = new InlineKeyboardButton();
        drivingDirectionsButton.setText("Схема проезда");
        drivingDirectionsButton.setCallbackData("DRIVING_DIRECTIONS_BUTTON");

        var safetyPrecautionsButton = new InlineKeyboardButton();
        safetyPrecautionsButton.setText("ТБ на территории приюта");
        safetyPrecautionsButton.setCallbackData("SAFETY_PRECAUTIONS_BUTTON");

        var callVolunteerButton = new InlineKeyboardButton();
        callVolunteerButton.setText("Позвать волотнтера");
        callVolunteerButton.setCallbackData("CALL_VOLUNTEER_BUTTON");

        List<InlineKeyboardButton> row1 = List.of(addressButton, drivingDirectionsButton);
        List<InlineKeyboardButton> row2 = List.of(safetyPrecautionsButton, callVolunteerButton);

        rowsInLine.add(row1);
        rowsInLine.add(row2);

        markup.setKeyboard(rowsInLine);
        message.setReplyMarkup(markup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Возникла ошибка: " + e.getMessage());
        }
        }

    private void registerUser(Message msg) {
        if (userRepository.findById(msg.getChatId()).isEmpty()) {
            var chatId = msg.getChatId();
            var chat = msg.getChat();
            User user = new User();
            user.setId(chatId);
            user.setFirstName(chat.getFirstName());
            user.setLastName(chat.getLastName());
            user.setUserName(chat.getUserName());
            user.setRegisteredAt(new Timestamp(System.currentTimeMillis()));
            userRepository.save(user);
            log.info("Сохранен пользователь: " + user);
        }
    }
//Не используется
    /*private void startCommandReceived(long chatId, String name) {
        String answer = EmojiParser.parseToUnicode("Привет, " + name + "! Это чат-бот приюта животных из Астаны,\n" +
                " который хочет помочь людям, которые задумываются о том," +
                " чтобы забрать собаку или кошку домой." + ":dog: " + " :cat:");
        log.info("Replied to user " + name);
        sendMessage(chatId,answer);
    }*/

    private void start(long chatId, String name){
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        String answer = EmojiParser.parseToUnicode("Привет, " + name + "! Это чат-бот приюта животных из Астаны,\n" +
                " который хочет помочь людям, которые задумываются о том," +
                " чтобы забрать собаку или кошку домой." + ":dog: " + " :cat:");
        message.setText(answer);

        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> rowsInLine = new ArrayList<>();

        var infoButton = new InlineKeyboardButton();
        infoButton.setText("Узнать информацию о приюте");
        infoButton.setCallbackData("INFO_BUTTON");

        var toAdoptButton = new InlineKeyboardButton();
        toAdoptButton.setText("Как взять животное из приюта?");
        toAdoptButton.setCallbackData("TO_ADOPT_BUTTON");

        var submitReportButton = new InlineKeyboardButton();
        submitReportButton.setText("Прислать отчет о питомце");
        submitReportButton.setCallbackData("SUBMIT_REPORT_BUTTON");

        var callVolunteerButton = new InlineKeyboardButton();
        callVolunteerButton.setText("Позвать волотнтера");
        callVolunteerButton.setCallbackData("CALL_VOLUNTEER_BUTTON");

        List<InlineKeyboardButton> row1 = List.of(infoButton, toAdoptButton);
        List<InlineKeyboardButton> row2 = List.of(submitReportButton, callVolunteerButton);

        rowsInLine.add(row1);
        rowsInLine.add(row2);

        markup.setKeyboard(rowsInLine);
        message.setReplyMarkup(markup);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Возникла ошибка: " + e.getMessage());
        }

    }



    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage(String.valueOf(chatId), textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Возникла ошибка: " + e.getMessage());
        }
    }

  /**  метод, который вызывается при выполнении команды /call_volunteer */
    private void sendMsgToVolunteer(long chatId, String name) {
//        генерируем рандомный чат-айди одного из волонтёров
        long randomChatId = generateRandomChatId();
//        прописываем сообщение, которое будет отправлено волонтёру
        String answer = "Требуется консультация волонтёра для пользователя по имени "
        + name + ". Чат-айди пользователя - " + chatId;
//        отправляем сообщение рандомно-выбранному волонтёру
        sendMessage(randomChatId, answer);
    }
/** метод, который генерирует рандомный чат-айди одного из волонтёров */
    private long generateRandomChatId() {
        List<Long> chatIdList = List.of(956120008L, 198498708L, 921797425L, 1911144874L, 1837692225L, 5242769604L);
        int randValue = (int) (Math.random() * chatIdList.size());
        return chatIdList.get(randValue);
    }

}


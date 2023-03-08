package ru.team2.lookingforhouse.service;

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
            String chatId = update.getMessage().getChatId().toString();

            switch (messageText) {
                case "/start":
                    registerUser(update.getMessage());
                    startCommandReceived(chatId, update.getMessage().getChat().getFirstName());
                    break;
//                    выполнение команды /call_volunteer
                case "/call_volunteer":
                    sendMsgToVolunteer(chatId, userName);
//                    дефолтное сообщение, если бот получит неизвестную ему команду
                default:
                    sendMessage(chatId, "Нераспознанная команда, попробуйте ещё раз");
            }
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
            log.info("user saved: " + user);
        }
    }

    private void startCommandReceived(String chatId, String name) {
        String answer = "HI, " + name + ",nice to meet you!";
        log.info("Replied to user " + name);
        sendMessage(chatId, answer);

    }

    private void sendMessage(String chatId, String textToSend) {
        SendMessage message = new SendMessage(chatId, textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: " + e.getMessage());
        }
    }
//    метод, который вызывается при выполнении команды /call_volunteer
    private void sendMsgToVolunteer(String chatId, String name) {
        String answer = "Привет, сейчас ты в роли волонтёра. Тебя вызвал по кнопке \"Вызвать волонтёра\" пользователь по имени "
        + name + ", с айди " + chatId;
//        волонтёром пока работает у нас Евгений
        sendMessage("198498708", answer);
    }
}


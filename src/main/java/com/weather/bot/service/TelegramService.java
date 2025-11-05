package com.weather.bot.service;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import com.weather.bot.controller.TelegramBotController;
import java.util.HashSet;
import java.util.Set;

@Service
public class TelegramService {
    private TelegramBotController botController;
    private final Set<Long> subscribers;


    public TelegramService() {
        this.subscribers = new HashSet<>();
    }


    public void setBotController(TelegramBotController botController) {
        this.botController = botController;
    }

    public void sendWeatherUpdate(Long chatId, String message) throws TelegramApiException {
        if (botController == null) {
            System.err.println("Bot controller not initialized yet");
            return;
        }
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId.toString());
        sendMessage.setText(message);
        sendMessage.setParseMode(ParseMode.MARKDOWN);
        botController.execute(sendMessage);
    }

    public void sendToAllSubscribers(String message) {
        if (subscribers.isEmpty()) {
            System.out.println("No subscribers to notify");
            return;
        }

        for (Long chatId : subscribers) {
            try {
                sendWeatherUpdate(chatId, message);
                System.out.println("Weather update sent to: " + chatId);
            } catch (TelegramApiException e) {
                System.err.println("Failed to send message to " + chatId + ": " + e.getMessage());
            }
        }
    }

    public void sendSystemMessage(Long chatId, String message) {
        try {
            SendMessage sendMessage = new SendMessage();
            sendMessage.setChatId(chatId.toString());
            sendMessage.setText("⚙️ " + message);
            if (botController != null) {
                botController.execute(sendMessage);
            }
        } catch (TelegramApiException e) {
            System.err.println("Failed to send system message: " + e.getMessage());
        }
    }

    public void addSubscriber(Long chatId) {
        subscribers.add(chatId);
        System.out.println("Added Telegram subscriber: " + chatId);
        sendSystemMessage(chatId, "You have been subscribed to weather updates! 🌤️");
    }

    public void removeSubscriber(Long chatId) {
        subscribers.remove(chatId);
        System.out.println("Removed Telegram subscriber: " + chatId);
        sendSystemMessage(chatId, "You have been unsubscribed from weather updates.");
    }

    public Set<Long> getSubscribers() {
        return new HashSet<>(subscribers);
    }

    public int getSubscriberCount() {
        return subscribers.size();
    }
}
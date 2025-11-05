package com.weather.bot.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
import com.weather.bot.service.WeatherService;
import com.weather.bot.service.TelegramService;
import com.weather.bot.strategies.ManualUpdateStrategy;

import jakarta.annotation.PostConstruct;

@Component
public class TelegramBotController extends TelegramLongPollingBot {

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.name}")
    private String botName;

    private final WeatherService weatherService;
    private final ManualUpdateStrategy manualStrategy;
    private final TelegramService telegramService;

    public TelegramBotController(WeatherService weatherService,
                                 ManualUpdateStrategy manualStrategy,
                                 TelegramService telegramService) {
        this.weatherService = weatherService;
        this.manualStrategy = manualStrategy;
        this.telegramService = telegramService;
    }

    @PostConstruct
    public void init() {
        telegramService.setBotController(this);

        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(this);
            System.out.println("Telegram Bot Registered Successfully: " + botName);
        } catch (TelegramApiException e) {
            System.err.println("Failed to register bot: " + e.getMessage());
        }
    }

    @Override
    public String getBotUsername() {
        return botName;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            String messageText = update.getMessage().getText();
            Long chatId = update.getMessage().getChatId();

            processMessage(chatId, messageText);
        }
    }

    private void processMessage(Long chatId, String message) {
        SendMessage response = new SendMessage();
        response.setChatId(chatId.toString());

        try {

            String trimmedMessage = message.trim().toLowerCase();

            switch (trimmedMessage) {
                case "/start":
                    response.setText("🌤️ *Welcome to Weather Bot!* 🌤️\n\n" +
                            "Available Commands:\n" +
                            "• /current - Get current weather\n" +
                            "• /strategy realtime - Real-time updates\n" +
                            "• /strategy scheduled - Scheduled updates\n" +
                            "• /strategy manual - Manual updates\n" +
                            "• /strategy cached - Cached real-time updates\n" +
                            "• /station urban - Create urban station\n" +
                            "• /station coastal - Create coastal station\n" +
                            "• /manual temp hum press - Set manual weather\n" +
                            "• /cache clear - Clear weather cache\n" +
                            "• /cache status - Show cache status\n" +
                            "• /subscribe - Subscribe to updates\n" +
                            "• /unsubscribe - Unsubscribe\n" +
                            "• /status - System status\n\n" +
                            "Use /subscribe to start receiving weather updates!");
                    break;

                case "/current":
                    weatherService.performUpdate();
                    response.setText("🔄 Fetching current weather data...");
                    break;

                case "/subscribe":
                    weatherService.subscribeTelegram(chatId);
                    response.setText("✅ Subscribed to weather updates! You'll receive regular weather updates.");
                    break;

                case "/unsubscribe":
                    weatherService.unsubscribeTelegram(chatId);
                    response.setText("❌ Unsubscribed from weather updates");
                    break;

                case "/status":
                    response.setText("📊 *System Status:*\n" +
                            "• Strategy: " + weatherService.getCurrentStrategy() + "\n" +
                            "• Subscribers: " + telegramService.getSubscriberCount());
                case "/strategy cached":
                    weatherService.setStrategy("cached");
                    response.setText("🔄 Switched to *Cached Real-time* strategy");
                    break;

                case "/station urban":
                    weatherService.setStrategy("urban");
                    response.setText("🏙️ Created *Urban* Weather Station");
                    break;

                case "/station coastal":
                    weatherService.setStrategy("coastal");
                    response.setText("🏖️ Created *Coastal* Weather Station");
                    break;

                case "/cache clear":
                    weatherService.clearCache();
                    response.setText("🗑️ Weather cache cleared!");
                    break;

                case "/cache status":
                    weatherService.showCacheStatus();
                    response.setText("📊 Fetching cache status...");
                    break;

                default:
                    // Handle multi-word commands
                    if (trimmedMessage.startsWith("/strategy ")) {
                        handleStrategyCommand(trimmedMessage, response);
                    } else if (trimmedMessage.startsWith("/manual ")) {
                        handleManualCommand(trimmedMessage, response);
                    } else if (trimmedMessage.startsWith("/strategy_")) {
                        handleUnderscoreStrategyCommand(trimmedMessage, response);
                    } else {
                        response.setText("❓ Unknown command. Use /start to see available commands.");
                    }
                    break;
            }

            execute(response);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void handleStrategyCommand(String message, SendMessage response) {
        String[] parts = message.split(" ");
        if (parts.length == 2) {
            String strategy = parts[1].toLowerCase();
            switch (strategy) {
                case "realtime":
                    weatherService.setStrategy("realtime");
                    response.setText("🔄 Switched to *Real-time* strategy");
                    break;
                case "scheduled":
                    weatherService.setStrategy("scheduled");
                    response.setText("🔄 Switched to *Scheduled* strategy");
                    break;
                case "manual":
                    weatherService.setStrategy("manual");
                    response.setText("🔄 Switched to *Manual* strategy");
                    break;
                default:
                    response.setText("❌ Unknown strategy. Use: realtime, scheduled, or manual");
                    break;
            }
        } else {
            response.setText("❌ Invalid format. Use: /strategy realtime  OR  /strategy_scheduled");
        }
    }

    private void handleUnderscoreStrategyCommand(String message, SendMessage response) {
        if (message.equals("/strategy_realtime")) {
            weatherService.setStrategy("realtime");
            response.setText("🔄 Switched to *Real-time* strategy");
        } else if (message.equals("/strategy_scheduled")) {
            weatherService.setStrategy("scheduled");
            response.setText("🔄 Switched to *Scheduled* strategy");
        } else if (message.equals("/strategy_manual")) {
            weatherService.setStrategy("manual");
            response.setText("🔄 Switched to *Manual* strategy");
        } else {
            response.setText("❌ Unknown strategy command. Use: /strategy_realtime, /strategy_scheduled, or /strategy_manual");
        }
    }

    private void handleManualCommand(String message, SendMessage response) {
        String[] parts = message.split(" ");
        if (parts.length == 4) {
            try {
                double temp = Double.parseDouble(parts[1]);
                double hum = Double.parseDouble(parts[2]);
                double press = Double.parseDouble(parts[3]);
                manualStrategy.setManualData(temp, hum, press);
                response.setText(" Manual data set:\n" +
                        "• Temperature: " + temp + "°C\n" +
                        "• Humidity: " + hum + "%\n" +
                        "• Pressure: " + press + " hPa\n\n" +
                        "Use /current to see this data!");
            } catch (NumberFormatException e) {
                response.setText(" Invalid numbers. Use: /manual temperature humidity pressure\nExample: /manual 25 60 1015");
            }
        } else {
            response.setText(" Invalid format. Use: /manual temperature humidity pressure\nExample: /manual 25 60 1015");
        }
    }
}
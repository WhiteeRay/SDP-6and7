package com.weather.bot.service;

import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import com.weather.bot.core.WeatherStation;
import com.weather.bot.observers.TelegramBotObserver;
import com.weather.bot.strategies.*;

@Service
public class WeatherService {
    private final WeatherStation weatherStation;
    private final RealTimeUpdateStrategy realTimeStrategy;
    private final ScheduledUpdateStrategy scheduledStrategy;
    private final ManualUpdateStrategy manualStrategy;
    private final TelegramBotObserver telegramObserver;
    private final TelegramService telegramService;

    public WeatherService(RealTimeUpdateStrategy realTimeStrategy,
                          ScheduledUpdateStrategy scheduledStrategy,
                          ManualUpdateStrategy manualStrategy,
                          TelegramBotObserver telegramObserver,
                          TelegramService telegramService) {
        this.realTimeStrategy = realTimeStrategy;
        this.scheduledStrategy = scheduledStrategy;
        this.manualStrategy = manualStrategy;
        this.telegramObserver = telegramObserver;
        this.telegramService = telegramService;

        this.weatherStation = new WeatherStation(realTimeStrategy);
    }

    @PostConstruct
    public void initialize() {
        registerAllObservers();
    }

    private void registerAllObservers() {
        weatherStation.registerObserver(telegramObserver);
        System.out.println("Telegram observer registered: " + telegramObserver.getObserverName());
        System.out.println("Total observers: " + weatherStation.getObserverCount());
    }

    public void performUpdate() {
        weatherStation.performUpdate();
    }

    public void setStrategy(String strategyType) {
        switch (strategyType.toLowerCase()) {
            case "realtime":
                weatherStation.setUpdateStrategy(realTimeStrategy);
                break;
            case "scheduled":
                weatherStation.setUpdateStrategy(scheduledStrategy);
                break;
            case "manual":
                weatherStation.setUpdateStrategy(manualStrategy);
                break;
        }
    }

    public void subscribeTelegram(Long chatId) {
        telegramService.addSubscriber(chatId);
    }

    public void unsubscribeTelegram(Long chatId) {
        telegramService.removeSubscriber(chatId);
    }

    public String getCurrentStrategy() {
        return weatherStation.getCurrentStrategy();
    }

    public int getObserverCount() {
        return weatherStation.getObserverCount();
    }
}
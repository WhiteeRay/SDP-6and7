package com.weather.bot.service;

import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;
import com.weather.bot.core.WeatherStation;
import com.weather.bot.observers.TelegramBotObserver;
import com.weather.bot.strategies.*;
import com.weather.bot.factory.WeatherStationFactory;
import com.weather.bot.singleton.WeatherDataCache;

@Service
public class WeatherService {
    private WeatherStation weatherStation;
    private final RealTimeUpdateStrategy realTimeStrategy;
    private final ScheduledUpdateStrategy scheduledStrategy;
    private final ManualUpdateStrategy manualStrategy;
    private final CachedRealTimeUpdateStrategy cachedRealTimeStrategy;
    private final TelegramBotObserver telegramObserver;
    private final TelegramService telegramService;
    private final WeatherStationFactory weatherStationFactory;

    public WeatherService(RealTimeUpdateStrategy realTimeStrategy,
                          ScheduledUpdateStrategy scheduledStrategy,
                          ManualUpdateStrategy manualStrategy,
                          CachedRealTimeUpdateStrategy cachedRealTimeStrategy,
                          TelegramBotObserver telegramObserver,
                          TelegramService telegramService,
                          WeatherStationFactory weatherStationFactory) {
        this.realTimeStrategy = realTimeStrategy;
        this.scheduledStrategy = scheduledStrategy;
        this.manualStrategy = manualStrategy;
        this.cachedRealTimeStrategy = cachedRealTimeStrategy;
        this.telegramObserver = telegramObserver;
        this.telegramService = telegramService;
        this.weatherStationFactory = weatherStationFactory;

        this.weatherStation = weatherStationFactory.createDefaultWeatherStation(realTimeStrategy);
    }

    @PostConstruct
    public void initialize() {
        registerAllObservers();
        initializeCache();
    }

    private void registerAllObservers() {
        weatherStation.registerObserver(telegramObserver);
        System.out.println("Telegram observer registered: " + telegramObserver.getObserverName());
        System.out.println("Total observers: " + weatherStation.getObserverCount());
    }

    private void initializeCache() {
        WeatherDataCache cache = WeatherDataCache.getInstance();
        System.out.println("Weather Data Cache initialized. Size: " + cache.getCacheSize());
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
            case "cached":
                weatherStation.setUpdateStrategy(cachedRealTimeStrategy);
                break;
            case "urban":
                this.weatherStation = weatherStationFactory.createStationWithType("urban", realTimeStrategy);
                registerAllObservers();
                break;
            case "coastal":
                this.weatherStation = weatherStationFactory.createStationWithType("coastal", realTimeStrategy);
                registerAllObservers();
                break;
        }
    }

    public void subscribeTelegram(Long chatId) {
        telegramService.addSubscriber(chatId);
    }

    public void unsubscribeTelegram(Long chatId) {
        telegramService.removeSubscriber(chatId);
    }

    public void clearCache() {
        WeatherDataCache cache = WeatherDataCache.getInstance();
        cache.clearAllCache();
        telegramService.sendToAllSubscribers("🗑️ Weather cache cleared!");
    }

    public void showCacheStatus() {
        WeatherDataCache cache = WeatherDataCache.getInstance();
        String status = "📊 *Cache Status:*\n" +
                "• Cached Locations: " + cache.getCachedLocations().size() + "\n" +
                "• Total Cache Size: " + cache.getCacheSize() + "\n" +
                "• Locations: " + String.join(", ", cache.getCachedLocations());

        telegramService.sendToAllSubscribers(status);
    }

    public String getCurrentStrategy() {
        return weatherStation.getCurrentStrategy();
    }

    public int getObserverCount() {
        return weatherStation.getObserverCount();
    }
}
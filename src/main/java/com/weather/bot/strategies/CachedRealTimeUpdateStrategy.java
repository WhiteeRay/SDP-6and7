package com.weather.bot.strategies;

import com.weather.bot.core.WeatherData;
import com.weather.bot.singleton.WeatherDataCache;
import org.springframework.stereotype.Component;

@Component
public class CachedRealTimeUpdateStrategy implements UpdateStrategy {
    private final String LOCATION = "default";

    @Override
    public WeatherData fetchWeatherData() {
        WeatherDataCache cache = WeatherDataCache.getInstance();
        WeatherData cachedData = cache.getLatestCachedData(LOCATION);

        if (cachedData != null && isCacheValid(cachedData)) {
            System.out.println("Using cached real-time data");
            return cachedData;
        }

        System.out.println("Generating new real-time data");
        double temperature = 20 + (Math.random() * 10);
        double humidity = 40 + (Math.random() * 30);
        double pressure = 1013 + (Math.random() * 10);

        WeatherData newData = new WeatherData(temperature, humidity, pressure);
        cache.cacheWeatherData(LOCATION, newData);

        return newData;
    }

    private boolean isCacheValid(WeatherData data) {
        return data.getTimestamp().isAfter(
                java.time.LocalDateTime.now().minusMinutes(1)
        );
    }

    @Override
    public String getStrategyName() {
        return "Cached Real-Time Strategy";
    }
}
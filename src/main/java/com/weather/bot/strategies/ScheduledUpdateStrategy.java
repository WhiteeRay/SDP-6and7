package com.weather.bot.strategies;

import org.springframework.stereotype.Component;
import com.weather.bot.core.WeatherData;

@Component
public class ScheduledUpdateStrategy implements UpdateStrategy {
    private int updateCount = 0;
    private final WeatherData[] cachedData = {
            new WeatherData(22.5, 65.2, 1015.8),
            new WeatherData(23.1, 62.8, 1016.2),
            new WeatherData(21.8, 68.5, 1014.9),
            new WeatherData(24.2, 58.3, 1017.1)
    };

    @Override
    public WeatherData fetchWeatherData() {
        System.out.println("Fetching scheduled batch update...");
        WeatherData data = cachedData[updateCount % cachedData.length];
        updateCount++;

        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return data;
    }

    @Override
    public String getStrategyName() {
        return "Scheduled Batch Updates";
    }
}
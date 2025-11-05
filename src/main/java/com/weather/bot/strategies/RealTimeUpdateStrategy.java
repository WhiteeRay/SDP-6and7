package com.weather.bot.strategies;

import org.springframework.stereotype.Component;
import com.weather.bot.core.WeatherData;

@Component
public class RealTimeUpdateStrategy implements UpdateStrategy {
    @Override
    public WeatherData fetchWeatherData() {
        // Remove the System.out.println for cleaner output
        double temperature = 20 + (Math.random() * 10);
        double humidity = 40 + (Math.random() * 30);
        double pressure = 1013 + (Math.random() * 10);

        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        return new WeatherData(temperature, humidity, pressure);
    }

    @Override
    public String getStrategyName() {
        return "Real-Time Sensor Polling";
    }
}
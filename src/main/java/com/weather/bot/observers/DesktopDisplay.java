package com.weather.bot.observers;

import org.springframework.stereotype.Component;
import com.weather.bot.core.WeatherData;

@Component
public class DesktopDisplay implements WeatherObserver {
    private final String location;

    public DesktopDisplay() {
        this.location = "Living Room";
    }

    @Override
    public void update(WeatherData weatherData) {
        System.out.println("Desktop Display - " + location + ":");
        System.out.println("   ---------------------");
        System.out.printf("   | %6.1fC           |\n", weatherData.getTemperature());
        System.out.printf("   | %6.1f%% Humidity  |\n", weatherData.getHumidity());
        System.out.printf("   | %6.1f hPa        |\n", weatherData.getPressure());
        System.out.println("   ---------------------");
        System.out.println();
    }

    @Override
    public String getObserverName() {
        return location + " Desktop Display";
    }
}
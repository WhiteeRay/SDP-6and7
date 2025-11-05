package com.weather.bot.observers;

import org.springframework.stereotype.Component;
import com.weather.bot.core.WeatherData;

@Component
public class MobileApp implements WeatherObserver {
    private final String appName;

    public MobileApp() {
        this.appName = "WeatherPro";
    }

    @Override
    public void update(WeatherData weatherData) {
        System.out.println("Mobile App Notification:");
        System.out.println("   Temperature: " + weatherData.getTemperature() + "C");
        System.out.println("   Humidity: " + weatherData.getHumidity() + "%");
        System.out.println("   Pressure: " + weatherData.getPressure() + " hPa");
        System.out.println("   Timestamp: " + weatherData.getTimestamp());
        System.out.println();
    }

    @Override
    public String getObserverName() {
        return appName + " Mobile App";
    }
}
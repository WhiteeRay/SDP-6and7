package com.weather.bot.observers;

import com.weather.bot.core.WeatherData;

public interface WeatherObserver {
    void update(WeatherData weatherData);
    String getObserverName();
}
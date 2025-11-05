package com.weather.bot.strategies;

import com.weather.bot.core.WeatherData;

public interface UpdateStrategy {
    WeatherData fetchWeatherData();
    String getStrategyName();
}
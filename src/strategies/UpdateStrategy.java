package strategies;

import core.WeatherData;

public interface UpdateStrategy {
    WeatherData fetchWeatherData();
    String getStrategyName();
}
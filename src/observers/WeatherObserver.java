package observers;

import core.WeatherData;

public interface WeatherObserver {
    void update(WeatherData weatherData);
    String getObserverName();
}
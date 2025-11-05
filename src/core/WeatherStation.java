package core;

import strategies.UpdateStrategy;
import observers.WeatherObserver;
import java.util.ArrayList;
import java.util.List;

public class WeatherStation {
    private final List<WeatherObserver> observers;
    private UpdateStrategy updateStrategy;
    private WeatherData currentWeather;

    public WeatherStation(UpdateStrategy initialStrategy) {
        this.observers = new ArrayList<>();
        this.updateStrategy = initialStrategy;
        this.currentWeather = new WeatherData(0, 0, 0);
    }

    public void registerObserver(WeatherObserver observer) {
        observers.add(observer);
        System.out.println("Registered: " + observer.getObserverName());
    }

    public void removeObserver(WeatherObserver observer) {
        observers.remove(observer);
        System.out.println("Removed: " + observer.getObserverName());
    }

    public void notifyObservers() {
        for (WeatherObserver observer : observers) {
            observer.update(currentWeather);
        }
    }

    public void setUpdateStrategy(UpdateStrategy strategy) {
        this.updateStrategy = strategy;
        System.out.println("Switched to: " + strategy.getStrategyName());
    }

    public void performUpdate() {
        System.out.println("--- Weather Update Cycle ---");
        System.out.println("Strategy: " + updateStrategy.getStrategyName());

        this.currentWeather = updateStrategy.fetchWeatherData();
        System.out.println("New Data: " + currentWeather);

        notifyObservers();
    }

    public String getCurrentStrategy() {
        return updateStrategy.getStrategyName();
    }

    public int getObserverCount() {
        return observers.size();
    }
}
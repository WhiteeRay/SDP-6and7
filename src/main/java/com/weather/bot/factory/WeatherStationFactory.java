package com.weather.bot.factory;

import com.weather.bot.core.WeatherStation;
import com.weather.bot.strategies.UpdateStrategy;
import org.springframework.stereotype.Component;

@Component
public class WeatherStationFactory {

    public WeatherStation createWeatherStation(UpdateStrategy strategy) {
        return new WeatherStation(strategy);
    }

    public WeatherStation createDefaultWeatherStation(UpdateStrategy strategy) {
        WeatherStation station = new WeatherStation(strategy);
        System.out.println("Created new WeatherStation with strategy: " + strategy.getStrategyName());
        return station;
    }

    public WeatherStation createStationWithType(String stationType, UpdateStrategy strategy) {
        WeatherStation station = new WeatherStation(strategy);

        switch (stationType.toLowerCase()) {
            case "urban":
                System.out.println("Created Urban Weather Station");
                break;
            case "coastal":
                System.out.println("Created Coastal Weather Station");
                break;
            case "mountain":
                System.out.println("Created Mountain Weather Station");
                break;
            default:
                System.out.println("Created Standard Weather Station");
        }

        return station;
    }
}
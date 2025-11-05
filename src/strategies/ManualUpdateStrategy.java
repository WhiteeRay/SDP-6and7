package strategies;

import core.WeatherData;

public class ManualUpdateStrategy implements UpdateStrategy {
    private WeatherData lastManualData;

    @Override
    public WeatherData fetchWeatherData() {
        System.out.println("Using manual weather input...");
        if (lastManualData == null) {
            lastManualData = new WeatherData(25.0, 60.0, 1015.0);
        }
        return lastManualData;
    }

    public void setManualData(double temperature, double humidity, double pressure) {
        this.lastManualData = new WeatherData(temperature, humidity, pressure);
        System.out.println("Manual data updated: " + lastManualData);
    }

    @Override
    public String getStrategyName() {
        return "Manual Input";
    }
}
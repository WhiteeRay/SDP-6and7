package observers;

import core.WeatherData;

public class SmartHomeSystem implements WeatherObserver {
    @Override
    public void update(WeatherData weatherData) {
        System.out.println("Smart Home System Adjustments:");

        if (weatherData.getTemperature() > 25) {
            System.out.println("   Turning on AC");
        } else if (weatherData.getTemperature() < 18) {
            System.out.println("   Turning on Heater");
        }

        if (weatherData.getHumidity() > 70) {
            System.out.println("   Activating Dehumidifier");
        } else if (weatherData.getHumidity() < 40) {
            System.out.println("   Activating Humidifier");
        }
        System.out.println();
    }

    @Override
    public String getObserverName() {
        return "Smart Home System";
    }
}
package observers;

import core.WeatherData;

public class MobileApp implements WeatherObserver {
    private final String appName;

    public MobileApp(String appName) {
        this.appName = appName;
    }

    @Override
    public void update(WeatherData weatherData) {
        System.out.println(appName + " App Notification:");
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
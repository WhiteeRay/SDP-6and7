package core;

import java.time.LocalDateTime;

public class WeatherData {
    private final double temperature;
    private final double humidity;
    private final double pressure;
    private final LocalDateTime timestamp;

    public WeatherData(double temperature, double humidity, double pressure) {
        this.temperature = temperature;
        this.humidity = humidity;
        this.pressure = pressure;
        this.timestamp = LocalDateTime.now();
    }

    public double getTemperature() { return temperature; }
    public double getHumidity() { return humidity; }
    public double getPressure() { return pressure; }
    public LocalDateTime getTimestamp() { return timestamp; }

    @Override
    public String toString() {
        return String.format("WeatherData{temp=%.1fC, humidity=%.1f%%, pressure=%.1fhPa}",
                temperature, humidity, pressure);
    }
}
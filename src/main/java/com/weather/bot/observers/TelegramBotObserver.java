package com.weather.bot.observers;

import org.springframework.stereotype.Component;
import com.weather.bot.core.WeatherData;
import com.weather.bot.service.TelegramService;

@Component
public class TelegramBotObserver implements WeatherObserver {
    private final TelegramService telegramService;

    public TelegramBotObserver(TelegramService telegramService) {
        this.telegramService = telegramService;
    }

    @Override
    public void update(WeatherData weatherData) {
        String message = createWeatherMessage(weatherData);
        telegramService.sendToAllSubscribers(message);
    }

    private String createWeatherMessage(WeatherData weatherData) {
        return "🌤️ *WEATHER UPDATE* 🌤️\n\n" +
                "📊 *Current Conditions:*\n" +
                "• 🌡️ Temperature: " + String.format("%.1f", weatherData.getTemperature()) + "°C\n" +
                "• 💧 Humidity: " + String.format("%.1f", weatherData.getHumidity()) + "%\n" +
                "• 📊 Pressure: " + String.format("%.1f", weatherData.getPressure()) + " hPa\n" +
                "• 🕐 Updated: " + weatherData.getTimestamp().toLocalTime() + "\n\n" +
                getWeatherAdvice(weatherData);
    }

    private String getWeatherAdvice(WeatherData weatherData) {
        StringBuilder advice = new StringBuilder("💡 *Recommendations:*\n");

        double temp = weatherData.getTemperature();
        double humidity = weatherData.getHumidity();

        if (temp > 28) {
            advice.append("• 🥵 It's hot! Stay hydrated\n");
        } else if (temp < 15) {
            advice.append("• 🥶 It's cold! Dress warmly\n");
        } else {
            advice.append("• 😊 Pleasant temperature\n");
        }

        if (humidity > 70) {
            advice.append("• 💦 High humidity, might feel muggy\n");
        } else if (humidity < 40) {
            advice.append("• 🏜️ Low humidity, stay hydrated\n");
        }

        return advice.toString();
    }

    @Override
    public String getObserverName() {
        return "Telegram Bot Observer";
    }


}
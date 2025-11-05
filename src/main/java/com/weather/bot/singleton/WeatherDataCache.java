package com.weather.bot.singleton;

import com.weather.bot.core.WeatherData;
import org.springframework.stereotype.Component;
import java.util.*;

@Component
public class WeatherDataCache {

    private static WeatherDataCache instance;
    private final Map<String, List<WeatherData>> cache;
    private final int MAX_CACHE_SIZE = 100;

    private WeatherDataCache() {
        this.cache = new LinkedHashMap<>() {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, List<WeatherData>> eldest) {
                return size() > MAX_CACHE_SIZE;
            }
        };
        System.out.println("WeatherDataCache Singleton initialized");
    }

    public static synchronized WeatherDataCache getInstance() {
        if (instance == null) {
            instance = new WeatherDataCache();
        }
        return instance;
    }

    public void cacheWeatherData(String location, WeatherData data) {
        cache.computeIfAbsent(location, k -> new ArrayList<>()).add(data);
        System.out.println("Cached weather data for: " + location);
    }

    public List<WeatherData> getCachedData(String location) {
        return cache.getOrDefault(location, new ArrayList<>());
    }

    public WeatherData getLatestCachedData(String location) {
        List<WeatherData> dataList = cache.get(location);
        if (dataList != null && !dataList.isEmpty()) {
            return dataList.get(dataList.size() - 1);
        }
        return null;
    }

    public void clearCache(String location) {
        cache.remove(location);
        System.out.println("Cleared cache for: " + location);
    }

    public void clearAllCache() {
        cache.clear();
        System.out.println("Cleared all weather data cache");
    }

    public int getCacheSize() {
        return cache.size();
    }

    public Set<String> getCachedLocations() {
        return cache.keySet();
    }
}
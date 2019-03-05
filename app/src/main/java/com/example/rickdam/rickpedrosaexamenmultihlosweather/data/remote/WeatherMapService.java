package com.example.rickdam.rickpedrosaexamenmultihlosweather.data.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherMapService {
    private static WeatherMapService INSTANCE;
    private final WeatherMap weatherMap;

    public static WeatherMapService getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new WeatherMapService(buildInstance());
        }
        return INSTANCE;
    }

    private static WeatherMap buildInstance() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org./data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(WeatherMap.class);
    }

    private WeatherMapService(WeatherMap weatherMap) {
        this.weatherMap = weatherMap;
    }

    public WeatherMap getWeatherMap() {
        return weatherMap;
    }
}

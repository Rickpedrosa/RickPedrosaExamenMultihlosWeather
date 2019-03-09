package com.example.rickdam.rickpedrosaexamenmultihlosweather.data.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherMapService {

    private static WeatherMapService INSTANCE;
    private final WeatherMap weatherMap;

    public static final String API_KEY = "02f7e06f18094f01937b2d887b02e9f5";
    public static final String API_SYNTAX = "appid";
    public static final String CITY_SYNTAX = "q";
    public static final String UNITS_SYNTAX = "units";
    public static final String UNITS = "metric";
    public static final String LANG_SYNTAX = "lang";
    public static final String LANG = "sp";

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

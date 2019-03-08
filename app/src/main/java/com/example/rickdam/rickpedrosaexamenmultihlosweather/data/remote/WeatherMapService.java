package com.example.rickdam.rickpedrosaexamenmultihlosweather.data.remote;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class WeatherMapService {

    private static WeatherMapService INSTANCE;
    private final WeatherMap weatherMap;

    public static final String apiKey = "02f7e06f18094f01937b2d887b02e9f5";
    public static final String apiSyntax = "appid";
    public static final String citySyntax = "q";
    public static final String unitsSyntax = "units";
    public static final String units = "metric";
    public static final String langSyntax = "lang";
    public static final String lang = "sp";

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

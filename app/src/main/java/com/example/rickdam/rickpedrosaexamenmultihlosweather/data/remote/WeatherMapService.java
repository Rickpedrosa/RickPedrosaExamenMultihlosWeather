package com.example.rickdam.rickpedrosaexamenmultihlosweather.data.remote;

import com.example.rickdam.rickpedrosaexamenmultihlosweather.data.entity.WeatherResponse;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface WeatherMapService {
    @GET("weather")
    Call<WeatherResponse> getCurrentWeatherv2(@QueryMap Map<String, String> params);
}

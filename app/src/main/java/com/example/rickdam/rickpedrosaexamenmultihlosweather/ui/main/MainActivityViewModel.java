package com.example.rickdam.rickpedrosaexamenmultihlosweather.ui.main;


import com.example.rickdam.rickpedrosaexamenmultihlosweather.data.entity.WeatherResponse;
import com.example.rickdam.rickpedrosaexamenmultihlosweather.data.model.CustomWeather;
import com.example.rickdam.rickpedrosaexamenmultihlosweather.data.remote.WeatherMapService;

import java.util.HashMap;
import java.util.Map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class MainActivityViewModel extends ViewModel {

    private MutableLiveData<Boolean> searchTrigger;
    private MutableLiveData<Boolean> loading;
    private MutableLiveData<CustomWeather> weather;

    void callWeatherApi(String city) {
        loading.postValue(true);
        Map<String, String> params = new HashMap<>();
        String apiKey = "02f7e06f18094f01937b2d887b02e9f5";
        params.put("appid", apiKey);
        params.put("q", city);
        params.put("units", "metric");
        params.put("lang", "sp");

        Call<WeatherResponse> call = WeatherMapService.getInstance().getWeatherMap()
                .getCurrentWeatherv2(params);
        //noinspection NullableProblems
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    String city_name = response.body().getName().concat(", ").concat(response.body().getSys().getCountry());
                    String weather_description = response.body().getWeather().get(0).getDescription();
                    String tempMin = String.valueOf(response.body().getMain().getTempMin()).concat("ºC");
                    String tempMax = String.valueOf(response.body().getMain().getTempMax()).concat("ºC");
                    String tempMedia = String.valueOf(response.body().getMain().getTemp()).concat("ºC");
                    String windSpeed = String.valueOf(response.body().getWind().getSpeed()).concat("mps");
                    String windDegrees = String.valueOf(response.body().getWind().getDeg()).concat("º");
                    String humidity = String.valueOf(response.body().getMain().getHumidity()).concat("%");
                    String cloudAll = String.valueOf(response.body().getClouds().getAll()).concat("%");
                    String logo = "http://openweathermap.org/img/w/" + response.body().getWeather().get(0).getIcon() + ".png";
                    String rain = response.body().getRain() == null ? "-" : "Sí";
                    String sunset = response.body().getSys().getSunset().toString();
                    String dawn = response.body().getSys().getSunrise().toString();
                    CustomWeather customWeather =
                            new CustomWeather(city_name, weather_description, tempMin, tempMax,
                                    tempMedia, windSpeed, windDegrees, humidity, cloudAll,
                                    logo, rain, sunset, dawn);

                    weather.postValue(customWeather);
                    loading.postValue(false);
                } else if (response.body() == null) {
                    CustomWeather customWeather =
                            new CustomWeather("Localidad no encontrada", "-", "-", "-",
                                    "-", "-", "-", "-", "-",
                                    "-", "-", "0", "0");
                    weather.postValue(customWeather);
                    loading.postValue(false);
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                CustomWeather customWeather =
                        new CustomWeather("", "", "-", "-",
                                "-", "-", "-", "-", "-",
                                "-", "-", "0", "0");
                weather.postValue(customWeather);
                loading.postValue(false);
            }
        });


    }

    void toggleSearch() {
        searchTrigger.postValue(true);
    }


    MutableLiveData<Boolean> observeSearchToggling() {
        if (searchTrigger == null) {
            searchTrigger = new MutableLiveData<>();
        }
        return searchTrigger;
    }

    LiveData<Boolean> observeLoading(){
        if(loading == null){
            loading = new MutableLiveData<>();
        }
        return loading;
    }

    LiveData<CustomWeather> observeWeather() {
        if (weather == null) {
            weather = new MutableLiveData<>();
        }
        return weather;
    }

}

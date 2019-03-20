package com.example.rickdam.rickpedrosaexamenmultihlosweather.ui.main;

import android.app.Notification;
import android.view.View;

import com.example.rickdam.rickpedrosaexamenmultihlosweather.R;
import com.example.rickdam.rickpedrosaexamenmultihlosweather.data.entity.WeatherResponse;
import com.example.rickdam.rickpedrosaexamenmultihlosweather.data.model.CustomWeather;
import com.example.rickdam.rickpedrosaexamenmultihlosweather.data.Repository;
import com.example.rickdam.rickpedrosaexamenmultihlosweather.utils.Event;
import com.google.android.material.snackbar.Snackbar;


import java.util.HashMap;
import java.util.Map;

import androidx.core.app.NotificationCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

class MainActivityViewModel extends ViewModel {

    private MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private MutableLiveData<CustomWeather> weather = new MutableLiveData<>();
    private MutableLiveData<Event<Snackbar>> snackBarEvent = new MutableLiveData<>();
    private MutableLiveData<Event<Notification>> notificationEvent = new MutableLiveData<>();
    private CustomWeather customWeather = new CustomWeather("", "", "-", "-",
            "-", "-", "-", "-", "-",
            "-", "-", "0", "0");

    void callWeatherApi(String city, final View view) {
        loading.postValue(true);
        Map<String, String> params = new HashMap<>();
        params.put(Repository.API_SYNTAX, Repository.API_KEY);
        params.put(Repository.CITY_SYNTAX, city);
        params.put(Repository.UNITS_SYNTAX, Repository.UNITS);
        params.put(Repository.LANG_SYNTAX, Repository.LANG);

        Call<WeatherResponse> call = Repository.getInstance().getWeatherMapService()
                .getCurrentWeatherv2(params);
        //noinspection NullableProblems
        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String city_name = response.body().getName().concat(", ").concat(response.body().getSys().getCountry());
                    String weather_description = response.body().getWeather().get(0).getDescription();
                    String tempMin = String.valueOf(response.body().getMain().getTempMin());
                    String tempMax = String.valueOf(response.body().getMain().getTempMax());
                    String tempMedia = String.valueOf(response.body().getMain().getTemp());
                    String windSpeed = String.valueOf(response.body().getWind().getSpeed());
                    String windDegrees = response.body().getWind().getDeg() == null ? "-" : String.valueOf(response.body().getWind().getDeg());
                    String humidity = String.valueOf(response.body().getMain().getHumidity());
                    String cloudAll = String.valueOf(response.body().getClouds().getAll());
                    String logo = response.body().getWeather().get(0).getIcon();
                    String rain = response.body().getRain() == null ? "-" : "Sí";
                    String sunset = response.body().getSys().getSunset().toString();
                    String dawn = response.body().getSys().getSunrise().toString();
                    customWeather =
                            new CustomWeather(city_name, weather_description, tempMin, tempMax,
                                    tempMedia, windSpeed, windDegrees, humidity, cloudAll,
                                    logo, rain, sunset, dawn);

                    weather.postValue(customWeather);
                    loading.postValue(false);
                    notificationEvent.postValue(new Event<>(new NotificationCompat.
                            Builder(view.getContext(), MainActivity.CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_search_white_24dp)
                            .setContentTitle(city_name)
                            .setContentText(weather_description.toUpperCase())
                            .build()));
                } else if (response.body() == null) {
                    customWeather =
                            new CustomWeather(view.getContext().getResources().getString(R.string.nodata_snack_value),
                                    "-", "-", "-",
                                    "-", "-", "-", "-", "-",
                                    "-", "-", "0", "0");
                    weather.postValue(customWeather);
                    loading.postValue(false);
                    snackBarEvent.postValue(new Event<>(
                            Snackbar.make(view.getRootView(), R.string.nodata_snack_value, Snackbar.LENGTH_SHORT)));
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                weather.postValue(customWeather);
                loading.postValue(false);
                snackBarEvent.postValue(new Event<>(
                        Snackbar.make(view.getRootView(), t.getMessage(), Snackbar.LENGTH_SHORT)));
            }
        });


    }

    LiveData<Boolean> observeLoading() {
        return loading;
    }

    LiveData<CustomWeather> observeWeather() {
        return weather;
    }

    LiveData<Event<Notification>> getNotificationEvent() {
        return notificationEvent;
    }

    LiveData<Event<Snackbar>> getSnackBarEvent() {
        return snackBarEvent;
    }
}

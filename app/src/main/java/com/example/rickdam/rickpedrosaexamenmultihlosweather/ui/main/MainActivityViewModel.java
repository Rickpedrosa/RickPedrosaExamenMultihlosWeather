package com.example.rickdam.rickpedrosaexamenmultihlosweather.ui.main;

import android.app.Notification;
import android.content.Context;
import android.view.View;

import com.example.rickdam.rickpedrosaexamenmultihlosweather.R;
import com.example.rickdam.rickpedrosaexamenmultihlosweather.data.entity.WeatherResponse;
import com.example.rickdam.rickpedrosaexamenmultihlosweather.data.model.CustomWeather;
import com.example.rickdam.rickpedrosaexamenmultihlosweather.data.remote.WeatherMapService;
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

    private MutableLiveData<Boolean> searchTrigger = new MutableLiveData<>();
    private MutableLiveData<Boolean> loading = new MutableLiveData<>();
    private MutableLiveData<CustomWeather> weather = new MutableLiveData<>();
    private Event<Snackbar> snackBarEvent;
    private MutableLiveData<Event<Notification>> notificationEvent = new MutableLiveData<>();

    void callWeatherApi(String city, final Context context) {
        loading.postValue(true);
        Map<String, String> params = new HashMap<>();
        params.put(WeatherMapService.apiSyntax, WeatherMapService.apiKey);
        params.put(WeatherMapService.citySyntax, city);
        params.put(WeatherMapService.unitsSyntax, WeatherMapService.units);
        params.put(WeatherMapService.langSyntax, WeatherMapService.lang);

        Call<WeatherResponse> call = WeatherMapService.getInstance().getWeatherMap()
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
                    String windDegrees = String.valueOf(response.body().getWind().getDeg());
                    String humidity = String.valueOf(response.body().getMain().getHumidity());
                    String cloudAll = String.valueOf(response.body().getClouds().getAll());
                    String logo = response.body().getWeather().get(0).getIcon();
                    String rain = response.body().getRain() == null ? "-" : "Sí";
                    String sunset = response.body().getSys().getSunset().toString();
                    String dawn = response.body().getSys().getSunrise().toString();
                    CustomWeather customWeather =
                            new CustomWeather(city_name, weather_description, tempMin, tempMax,
                                    tempMedia, windSpeed, windDegrees, humidity, cloudAll,
                                    logo, rain, sunset, dawn);

                    weather.postValue(customWeather);
                    loading.postValue(false);
                    notificationEvent.postValue(new Event<>(new NotificationCompat.Builder(context, MainActivity.CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_search_white_24dp)
                            .setContentTitle(city_name)
                            .setContentText(weather_description.toUpperCase())
                            .build()));
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
        return searchTrigger;
    }

    LiveData<Boolean> observeLoading() {
        return loading;
    }

    LiveData<CustomWeather> observeWeather() {
        return weather;
    }

    void setSnackBarEvent(View view) {
        snackBarEvent = new Event<>(Snackbar.make(view, "", Snackbar.LENGTH_SHORT));
    }

    Event<Snackbar> getSnackBarEvent() {
        return snackBarEvent;
    }

    LiveData<Event<Notification>> getNotificationEvent() {
        return notificationEvent;
    }
}

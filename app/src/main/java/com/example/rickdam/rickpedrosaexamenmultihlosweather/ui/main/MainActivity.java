package com.example.rickdam.rickpedrosaexamenmultihlosweather.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Notification;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.example.rickdam.rickpedrosaexamenmultihlosweather.R;
import com.example.rickdam.rickpedrosaexamenmultihlosweather.data.model.CustomWeather;
import com.example.rickdam.rickpedrosaexamenmultihlosweather.databinding.ActivityMainBinding;
import com.example.rickdam.rickpedrosaexamenmultihlosweather.utils.Event;
import com.example.rickdam.rickpedrosaexamenmultihlosweather.utils.TimeUtils;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    public static final String CHANNEL_ID = "1";
    private ActivityMainBinding b;
    private MainActivityViewModel viewModel;
    private NotificationManagerCompat notificationManagerCompat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        b = DataBindingUtil.setContentView(this, R.layout.activity_main);
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        setupViews();
        b.setLifecycleOwner(this);
        observeLiveData();
        search();
    }

    private void setupViews() {
        notificationManagerCompat = NotificationManagerCompat.from(MainActivity.this);
        b.progressBar.setVisibility(View.INVISIBLE);
    }

    private void search() {
        b.fabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(b.txtCiudad.getText().toString())) {
                    viewModel.callWeatherApi(b.txtCiudad.getText().toString(), b.lblValorAmanecer);
                    viewModel.toggleSearch();
                } else {
                    Event<Snackbar> snackBarEvent = new Event<>
                            (Snackbar.make(b.lblValorAmanecer, getString(R.string.empty_snack_value), Snackbar.LENGTH_SHORT));
                    snackBarEvent.getContentIfNotHandled().show();
                }
            }
        });
    }

    private void observeLiveData() {
        viewModel.observeSearchToggling().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                viewModel.observeWeather().observe(MainActivity.this, new Observer<CustomWeather>() {
                    @Override
                    public void onChanged(CustomWeather c) {
                        b.lblValorTiempoActual.setText(c.getWeather_description());
                        b.lblValorCiudad.setText(c.getCity_name());
                        b.lblValorTempMin.setText(c.getTempMin());
                        b.lblValorTempMax.setText(c.getTempMax());
                        b.lblValorTempMedia.setText(c.getTempMedia());
                        b.lblValorLluvia.setText(c.getRain());
                        b.lblValorHumedad.setText(c.getHumidity());
                        b.lblValorVelocidad.setText(c.getWindSpeed());
                        b.lblDireccionViento.setText(c.getWindDegrees());
                        b.lblValorNubosidad.setText(c.getCloudAll());
                        b.lblValorAmanecer.setText(TimeUtils.getDateCurrentTimeZone(Long.parseLong(c.getDawn())));
                        b.lblValorAtardecer.setText(TimeUtils.getDateCurrentTimeZone(Long.parseLong(c.getSunset())));
                        Picasso.with(b.imgValorTiempoIcono.getContext()).load(c.getLogo())
                                .resize(240, 240).into(b.imgValorTiempoIcono);
                    }
                });
            }
        });
        viewModel.observeLoading().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                b.progressBar.setVisibility(aBoolean ? View.VISIBLE : View.INVISIBLE);
            }
        });

        viewModel.getNotificationEvent().observe(this, new Observer<Event<Notification>>() {
            @Override
            public void onChanged(Event<Notification> notificationEvent) {
                if (!notificationEvent.hasBeenHandled()) {
                    notificationManagerCompat.notify(1, notificationEvent.getContentIfNotHandled());
                }
            }
        });

        viewModel.getSnackBarEvent().observe(this, new Observer<Event<Snackbar>>() {
            @Override
            public void onChanged(Event<Snackbar> snackBarEvent) {
                if (!snackBarEvent.hasBeenHandled()) {
                    snackBarEvent.getContentIfNotHandled().show();
                }
            }
        });
    }
}

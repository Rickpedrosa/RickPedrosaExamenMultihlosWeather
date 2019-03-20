package com.example.rickdam.rickpedrosaexamenmultihlosweather.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

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
        b.fabSearch.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(b.txtCiudad.getText().toString())) {
                viewModel.callWeatherApi(b.txtCiudad.getText().toString(), b.lblValorAmanecer);
                observeLiveData();
            } else {
                Event<Snackbar> snackBarEvent = new Event<>
                        (Snackbar.make(b.lblValorAmanecer, getString(R.string.empty_snack_value), Snackbar.LENGTH_SHORT));
                snackBarEvent.getContentIfNotHandled().show();
            }
        });
    }

    private void observeLiveData() {
        viewModel.observeWeather().observe(MainActivity.this, this::setWeatherInfoToUI);

        viewModel.observeLoading().observe(this, aBoolean
                -> b.progressBar.setVisibility(aBoolean ? View.VISIBLE : View.INVISIBLE));

        viewModel.getNotificationEvent().observe(this, notificationEvent -> {
            if (!notificationEvent.hasBeenHandled()) {
                notificationManagerCompat.notify(1, notificationEvent.getContentIfNotHandled());
            }
        });

        viewModel.getSnackBarEvent().observe(this, snackBarEvent -> {
            if (!snackBarEvent.hasBeenHandled()) {
                snackBarEvent.getContentIfNotHandled().show();
            }
        });
    }

    private void setWeatherInfoToUI(CustomWeather c) {
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
                .resize(getResources().getInteger(R.integer.iconApiSize),
                        getResources().getInteger(R.integer.iconApiSize)).into(b.imgValorTiempoIcono);
    }
}

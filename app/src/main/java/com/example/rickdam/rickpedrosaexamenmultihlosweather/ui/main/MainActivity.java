package com.example.rickdam.rickpedrosaexamenmultihlosweather.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rickdam.rickpedrosaexamenmultihlosweather.R;
import com.example.rickdam.rickpedrosaexamenmultihlosweather.data.model.CustomWeather;
import com.example.rickdam.rickpedrosaexamenmultihlosweather.utils.Event;
import com.example.rickdam.rickpedrosaexamenmultihlosweather.utils.TimeUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private EditText txtCiudad;
    private ImageView tempLogo;
    private FloatingActionButton searchFab;
    private TextView lbl_valorTiempo, lbl_valorCiudad, lbl_valorMinTemp, lbl_valorMaxTemp, lbl_valorMediaTemp,
            lbl_valorLluvia, lbl_valorHumedad, lbl_valorVelocidad, lbl_valorDireccion, lbl_valorNubosidad, lbl_valorAmanecer,
            lbl_valorAtardecer;
    private MainActivityViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        viewModel = ViewModelProviders.of(this).get(MainActivityViewModel.class);
        setupViews();
        viewModel.observeSearchToggler().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    setWeatherValues(txtCiudad.getText().toString());
                }
                viewModel.stopSearch();
            }
        });
        search();
    }

    private void setupViews() {
        lbl_valorTiempo = ActivityCompat.requireViewById(this, R.id.lbl_valorTiempoActual);
        lbl_valorCiudad = ActivityCompat.requireViewById(this, R.id.lbl_valorCiudad);
        lbl_valorMinTemp = ActivityCompat.requireViewById(this, R.id.lbl_valorTempMin);
        lbl_valorMaxTemp = ActivityCompat.requireViewById(this, R.id.lbl_valorTempMax);
        lbl_valorMediaTemp = ActivityCompat.requireViewById(this, R.id.lbl_valorTempMedia);
        lbl_valorLluvia = ActivityCompat.requireViewById(this, R.id.lbl_valorLluvia);
        lbl_valorHumedad = ActivityCompat.requireViewById(this, R.id.lbl_valorHumedad);
        lbl_valorVelocidad = ActivityCompat.requireViewById(this, R.id.lbl_valorVelocidad);
        lbl_valorDireccion = ActivityCompat.requireViewById(this, R.id.lbl_valorDireccion);
        lbl_valorNubosidad = ActivityCompat.requireViewById(this, R.id.lbl_valorNubosidad);
        lbl_valorAmanecer = ActivityCompat.requireViewById(this, R.id.lbl_valorAmanecer);
        lbl_valorAtardecer = ActivityCompat.requireViewById(this, R.id.lbl_valorAtardecer);

        txtCiudad = ActivityCompat.requireViewById(this, R.id.txt_ciudad);
        tempLogo = ActivityCompat.requireViewById(this, R.id.img_valorTiempoIcono);
        searchFab = ActivityCompat.requireViewById(this, R.id.fab_search);
    }

    private void setWeatherValues(String city) {
        viewModel.observeWeather(city).observe(this, new Observer<CustomWeather>() {
            @Override
            public void onChanged(CustomWeather c) {
                lbl_valorTiempo.setText(c.getWeather_description());
                lbl_valorCiudad.setText(c.getCity_name());
                lbl_valorMinTemp.setText(c.getTempMin());
                lbl_valorMaxTemp.setText(c.getTempMax());
                lbl_valorMediaTemp.setText(c.getTempMedia());
                lbl_valorLluvia.setText(c.getRain());
                lbl_valorHumedad.setText(c.getHumidity());
                lbl_valorVelocidad.setText(c.getWindSpeed());
                lbl_valorDireccion.setText(c.getWindDegrees());
                lbl_valorNubosidad.setText(c.getCloudAll());
                lbl_valorAmanecer.setText(TimeUtils.getDateCurrentTimeZone(Long.parseLong(c.getDawn())));
                lbl_valorAtardecer.setText(TimeUtils.getDateCurrentTimeZone(Long.parseLong(c.getSunset())));
                Picasso.with(tempLogo.getContext()).load(c.getLogo())
                        .resize(240, 240).into(tempLogo);

                if (c.getWeather_description().length() == 0) {
                    Snackbar.make(lbl_valorAmanecer, "Error en la carga de datos", Snackbar.LENGTH_SHORT).show();
                } else if (c.getWeather_description().equals("-")) {
                    Snackbar.make(lbl_valorAmanecer, "No hay datos para la ciudad consultada", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void search() {
        searchFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(txtCiudad.getText().toString())) {
                    viewModel.toggleSearch();
                } else {
                    Event<Toast> toast = new Event<>(Toast.makeText(MainActivity.this, "Introduce una localidad", Toast.LENGTH_SHORT));
                    toast.getContentIfNotHandled().show();
                    toast.hasBeenHandled();
                }
            }
        });
    }
}

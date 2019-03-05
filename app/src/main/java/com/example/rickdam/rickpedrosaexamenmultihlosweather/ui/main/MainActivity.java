package com.example.rickdam.rickpedrosaexamenmultihlosweather.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProviders;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rickdam.rickpedrosaexamenmultihlosweather.R;
import com.example.rickdam.rickpedrosaexamenmultihlosweather.data.entity.WeatherResponse;
import com.example.rickdam.rickpedrosaexamenmultihlosweather.data.model.CustomWeather;
import com.example.rickdam.rickpedrosaexamenmultihlosweather.data.remote.WeatherMapService;
import com.example.rickdam.rickpedrosaexamenmultihlosweather.utils.TimeUtils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

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
        search();
    }

    private void setupViews() {
        TextView lblCiudad = ActivityCompat.requireViewById(this, R.id.lbl_ciudad);
        TextView lblTemp = ActivityCompat.requireViewById(this, R.id.lbl_temperatura);
        TextView lblMinTemp = ActivityCompat.requireViewById(this, R.id.lbl_tempMin);
        TextView lblMaxTemp = ActivityCompat.requireViewById(this, R.id.lbl_tempMax);
        TextView lblMediaTemp = ActivityCompat.requireViewById(this, R.id.lbl_tempMedia);
        TextView lblLluvia = ActivityCompat.requireViewById(this, R.id.lbl_lluvia);
        TextView lblHumedad = ActivityCompat.requireViewById(this, R.id.lbl_humedad);
        TextView lblViento = ActivityCompat.requireViewById(this, R.id.lbl_viento);
        TextView lblVientoVelocidad = ActivityCompat.requireViewById(this, R.id.lbl_velocidad);
        TextView lblVientoDireccion = ActivityCompat.requireViewById(this, R.id.lbl_direccionViento);
        TextView lblNubosidad = ActivityCompat.requireViewById(this, R.id.lbl_nubosidad);
        TextView lblAmanecer = ActivityCompat.requireViewById(this, R.id.lbl_amanecer);
        TextView lblAtardecer = ActivityCompat.requireViewById(this, R.id.lbl_atardecer);

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
                lbl_valorAmanecer.setText(TimeUtils.getDateCurrentTimeZone(Long.parseLong(c.getSunset())));
                lbl_valorAtardecer.setText(TimeUtils.getDateCurrentTimeZone(Long.parseLong(c.getDawn())));
                Picasso.with(tempLogo.getContext()).load(c.getLogo())
                        .resize(240, 240).into(tempLogo);
            }
        });
    }

    private void search() {
        searchFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO Poner a true el livedata de trigger, si está en true usar setWeatherValues
                //TODO y capturar el contenido del edittext
                setWeatherValues(txtCiudad.getText().toString());
            }
        });
    }
}

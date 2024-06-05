package com.geesar.weatherapplication;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;


import android.content.SharedPreferences;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private EditText etCityName;
    private Button btnFetchWeather;
    private TextView tvAddress, tvTime, tvWeatherInfo;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCityName = findViewById(R.id.etCityName);
        btnFetchWeather = findViewById(R.id.btnFetchWeather);
        tvAddress = findViewById(R.id.tvAddress);
        tvTime = findViewById(R.id.tvTime);
        tvWeatherInfo = findViewById(R.id.tvWeatherInfo);

        sharedPreferences = getSharedPreferences("CityWeatherApp", MODE_PRIVATE);
        String lastCity = sharedPreferences.getString("last_city", "");
        if (!lastCity.isEmpty()) {
            etCityName.setText(lastCity);
            fetchWeatherData(lastCity);
        }

        btnFetchWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cityName = etCityName.getText().toString();
                if (!cityName.isEmpty()) {
                    fetchWeatherData(cityName);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("last_city", cityName);
                    editor.apply();
                }
            }
        });

        updateSystemTime();
    }

    private void fetchWeatherData(String cityName) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GetUrl service = retrofit.create(GetUrl.class);
        Call<WeatherResponse> call = service.getCurrentWeather(cityName, "525c30e890b57ea624ef16d6e2b4a347");

        call.enqueue(new Callback<WeatherResponse>() {
            @Override
            public void onResponse(Call<WeatherResponse> call, Response<WeatherResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    WeatherResponse weatherResponse = response.body();
                    String weatherInfo = "Temperature: " + weatherResponse.getMain().getTemp() + "\n"
                            + "Humidity: " + weatherResponse.getMain().getHumidity() + "\n"
                            + "Description: " + weatherResponse.getWeather().get(0).getDescription();
                    tvWeatherInfo.setText(weatherInfo);
                } else {
                    tvWeatherInfo.setText("Failed to get weather data");
                }
            }

            @Override
            public void onFailure(Call<WeatherResponse> call, Throwable t) {
                tvWeatherInfo.setText("Failed to get weather data");
            }
        });
    }

    private void updateSystemTime() {
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                String currentTime = sdf.format(new Date());
                tvTime.setText(currentTime);
                handler.postDelayed(this, 1000);
            }
        };
        handler.post(runnable);
    }
}
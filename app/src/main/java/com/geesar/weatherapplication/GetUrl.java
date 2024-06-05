package com.geesar.weatherapplication;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface GetUrl {
    @GET("weather")
    Call<WeatherResponse> getCurrentWeather(@Query("q") String cityName, @Query("appid") String apiKey);
}
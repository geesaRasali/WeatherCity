package com.geesar.weatherapplication;

import java.util.List;

public class WeatherResponse {
    private Main main;
    private List<DataWeather> weather;

    // Getters and setters
    public Main getMain() { return main; }
    public void setMain(Main main) { this.main = main; }
    public List<DataWeather> getWeather() { return weather; }
    public void setWeather(List<DataWeather> weather) { this.weather = weather; }
}
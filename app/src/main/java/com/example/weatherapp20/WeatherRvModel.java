package com.example.weatherapp20;

public class WeatherRvModel {
    private String temp,time , icon, windSpeed;

    public WeatherRvModel(String temp, String time, String icon, String windSpeed) {
        this.temp = temp;
        this.time = time;
        this.icon = icon;
        this.windSpeed = windSpeed;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }
}

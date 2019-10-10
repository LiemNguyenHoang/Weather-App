package com.example.wt.Model;

import java.util.HashMap;

public class ListOfWeather extends HashMap<String, Object> {
    private String Dt_txt;
    private  Object  Clouds,Main,Rain,Weather,Wind;

    public ListOfWeather() {
    }

    public ListOfWeather(String dt_txt, Object clouds, Object main, Object rain, Object weather, Object wind) {
        Dt_txt = dt_txt;
        Clouds = clouds;
        Main = main;
        Rain = rain;
        Weather = weather;
        Wind = wind;
    }

    public String getDt_txt() {
        return Dt_txt;
    }

    public void setDt_txt(String dt_txt) {
        Dt_txt = dt_txt;
    }

    public Object getClouds() {
        return Clouds;
    }

    public void setClouds(Object clouds) {
        Clouds = clouds;
    }

    public Object getMain() {
        return Main;
    }

    public void setMain(Object main) {
        Main = main;
    }

    public Object getRain() {
        return Rain;
    }

    public void setRain(Object rain) {
        Rain = rain;
    }

    public Object getWeather() {
        return Weather;
    }

    public void setWeather(Object weather) {
        Weather = weather;
    }

    public Object getWind() {
        return Wind;
    }

    public void setWind(Object wind) {
        Wind = wind;
    }
}

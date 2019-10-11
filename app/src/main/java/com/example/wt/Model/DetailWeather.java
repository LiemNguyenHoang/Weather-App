package com.example.wt.Model;

public class DetailWeather {
    String Dt_txt;
    String Clouds;
    String Rain;
    Weathers Weather;
    Winds Wind;
    Mains Main;

    public DetailWeather() {
    }
    public DetailWeather( String dt_txt ) {
        Dt_txt = dt_txt;
    }
    public DetailWeather(String dt_txt, String clouds, String rain, Weathers weather, Winds wind, Mains main) {
        Dt_txt = dt_txt;
        Clouds = clouds;
        Rain = rain;
        Weather = weather;
        Wind = wind;
        Main = main;
    }

    public String getDt_txt() {
        return Dt_txt;
    }

    public void setDt_txt(String dt_txt) {
        Dt_txt = dt_txt;
    }

    public String getClouds() {
        return Clouds;
    }

    public void setClouds(String clouds) {
        Clouds = clouds;
    }

    public String getRain() {
        return Rain;
    }

    public void setRain(String rain) {
        Rain = rain;
    }

    public Weathers getWeather() {
        return Weather;
    }

    public void setWeather(Weathers weather) {
        Weather = weather;
    }

    public Winds getWind() {
        return Wind;
    }

    public void setWind(Winds wind) {
        Wind = wind;
    }

    public Mains getMain() {
        return Main;
    }

    public void setMain(Mains main) {
        Main = main;
    }
}

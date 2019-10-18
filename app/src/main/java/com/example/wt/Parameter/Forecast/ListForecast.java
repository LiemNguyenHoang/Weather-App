package com.example.wt.Parameter.Forecast;

import java.util.ArrayList;

public class ListForecast {
    private CloudsForecast clouds;
    private Integer dt;
    private String dt_txt;
    private MainForecast main;
    private RainForecast rain;
    private SysForecast sys;
    private ArrayList<Weather> weather;
    private WindForecast wind;

    public ListForecast() {
    }

    public ListForecast(CloudsForecast clouds, Integer dt, String dt_txt, MainForecast main, RainForecast rain, SysForecast sys, ArrayList<Weather> weather, WindForecast wind) {
        this.clouds = clouds;
        this.dt = dt;
        this.dt_txt = dt_txt;
        this.main = main;
        this.rain = rain;
        this.sys = sys;
        this.weather = weather;
        this.wind = wind;
    }

    public CloudsForecast getClouds() {
        return clouds;
    }

    public void setClouds(CloudsForecast clouds) {
        this.clouds = clouds;
    }

    public Integer getDt() {
        return dt;
    }

    public void setDt(Integer dt) {
        this.dt = dt;
    }

    public String getDt_txt() {
        return dt_txt;
    }

    public void setDt_txt(String dt_txt) {
        this.dt_txt = dt_txt;
    }

    public MainForecast getMain() {
        return main;
    }

    public void setMain(MainForecast main) {
        this.main = main;
    }

    public RainForecast getRain() {
        return rain;
    }

    public void setRain(RainForecast rain) {
        this.rain = rain;
    }

    public SysForecast getSys() {
        return sys;
    }

    public void setSys(SysForecast sys) {
        this.sys = sys;
    }

    public ArrayList<Weather> getWeather() {
        return weather;
    }

    public void setWeather(ArrayList<Weather> weather) {
        this.weather = weather;
    }

    public WindForecast getWind() {
        return wind;
    }

    public void setWind(WindForecast wind) {
        this.wind = wind;
    }
}

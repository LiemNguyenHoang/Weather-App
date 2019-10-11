package com.example.wt.Model;

import java.util.ArrayList;
import java.util.HashMap;

public class ListOfWeather  {
    private City city;
    private  HashMap<String, ArrayList<DetailWeather>> detailWeatherHashMap;

    public ListOfWeather() {
    }

    public ListOfWeather(City city, HashMap<String, ArrayList<DetailWeather>> detailWeatherHashMap) {
        this.city = city;
        this.detailWeatherHashMap = detailWeatherHashMap;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public HashMap<String, ArrayList<DetailWeather>> getDetailWeatherHashMap() {
        return detailWeatherHashMap;
    }

    public void setDetailWeatherHashMap(HashMap<String, ArrayList<DetailWeather>> detailWeatherHashMap) {
        this.detailWeatherHashMap = detailWeatherHashMap;
    }
}

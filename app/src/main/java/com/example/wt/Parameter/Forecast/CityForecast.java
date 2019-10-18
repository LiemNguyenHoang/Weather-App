package com.example.wt.Parameter.Forecast;

public class CityForecast {
    private int id;
    private String name;
    private CoordForecast coord;
    private String country;
    private int timezone;
    private long sunrise;
    private long sunset;

    public CityForecast() {
    }

    public CityForecast(int id, String name, CoordForecast coord, String country, int timezone, long sunrise, long sunset) {
        this.id = id;
        this.name = name;
        this.coord = coord;
        this.country = country;
        this.timezone = timezone;
        this.sunrise = sunrise;
        this.sunset = sunset;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public CoordForecast getCoord() {
        return coord;
    }

    public void setCoord(CoordForecast coord) {
        this.coord = coord;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public int getTimezone() {
        return timezone;
    }

    public void setTimezone(int timezone) {
        this.timezone = timezone;
    }

    public long getSunrise() {
        return sunrise;
    }

    public void setSunrise(long sunrise) {
        this.sunrise = sunrise;
    }

    public long getSunset() {
        return sunset;
    }

    public void setSunset(long sunset) {
        this.sunset = sunset;
    }
}

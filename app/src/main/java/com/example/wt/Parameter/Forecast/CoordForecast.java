package com.example.wt.Parameter.Forecast;

public class CoordForecast {
public double lon,
    lat;

    public CoordForecast() {
    }

    public CoordForecast(double lon, double lat) {
        this.lon = lon;
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}

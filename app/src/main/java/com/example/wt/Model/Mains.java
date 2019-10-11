package com.example.wt.Model;

public class Mains {
    private String Humidity,
    Pressure,
    Temp,
    TempMax,
    TempMin;

    public Mains() {
    }

    public Mains(String humidity, String pressure, String temp, String tempMax, String tempMin) {
        Humidity = humidity;
        Pressure = pressure;
        Temp = temp;
        TempMax = tempMax;
        TempMin = tempMin;
    }

    public String getHumidity() {
        return Humidity;
    }

    public void setHumidity(String humidity) {
        Humidity = humidity;
    }

    public String getPressure() {
        return Pressure;
    }

    public void setPressure(String pressure) {
        Pressure = pressure;
    }

    public String getTemp() {
        return Temp;
    }

    public void setTemp(String temp) {
        Temp = temp;
    }

    public String getTempMax() {
        return TempMax;
    }

    public void setTempMax(String tempMax) {
        TempMax = tempMax;
    }

    public String getTempMin() {
        return TempMin;
    }

    public void setTempMin(String tempMin) {
        TempMin = tempMin;
    }
}

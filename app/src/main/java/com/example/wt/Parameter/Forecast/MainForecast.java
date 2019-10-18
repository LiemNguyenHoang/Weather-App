package com.example.wt.Parameter.Forecast;

public class MainForecast {
    private Double grnd_level,temp,temp_max,temp_min;
    private Integer humidity,pressure,sea_level,temp_kf;

    public MainForecast() {
    }

    public MainForecast(Double grnd_level, Double temp, Double temp_max, Double temp_min, Integer humidity, Integer pressure, Integer sea_level, Integer temp_kf) {
        this.grnd_level = grnd_level;
        this.temp = temp;
        this.temp_max = temp_max;
        this.temp_min = temp_min;
        this.humidity = humidity;
        this.pressure = pressure;
        this.sea_level = sea_level;
        this.temp_kf = temp_kf;
    }

    public Double getGrnd_level() {
        return grnd_level;
    }

    public void setGrnd_level(Double grnd_level) {
        this.grnd_level = grnd_level;
    }

    public Double getTemp() {
        return temp;
    }

    public void setTemp(Double temp) {
        this.temp = temp;
    }

    public Double getTemp_max() {
        return temp_max;
    }

    public void setTemp_max(Double temp_max) {
        this.temp_max = temp_max;
    }

    public Double getTemp_min() {
        return temp_min;
    }

    public void setTemp_min(Double temp_min) {
        this.temp_min = temp_min;
    }

    public Integer getHumidity() {
        return humidity;
    }

    public void setHumidity(Integer humidity) {
        this.humidity = humidity;
    }

    public Integer getPressure() {
        return pressure;
    }

    public void setPressure(Integer pressure) {
        this.pressure = pressure;
    }

    public Integer getSea_level() {
        return sea_level;
    }

    public void setSea_level(Integer sea_level) {
        this.sea_level = sea_level;
    }

    public Integer getTemp_kf() {
        return temp_kf;
    }

    public void setTemp_kf(Integer temp_kf) {
        this.temp_kf = temp_kf;
    }
}

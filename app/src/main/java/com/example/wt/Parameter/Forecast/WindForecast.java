package com.example.wt.Parameter.Forecast;

import androidx.annotation.NonNull;

public class WindForecast {
    public double speed;
    public double deg;

    public WindForecast() {
    }

    @NonNull
    @Override
    public String toString() {
        String str="WindCurrent\n";
        str+="speed: "+this.speed+"\t"+"deg: "+this.deg;
        return str;
    }

    public WindForecast(double speed, double deg) {
        this.speed = speed;
        this.deg = deg;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getDeg() {
        return deg;
    }

    public void setDeg(int deg) {
        this.deg = deg;
    }
}

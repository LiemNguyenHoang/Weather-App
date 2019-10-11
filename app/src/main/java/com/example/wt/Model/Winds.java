package com.example.wt.Model;

public class Winds {
    private String Deg,Speed;

    public Winds() {
    }

    public Winds(String deg, String speed) {
        Deg = deg;
        Speed = speed;
    }

    public String getDeg() {
        return Deg;
    }

    public void setDeg(String deg) {
        Deg = deg;
    }

    public String getSpeed() {
        return Speed;
    }

    public void setSpeed(String speed) {
        Speed = speed;
    }
}

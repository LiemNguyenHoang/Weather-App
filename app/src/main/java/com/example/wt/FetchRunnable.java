package com.example.wt;

import android.app.ProgressDialog;
import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchRunnable implements Runnable {
    private int id;
    private volatile String string;
    private Context context;
    private ProgressDialog progressDialog;

    private String choose;
    private double lon,lat;

    public FetchRunnable() {
    }


    public FetchRunnable(int id, Context context, String choose) {
        this.context = context;
        this.id = id;
        progressDialog = new ProgressDialog(context);
        this.choose = choose;
    }

    public FetchRunnable(double lat, double lon, Context context, String choose) {
        this.lat = lat;
        this.lon=lon;
        this.context = context;
        progressDialog = new ProgressDialog(context);
        this.choose = choose;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    @Override
    public void run() {
        StringBuilder sb = null;
        progressDialog.setMessage("Đang xử lý ... ");
        progressDialog.setIndeterminate(true);

        // Lỗi không hiễn thị được progress bar lên

        try {


            String link = "";

            if (this.choose.equals("LatLng")) {
                link = "https://api.openweathermap.org/data/2.5/weather?lat=" + this.getLat() + "&lon=" + this.getLon() + "&appid=" + MainActivity.KEY_API;
            } else {
                link = "https://api.openweathermap.org/data/2.5/" + this.choose + "?id=" + this.getId() + "&appid=" + MainActivity.KEY_API;
            }
//            link = "https://api.openweathermap.org/data/2.5/" + this.choose + "?id=" + 1587923 + "&appid=" + MainActivity.KEY_API;
            URL url = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();
            connection.getInputStream().getClass();
            if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                sb = new StringBuilder();
                String line = "";
                while ((line = br.readLine()) != null) {
                    sb.append(line).append("\n");
                }
                br.close();
                this.string = sb.toString();
                progressDialog.dismiss();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getValue() {
        return this.string;
    }
}

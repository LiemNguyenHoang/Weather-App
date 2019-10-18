package com.example.wt;

import android.app.ProgressDialog;
import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class FetchRunnable implements Runnable {
    private int ID;
    private volatile String string;
    private Context context;
    private ProgressDialog progressDialog;
    private String choose;

    public FetchRunnable() {
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public FetchRunnable(int ID, Context context, String choose) {
        this.context = context;
        this.ID = ID;
        progressDialog = new ProgressDialog(context);
        this.choose = choose;
    }

    @Override
    public void run() {
        StringBuilder sb = null;
        progressDialog.setMessage("Đang xử lý ... ");
        progressDialog.setIndeterminate(true);
        // Lỗi không hiễn thị được progress bar lên

        try {

            String link = "https://api.openweathermap.org/data/2.5/" + this.choose + "?id=" + this.getID() + "&appid=" + MainActivity.KEY_API;
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
//                progressDialog.dismiss();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getValue() {
        return this.string;
    }
}

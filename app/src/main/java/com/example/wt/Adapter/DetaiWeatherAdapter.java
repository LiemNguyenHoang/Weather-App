package com.example.wt.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wt.Model.City;
import com.example.wt.Model.DetailWeather;
import com.example.wt.Model.ListOfWeather;
import com.example.wt.Model.Weathers;
import com.example.wt.R;

import java.util.ArrayList;
import java.util.HashMap;

public class DetaiWeatherAdapter extends RecyclerView.Adapter<DetaiWeatherAdapter.DetailWeatherViewHolder> {
    private static final String TAG = "DetaiWeatherAdapter";
    private Context context;
    private ArrayList<ListOfWeather> listOfWeathers;
    private LayoutInflater layoutInflater;

    public DetaiWeatherAdapter(Context context, ArrayList<ListOfWeather> listOfWeather) {

        this.context = context;
        this.listOfWeathers = listOfWeather;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public DetailWeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.row_item_weather, parent, false);
        return new DetailWeatherViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailWeatherViewHolder holder, int position) {
        City city = listOfWeathers.get(position).getCity();
        HashMap<String, ArrayList<DetailWeather>> detailWeather = listOfWeathers.get(position).getDetailWeatherHashMap();

        // Tách lấy time của từng date
        String date = "2019-10-11";
        ArrayList<DetailWeather> listCurrent = detailWeather.get(date);
        int time = 0;
        DetailWeather detailWeather1 = listCurrent.get(0);



//        holder.tvTime.setText(detailWeatherArrayList.get(0).getDt_txt());
//        holder.tvLocate.setText(detailWeatherArrayList.get(0));


        int i = 0;

    }

    @Override
    public int getItemCount() {
        return listOfWeathers.size();
    }

    class DetailWeatherViewHolder extends RecyclerView.ViewHolder {
        private TextView tvLocate;
        private TextView tvTime;


        public DetailWeatherViewHolder(@NonNull View itemView) {
            super(itemView);

            tvLocate = itemView.findViewById(R.id.tvLocate);
            tvTime = itemView.findViewById(R.id.tvTime);
        }
    }
}

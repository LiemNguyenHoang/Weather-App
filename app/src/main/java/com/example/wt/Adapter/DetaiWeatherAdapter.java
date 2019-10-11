package com.example.wt.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wt.Model.DetailWeather;
import com.example.wt.R;

import java.util.ArrayList;

public class DetaiWeatherAdapter extends RecyclerView.Adapter<DetaiWeatherAdapter.DetailWeatherViewHolder> {
    private static final String TAG = "DetaiWeatherAdapter";
    private Context context;
    private ArrayList<DetailWeather> detailWeatherArrayList;
    private LayoutInflater layoutInflater;

    public DetaiWeatherAdapter(Context context, ArrayList<DetailWeather> detailWeatherArrayList) {

        this.context = context;
        this.detailWeatherArrayList = detailWeatherArrayList;
        layoutInflater = LayoutInflater.from(context);

    }

    @NonNull
    @Override
    public DetailWeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.row_item_weather,parent,false);
        return new DetailWeatherViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailWeatherViewHolder holder, int position) {
        DetailWeather detailWeather = detailWeatherArrayList.get(position);

        holder.tvLocate.setText(detailWeather.getDt_txt());
        holder.tvTime.setText(detailWeather.getRain());

    }

    @Override
    public int getItemCount() {
        return detailWeatherArrayList.size();
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

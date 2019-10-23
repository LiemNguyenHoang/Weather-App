package com.example.wt.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wt.MainActivity;
import com.example.wt.Model.City;
import com.example.wt.Model.DetailWeather;
import com.example.wt.Model.ListOfWeather;
import com.example.wt.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class DetailWeatherHistoryAdapter extends RecyclerView.Adapter<DetailWeatherHistoryAdapter.DetailWeatherHistoryViewHolder> {
    private static final String TAG = "DetaiWeatherAdapter";
    private DetailWeather detailWeather;
    private Context context;
    private ArrayList<DetailWeather> listOfWeathers;
    private LayoutInflater layoutInflater;
    private boolean flag;

    public DetailWeatherHistoryAdapter(Context context, ArrayList<DetailWeather> listOfWeather) {

        this.context = context;
        this.listOfWeathers = listOfWeather;
        layoutInflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public DetailWeatherHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.row_item_weather, parent, false);
        return new DetailWeatherHistoryAdapter.DetailWeatherHistoryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailWeatherHistoryViewHolder holder, final int position) {
        this.detailWeather = listOfWeathers.get(position);
        showDisplay(detailWeather, holder);

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetailWeather detailWeather11 = listOfWeathers.get(position);
                Intent intent = new Intent(context, MainActivity.class);
                intent.putExtra("id_locationTime", detailWeather11.getId()
                        + "," + listOfWeathers.get(position).getDt_txt().split(" ")[0]
                +","+listOfWeathers.size());

                String size = listOfWeathers.size()+"";
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return listOfWeathers.size();
    }


    class DetailWeatherHistoryViewHolder extends RecyclerView.ViewHolder {
        private TextView tvLocate;
        private TextView tvTime;
        private TextView tvTemp;
        private TextView tvRain;
        private TextView tvWind;
        //        private TextView tvStartTime;
        private TextView tvCurrentTime;
        private ImageView imgWeather;
        private ImageView imgWind;
        private ImageView imgRain;
        //        private TextView tvEndTime;
        private SeekBar seekTime;
        private Button btnExpand;
        private Button btnDelete;
        private LinearLayout linearLayout;


        public DetailWeatherHistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            tvLocate = itemView.findViewById(R.id.tvLocate);
            tvTime = itemView.findViewById(R.id.tvTime);
            tvTemp = itemView.findViewById(R.id.tvTemp);
            tvRain = itemView.findViewById(R.id.tvRain);
            tvWind = itemView.findViewById(R.id.tvWind);
            btnExpand = itemView.findViewById(R.id.btnExpand);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            seekTime = itemView.findViewById(R.id.seekTime);
//            tvStartTime = itemView.findViewById(R.id.tvStartTime);
            tvCurrentTime = itemView.findViewById(R.id.tvCurrentTime);
//            tvEndTime = itemView.findViewById(R.id.tvEndTime);
            imgRain = itemView.findViewById(R.id.imgRain);
            imgWeather = itemView.findViewById(R.id.imgWeather);
            imgWind = itemView.findViewById(R.id.imgWind);

//            tvStartTime.setVisibility(View.GONE);
            tvCurrentTime.setVisibility(View.GONE);
//            tvEndTime.setVisibility(View.GONE);
            seekTime.setVisibility(View.GONE);
            btnExpand.setVisibility(View.GONE);

        }
    }

    // Show weather của date current
    public void showDisplay(DetailWeather detailWeather, DetailWeatherHistoryAdapter.DetailWeatherHistoryViewHolder holder) {
        String sss = detailWeather.getMain().getTemp();
        double d = Double.parseDouble(sss);
        int tempInt = (int) Math.round(d);
        holder.tvLocate.setText(detailWeather.getLocate() + ", " + detailWeather.getCountry()+"|"+detailWeather.getId());
        holder.tvTime.setText(detailWeather.getDt_txt());
        holder.tvTemp.setText(tempInt + "");
        holder.tvRain.setText(detailWeather.getRain());
        holder.tvWind.setText(detailWeather.getWind().getSpeed());
        holder.tvCurrentTime.setText(detailWeather.getDt_txt());

        // show image
        holder.imgWeather.setImageResource(getImage(detailWeather.getWeather().getIcon()));
    }

    public int getImage(String string) {
        int result = 0;

        switch (string) {
            case "01d":
                result = R.drawable.i01;
                break;
            case "02d":
                result = R.drawable.i02;
                break;
            case "03d":
                result = R.drawable.i03;
                break;
            case "04d":
                result = R.drawable.i04;
                break;
            case "09d":
                result = R.drawable.i09;
                break;
            case "10d":
                result = R.drawable.i10;
                break;
            case "11d":
                result = R.drawable.i11;
                break;
            case "13d":
                result = R.drawable.i13;
                break;
            case "50d":
                result = R.drawable.i50;
                break;
            case "01n":
                result = R.drawable.i01;
                break;
            case "02n":
                result = R.drawable.i02;
                break;
            case "03n":
                result = R.drawable.i03;
                break;
            case "04n":
                result = R.drawable.i04;
                break;
            case "09n":
                result = R.drawable.i09;
                break;
            case "10n":
                result = R.drawable.i10;
                break;
            case "11n":
                result = R.drawable.i11;
                break;
            case "13n":
                result = R.drawable.i13;
                break;
            case "50n":
                result = R.drawable.i50;
                break;
        }
        return result;
    }

}

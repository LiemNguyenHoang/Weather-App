package com.example.wt.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wt.MainActivity;
import com.example.wt.Model.City;
import com.example.wt.Model.DetailWeather;
import com.example.wt.Model.ListOfWeather;
import com.example.wt.Model.Weathers;
import com.example.wt.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class DetaiWeatherAdapter extends RecyclerView.Adapter<DetaiWeatherAdapter.DetailWeatherViewHolder> {
    private static final String TAG = "DetaiWeatherAdapter";
    private HashMap<String, ArrayList<DetailWeather>> detailWeather;
    private Context context;
    private ArrayList<ListOfWeather> listOfWeathers;
    private LayoutInflater layoutInflater;
    private boolean flag;
    private String id;

    public DetaiWeatherAdapter(Context context, ArrayList<ListOfWeather> listOfWeather) {

        this.context = context;
        this.listOfWeathers = listOfWeather;
        layoutInflater = LayoutInflater.from(context);
    }

    public DetaiWeatherAdapter(Context context, ArrayList<ListOfWeather> listOfWeather, String id) {

        this.context = context;
        this.listOfWeathers = listOfWeather;
        layoutInflater = LayoutInflater.from(context);
        this.id = id;
    }

    @NonNull
    @Override
    public DetailWeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = layoutInflater.inflate(R.layout.row_item_weather, parent, false);
        return new DetailWeatherViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final DetailWeatherViewHolder holder, final int position) {
        final City city = listOfWeathers.get(position).getCity();
        this.detailWeather = listOfWeathers.get(position).getDetailWeatherHashMap();

        // Start: Tách lấy time của từng date đã sắp xếp
        ArrayList<String> listKey = new ArrayList<>(); // list get entire key
        for (String key : detailWeather.keySet()) {
            listKey.add(key);
        }
        Collections.sort(listKey);
        // End
        final ArrayList<DetailWeather> listCurrent = detailWeather.get(listKey.get(0));


        final int nWeather = listCurrent.size();
        holder.seekTime.setMax(nWeather - 1);

        flag = false;

        holder.btnExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flag = !flag;
                if (flag) {
                    holder.linearLayout.setVisibility(View.VISIBLE);
                    holder.btnExpand.setText("COLLAPSE");
                } else {

                    holder.linearLayout.setVisibility(View.GONE);
                    holder.btnExpand.setText("EXPAND");
                }
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                listCurrent.remove(position);
//                 Lấy id của locate cần xóa
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                String time = sdf.format(new Date());
                String id = listOfWeathers.get(position).getCity().getId();



                int i = 0;
                FirebaseFirestore db = FirebaseFirestore.getInstance();
                db.collection(MainActivity.getID())
                        .get()
                        .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                ArrayList<Object> list = (ArrayList<Object>) queryDocumentSnapshots.toObjects(Object.class);
                                ArrayList<Long> listIdLocate = new ArrayList<>();
                            }
                        });

                // xóa trong firebase

                notifyDataSetChanged();
            }
        });
        holder.seekTime.setMax(nWeather - 1);
        // show display at position 0
        holder.tvLocate.setText(city.getName()+", "+city.getCountry());

        showDisplay(listCurrent.get(0), holder);
        // show display for date forecast
        showForecast(this.detailWeather, holder);
        holder.seekTime.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                showDisplay(listCurrent.get(progress), holder);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    @Override
    public int getItemCount() {
        return listOfWeathers.size();
    }


    class DetailWeatherViewHolder extends RecyclerView.ViewHolder {
        private TextView tvLocate;
        private TextView tvTime;
        private TextView tvTemp;
        private TextView tvRain;
        private TextView tvWind;
        private TextView tvCurrentTime;
        private ImageView imgWeather;
        private ImageView imgWind;
        private ImageView imgRain;
        private SeekBar seekTime;
        private Button btnExpand;
        private Button btnDelete;
        private LinearLayout linearLayout;
        private TextView tvDate1;
        private TextView tvDate2;
        private TextView tvDate3;
        private TextView tvDate4;
        private TextView tvDate5;
        private TextView tvTemp1_1;
        private TextView tvTemp1_2;
        private TextView tvTemp2_1;
        private TextView tvTemp2_2;
        private TextView tvTemp3_1;
        private TextView tvTemp3_2;
        private TextView tvTemp4_1;
        private TextView tvTemp4_2;
        private TextView tvTemp5_1;
        private TextView tvTemp5_2;
        private ImageView img1;
        private ImageView img2;
        private ImageView img3;
        private ImageView img4;
        private ImageView img5;


        public DetailWeatherViewHolder(@NonNull View itemView) {
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
            tvCurrentTime = itemView.findViewById(R.id.tvCurrentTime);
            imgRain = itemView.findViewById(R.id.imgRain);
            imgWeather = itemView.findViewById(R.id.imgWeather);
            imgWind = itemView.findViewById(R.id.imgWind);


            tvDate1 = itemView.findViewById(R.id.tvDate1);
            tvDate2 = itemView.findViewById(R.id.tvDate2);
            tvDate3 = itemView.findViewById(R.id.tvDate3);
            tvDate4 = itemView.findViewById(R.id.tvDate4);
            tvDate5 = itemView.findViewById(R.id.tvDate5);
            tvTemp1_1 = itemView.findViewById(R.id.tvTemp1_1);
            tvTemp2_1 = itemView.findViewById(R.id.tvTemp2_1);
            tvTemp3_1 = itemView.findViewById(R.id.tvTemp3_1);
            tvTemp4_1 = itemView.findViewById(R.id.tvTemp4_1);
            tvTemp5_1 = itemView.findViewById(R.id.tvTemp5_1);
            tvTemp1_2 = itemView.findViewById(R.id.tvTemp1_2);
            tvTemp2_2 = itemView.findViewById(R.id.tvTemp2_2);
            tvTemp3_2 = itemView.findViewById(R.id.tvTemp3_2);
            tvTemp4_2 = itemView.findViewById(R.id.tvTemp4_2);
            tvTemp5_2 = itemView.findViewById(R.id.tvTemp5_2);
            img1 = itemView.findViewById(R.id.img1);
            img2 = itemView.findViewById(R.id.img2);
            img3 = itemView.findViewById(R.id.img3);
            img4 = itemView.findViewById(R.id.img4);
            img5 = itemView.findViewById(R.id.img5);

        }
    }

    // Show weather của date current
    public void showDisplay(DetailWeather detailWeather, DetailWeatherViewHolder holder) {
        String sss = detailWeather.getMain().getTemp();
        double d = Double.parseDouble(sss);
        int tempInt = (int) Math.round(d);
        holder.tvTemp.setText(tempInt + "");
        holder.tvRain.setText(detailWeather.getRain());
        holder.tvWind.setText(detailWeather.getWind().getSpeed());
        String[] strings = detailWeather.getDt_txt().split(" ")[1].split(":");
        String string = strings[0]+":"+strings[1];
        holder.tvCurrentTime.setText(string);

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

    //++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    public void showCurrent(ArrayList<DetailWeather> listCurrent, DetailWeatherViewHolder holder) {


    }

    // Show display của date forecast
    public void showForecast(HashMap<String, ArrayList<DetailWeather>> detailWeatherHashMap, DetailWeatherViewHolder holder) {
        ArrayList<String> listKey = new ArrayList<>(); // list get entire key
        for (String key : detailWeatherHashMap.keySet()) {
            listKey.add(key);
        }
        Collections.sort(listKey);

        // show weather của date 1 kế tiếp
        {
            // Show date của current
            ArrayList<DetailWeather> listOfWeathers = detailWeatherHashMap.get(listKey.get(0));
            holder.tvTime.setText(listOfWeathers.get(0).getDt_txt().split(" ")[0]);

            // Ngày 1
            listOfWeathers = detailWeatherHashMap.get(listKey.get(1));
            int tempMax_1 = getTempMaxDate(listOfWeathers);
            int tempMmin_1 = getTempDateMin(listOfWeathers);
            String getWeather = getWeatherDate(listOfWeathers);
            holder.tvDate1.setText(listOfWeathers.get(0).getDt_txt().split(" ")[0]);
            holder.tvTemp1_1.setText(tempMax_1 + "");
            holder.tvTemp1_2.setText(tempMmin_1 + "");
            holder.img1.setImageResource(getImage(getWeather));

            // Ngày 2
            listOfWeathers = detailWeatherHashMap.get(listKey.get(2));
            tempMax_1 = getTempMaxDate(listOfWeathers);
            tempMmin_1 = getTempDateMin(listOfWeathers);
            getWeather = getWeatherDate(listOfWeathers);
            holder.tvDate2.setText(listOfWeathers.get(0).getDt_txt().split(" ")[0]);
            holder.tvTemp2_1.setText(tempMax_1 + "");
            holder.tvTemp2_2.setText(tempMmin_1 + "");
            holder.img2.setImageResource(getImage(getWeather));

            // Ngày 3
            listOfWeathers = detailWeatherHashMap.get(listKey.get(3));
            tempMax_1 = getTempMaxDate(listOfWeathers);
            tempMmin_1 = getTempDateMin(listOfWeathers);
            getWeather = getWeatherDate(listOfWeathers);
            holder.tvDate3.setText(listOfWeathers.get(0).getDt_txt().split(" ")[0]);
            holder.tvTemp3_1.setText(tempMax_1 + "");
            holder.tvTemp3_2.setText(tempMmin_1 + "");
            holder.img3.setImageResource(getImage(getWeather));

            // Ngày 4
            listOfWeathers = detailWeatherHashMap.get(listKey.get(4));
            tempMax_1 = getTempMaxDate(listOfWeathers);
            tempMmin_1 = getTempDateMin(listOfWeathers);
            getWeather = getWeatherDate(listOfWeathers);
            holder.tvDate4.setText(listOfWeathers.get(0).getDt_txt().split(" ")[0]);
            holder.tvTemp4_1.setText(tempMax_1 + "");
            holder.tvTemp4_2.setText(tempMmin_1 + "");
            holder.img4.setImageResource(getImage(getWeather));

            // Ngày 5
            listOfWeathers = detailWeatherHashMap.get(listKey.get(5));
            tempMax_1 = getTempMaxDate(listOfWeathers);
            tempMmin_1 = getTempDateMin(listOfWeathers);
            getWeather = getWeatherDate(listOfWeathers);
            holder.tvDate5.setText(listOfWeathers.get(0).getDt_txt().split(" ")[0]);
            holder.tvTemp5_1.setText(tempMax_1 + "");
            holder.tvTemp5_2.setText(tempMmin_1 + "");
            holder.img5.setImageResource(getImage(getWeather));
        }


        int i = 0;
    }

    // Lấy nhiệt độ thấp nhất trong ngày
    public int getTempMaxDate(ArrayList<DetailWeather> listDetailWeathers) {
        String string = listDetailWeathers.get(0).getMain().getTempMax();
        double d = Double.parseDouble(string);
        int tempMax = (int) Math.round(d);

        for (int i = 0; i < listDetailWeathers.size(); i++) {
            string = listDetailWeathers.get(i).getMain().getTempMax();
            d = Double.parseDouble(string);
            int temp = (int) Math.round(d);
            if (temp > tempMax)
                tempMax = temp;
        }

        return tempMax;
    }

    // Lấy nhiệt độ thấp nhất trong ngày
    public int getTempDateMin(ArrayList<DetailWeather> listDetailWeathers) {
        String string = listDetailWeathers.get(0).getMain().getTempMin();
        double d = Double.parseDouble(string);
        int tempMin = (int) Math.round(d);

        for (int i = 0; i < listDetailWeathers.size(); i++) {
            string = listDetailWeathers.get(i).getMain().getTempMin();
            d = Double.parseDouble(string);
            int temp = (int) Math.round(d);
            if (temp < tempMin)
                tempMin = temp;
        }

        return tempMin;
    }

    // Lấy thời tiết dựa theo icon của weather link: https://openweathermap.org/weather-conditions
    public String getWeatherDate(ArrayList<DetailWeather> listDetailWeathers) {
        ArrayList<String> listWeather = new ArrayList<>();
        for (int i = 0; i < listDetailWeathers.size(); i++) {
            listWeather.add(listDetailWeathers.get(i).getWeather().getIcon());
        }
        Collections.sort(listWeather);

        return listWeather.get(listWeather.size() - 1);
    }
}

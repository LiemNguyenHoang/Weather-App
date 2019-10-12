package com.example.wt.Fragment;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.wt.Adapter.DetaiWeatherAdapter;
import com.example.wt.Model.City;
import com.example.wt.Model.Coords;
import com.example.wt.Model.DetailWeather;
import com.example.wt.Model.ListOfWeather;
import com.example.wt.Model.Mains;
import com.example.wt.Model.Weathers;
import com.example.wt.Model.Winds;
import com.example.wt.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {
    private RecyclerView rv_weather;
    private DetaiWeatherAdapter detaiWeatherAdapter;
    private ArrayList<ListOfWeather> listOfWeathers;
    FirebaseFirestore db;

    public WeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_weather, container, false);
        // Inflate the layout for this fragment
        rv_weather = view.findViewById(R.id.rv_weather);
        listOfWeathers = new ArrayList<>();
//        detailWeatherArrayList.add(new DetailWeather("say la la"));
//        detailWeatherArrayList.add(new DetailWeather("say"));
//        detailWeatherArrayList.add(new DetailWeather(" la la"));
//        detailWeatherArrayList.add(new DetailWeather("say  la"));
//        detailWeatherArrayList.add(new DetailWeather("say l"));
//        detailWeatherArrayList.add(new DetailWeather("say a"));
//        detailWeatherArrayList.add(new DetailWeather("say al"));

        final String locate = "Bình Phước",
                date = "9-10-2019";
        detaiWeatherAdapter = new DetaiWeatherAdapter(getContext(), listOfWeathers);
        rv_weather.setAdapter(detaiWeatherAdapter);



        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Cities").document(locate).collection(date).document("forecast");
        docRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Map<String, Object> map = documentSnapshot.getData();

                            // Lấy toàn bộ data của city
                            String s = map.get("city").getClass().toString();
                            City city = getCity((HashMap<String, Object>) map.get("city"));


                            // lấy toàn bộ data của list
                            ArrayList<Object> arrayList = (ArrayList<Object>) map.get("list");
                            HashMap<String, ArrayList<DetailWeather>> detailWeatherHashMap = fetchTimeOfDate(arrayList);

// =======================================================================================================================================
                            // mỗi card view là một locate
//                            HashMap<String, HashMap<String, ArrayList<DetailWeather>>> weatherLocateHashMap;
//                            {
//                                weatherLocateHashMap   = new HashMap<>();
//
//                                weatherLocateHashMap.put(locate,detailWeatherHashMap);
//                            }
// =======================================================================================================================================


//
                            listOfWeathers.add(new ListOfWeather(city,detailWeatherHashMap));
//                            detailWeatherArrayList.add(detailWeatherHashMap.get("2019-10-13").get(0));
//                            detailWeatherArrayList.add(detailWeatherHashMap.get("2019-10-12").get(0));
//                            detailWeatherArrayList.add(detailWeatherHashMap.get("2019-10-11").get(0));
//                            detailWeatherArrayList.add(detailWeatherHashMap.get("2019-10-10").get(0));
//                            detailWeatherArrayList.add(detailWeatherHashMap.get("2019-10-09").get(0));

                                    detaiWeatherAdapter.notifyDataSetChanged();
                            int i = 0;

                        } else {
                            Log.d("error", "No document!");
                        }
                    }
                });





        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv_weather.setLayoutManager(linearLayoutManager);
        return view;
    }


    public City getCity(HashMap<String, Object> hashMap) {
        String country = "",
                id = "",
                name = "",
                sunrise = "",
                sunset = "",
                timezone = "",
                lon = "",
                lat = "";
        country = (String) hashMap.get("country");
        id = hashMap.get("id").toString();
        name = (String) hashMap.get("name");
        sunrise = hashMap.get("sunrise").toString();
        sunset = hashMap.get("sunset").toString();
        timezone = hashMap.get("timezone").toString();

        HashMap<String, Object> hashMap1 = new HashMap<>();
        hashMap1 = (HashMap<String, Object>) hashMap.get("coord");
        lon = hashMap1.get("lon").toString();
        lat = hashMap1.get("lat").toString();
        Coords coords = new Coords(lon, lat);

        return new City(coords, country, name, sunrise, sunset, timezone);
    }


    // Phân loại times theo từng date
    private HashMap<String, ArrayList<DetailWeather>> fetchTimeOfDate(ArrayList<Object> arrayList) {
        ArrayList<DetailWeather> detailWeatherList = new ArrayList<>(); // list chứa toàn bộ data của date
        for (int i = 0; i < arrayList.size(); i++) {
            detailWeatherList.add(getWeatherOfList((HashMap<String, Object>) arrayList.get(i)));
        }

        HashMap<String, ArrayList<DetailWeather>> detailWeatherHashMap = new HashMap<>(); // Hashmap chứa list time theo từng date
        ArrayList<DetailWeather> detailWeathers = new ArrayList<>();// list bao gồm toàn bộ times

        //  tạo giá trị cho detailWeatherHashMap mà có times theo từng date
        detailWeathers.add(detailWeatherList.get(0));
        String[] strings = detailWeatherList.get(0).getDt_txt().split(" ");
        detailWeatherHashMap.put(strings[0], detailWeathers);
        for (int i = 1; i < detailWeatherList.size(); i++) {
            strings = detailWeatherList.get(i).getDt_txt().split(" ");
            if (detailWeatherHashMap.get(strings[0]) == null) {
                detailWeathers = new ArrayList<>();
                detailWeathers.add(detailWeatherList.get(i));
            } else {

                detailWeathers = detailWeatherHashMap.get(strings[0]);
                detailWeathers.add(detailWeatherList.get(i));
            }
            detailWeatherHashMap.put(strings[0], detailWeathers);
        }


        return detailWeatherHashMap;
    }

    // Lấy toàn bộ data của list rồi trả về chi tiết của mỗi list
    public DetailWeather getWeatherOfList(HashMap<String, Object> hashMap) {

        String dt_txt = hashMap.get("dt_txt").toString();

        HashMap<String, Object> hashMapClouds = (HashMap<String, Object>) hashMap.get("clouds");
        String clouds = hashMapClouds.get("all").toString();

        HashMap<String, Object> hashMapRains = (HashMap<String, Object>) hashMap.get("rain");
        String rain = "0";
        if (hashMapRains != null) {
            rain = hashMapRains.get("3h").toString();
        }

        ArrayList<Object> arrayList2 = (ArrayList<Object>) hashMap.get("weather");
        HashMap<String, Object> hashMap1 = (HashMap<String, Object>) arrayList2.get(0);
        String des = (String) hashMap1.get("description");
        String icon = (String) hashMap1.get("icon");
        String id = (String) hashMap1.get("id").toString();
        String main = (String) hashMap1.get("main");
        Weathers weathers = new Weathers(des, icon, id, main);

        HashMap<String, Object> hashMapWinds = (HashMap<String, Object>) hashMap.get("wind");
        String deg = hashMapWinds.get("deg").toString();
        String speed = hashMapWinds.get("speed").toString();
        Winds winds = new Winds(deg, speed);

        HashMap<String, Object> hashMapMains = (HashMap<String, Object>) hashMap.get("main");
        String hum = hashMapMains.get("humidity").toString();
        String pre = hashMapMains.get("pressure").toString();
        String tem = hashMapMains.get("temp").toString();
        String tempMax = hashMapMains.get("temp_max").toString();
        String tempMin = hashMapMains.get("temp_min").toString();
        Mains mains = new Mains(hum, pre, tem, tempMax, tempMin);

        return new DetailWeather(dt_txt, clouds, rain, weathers, winds, mains);
    }

}
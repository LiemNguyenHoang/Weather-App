package com.example.wt.Fragment;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wt.Adapter.DetaiWeatherAdapter;
import com.example.wt.Adapter.DetailWeatherHistoryAdapter;
import com.example.wt.FetchRunnable;
import com.example.wt.MainActivity;
import com.example.wt.Model.City;
import com.example.wt.Model.DetailWeather;
import com.example.wt.Model.ListOfWeather;
import com.example.wt.Model.Mains;
import com.example.wt.Model.Weathers;
import com.example.wt.Model.Winds;
import com.example.wt.Parameter.Current.WeatherCurrent;
import com.example.wt.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 */
public class HistoryFragment extends Fragment {

    private DetailWeatherHistoryAdapter detaiWeatherHistoryAdapter;
    private ArrayList<DetailWeather> listOfWeathersHistory;
    FirebaseFirestore db;
    //    String date;
    int id;
    RecyclerView rv_weather_history;
    SwipeRefreshLayout swiperefresh;
    ProgressDialog progressDialog;
    TextView tvPositionHistory;


    public HistoryFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        swiperefresh = view.findViewById(R.id.swiperefreshHistory);
        rv_weather_history = view.findViewById(R.id.rv_weather_history);
        tvPositionHistory = view.findViewById(R.id.tvPositionHistory);
        listOfWeathersHistory = new ArrayList<>();

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Weather Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(true);
//        progressDialog.show();

        detaiWeatherHistoryAdapter = new DetailWeatherHistoryAdapter(getContext(), listOfWeathersHistory);
        showWeatherCurrent();
        rv_weather_history.setAdapter(detaiWeatherHistoryAdapter);

        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                listOfWeathersHistory.clear();
                detaiWeatherHistoryAdapter = new DetailWeatherHistoryAdapter(getContext(), listOfWeathersHistory);
                showWeatherCurrent();
                rv_weather_history.setAdapter(detaiWeatherHistoryAdapter);

                swiperefresh.setRefreshing(false);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv_weather_history.setLayoutManager(linearLayoutManager);
        return view;
    }

    private void showWeatherCurrent() {
        db = FirebaseFirestore.getInstance();

        db.collection(MainActivity.getID()).get()
                .addOnSuccessListener(
                        new OnSuccessListener<QuerySnapshot>() {
                            @Override
                            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                if (queryDocumentSnapshots.isEmpty()) {
                                    Log.d("error", "Collection is empty");
                                } else {
                                    // Start: Lấy date của locate
                                    ArrayList<Object> list = (ArrayList<Object>) queryDocumentSnapshots.toObjects(Object.class);
                                    if (list.size() > 1) {
                                        HashMap<Long, ArrayList<String>> hashMapTimeIdHistory = getTimeIdHistory(list);
                                        // End

                                        ArrayList<Long> listId = new ArrayList<>();
                                        for (Long key : hashMapTimeIdHistory.keySet()) {
                                            listId.add(key);
                                        }
                                        for (long id : listId) {
                                            ArrayList<String> listTime = new ArrayList<>();
                                            listTime = hashMapTimeIdHistory.get(id);
                                            for (String date : listTime) {
                                                String name = getNameLocate((int) id);

                                                getWeatherCurrentDate(name, date);
                                            }

                                        }
                                        int i = 0;
                                    } else {
                                        Toast.makeText(getContext(), "Không có History", Toast.LENGTH_LONG).show();
                                    }

//                                    progressDialog.dismiss();
                                }
                            }
                        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        //Toast.makeText(getActivity(),"Load history devices failure",Toast.LENGTH_SHORT).show();
//                        progressDialog.dismiss();
                        tvPositionHistory.setText("Load history devices failure");

                    }
                });


    }

    public DetailWeather getWeatherCurrent(HashMap<String, Object> hashMap, final String date, final String locate) {

        // time
        String dt_txt = date.split(" ")[0];

        // rain
        String rain = "0";
        if (hashMap.containsKey("rain")) {
            HashMap<String, Object> hashMapRains = (HashMap<String, Object>) hashMap.get("rain");

            if (hashMapRains != null) {
                rain = hashMapRains.get("3h").toString();
            }
        }
        //weather
        ArrayList<Object> arrayList2 = (ArrayList<Object>) hashMap.get("weather");
        HashMap<String, Object> hashMap1 = (HashMap<String, Object>) arrayList2.get(0);
        String des = (String) hashMap1.get("description");
        String icon = (String) hashMap1.get("icon");
        String id = (String) hashMap1.get("id").toString();
        String main = (String) hashMap1.get("main");
        Weathers weathers = new Weathers(des, icon, id, main);
        // wind
        HashMap<String, Object> hashMapWinds = (HashMap<String, Object>) hashMap.get("wind");
        String deg = hashMapWinds.get("deg").toString();
        String speed = hashMapWinds.get("speed").toString();
        Winds winds = new Winds(deg, speed);
        // temp
        HashMap<String, Object> hashMapMains = (HashMap<String, Object>) hashMap.get("main");
        String tem = hashMapMains.get("temp").toString();
        Mains mains = new Mains(tem);

        HashMap<String, Object> hashMapSys= (HashMap<String, Object>) hashMap.get("sys");
        String country = hashMapSys.get("country").toString();

        return new DetailWeather(dt_txt, rain, weathers, winds, mains, locate,country);
//        return new DetailWeather( );
    }

    // hashmap chứa time và id
    private HashMap<Long, ArrayList<String>> getTimeIdHistory(ArrayList<Object> list) {
        HashMap<Long, ArrayList<String>> hashMapLocate = new HashMap<>(); // chứa id và time

        for (int i = 1; i < list.size(); i++) {
            HashMap<String, Object> hashMap = (HashMap<String, Object>) list.get(i);
            ArrayList<String> listKey = new ArrayList<>();
            for (String key : hashMap.keySet()) {
                listKey.add(key);
            }
            hashMapLocate.put((Long) hashMap.get(listKey.get(0)), listKey);
        }
        return hashMapLocate;
    }

    private void getWeatherCurrentDate(final String locate, final String date) {
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Cities").document(locate).collection(date).document("current");
        docRef
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Map<String, Object> map = documentSnapshot.getData();
                            HashMap<String, Object> hashMap = new HashMap<>();

                            DetailWeather detailWeather = new DetailWeather();
                            detailWeather = getWeatherCurrent((HashMap<String, Object>) map, date, locate);

                            // Temp
                            int i = 0;
                            // Rain

                            // Wind
//                            long  wind = (HashMap)map.get("wind").get("speed");
//                            // Lấy toàn bộ data của city
//                            String s = map.get("city").getClass().toString();
//                            City city = getCity((HashMap<String, Object>) map.get("city"));
//
//                            // lấy toàn bộ data của list
//                            ArrayList<Object> arrayList = (ArrayList<Object>) map.get("list");
//                            HashMap<String, ArrayList<DetailWeather>> detailWeatherHashMap = fetchTimeOfDate(arrayList);
                            listOfWeathersHistory.add(detailWeather);
                            detaiWeatherHistoryAdapter.notifyDataSetChanged();
                        } else {
                            Log.d("error", "No document!");
                        }
                    }
                });
    }

    private String getNameLocate(int id) {
        FetchRunnable t = new FetchRunnable(id, getContext(), "weather");

        Thread thread = new Thread(t);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String json = t.getValue();

        WeatherCurrent weatherCurrent = new WeatherCurrent();
        weatherCurrent.fetchData(json);
        return weatherCurrent.getName();
    }

}

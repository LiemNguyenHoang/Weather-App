package com.example.wt.Fragment;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wt.Adapter.DetaiWeatherAdapter;
import com.example.wt.FetchRunnable;
import com.example.wt.MainActivity;
import com.example.wt.Model.City;
import com.example.wt.Model.Coords;
import com.example.wt.Model.DetailWeather;
import com.example.wt.Model.ListOfWeather;
import com.example.wt.Model.Mains;
import com.example.wt.Model.Weathers;
import com.example.wt.Model.Winds;
import com.example.wt.Parameter.Current.WeatherCurrent;
import com.example.wt.Parameter.Forecast.WeatherForecast;
import com.example.wt.R;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.jar.JarInputStream;

/**
 * A simple {@link Fragment} subclass.
 */
public class WeatherFragment extends Fragment {
    private DetaiWeatherAdapter detaiWeatherAdapter;
    private ArrayList<ListOfWeather> listOfWeathers;
    TextView tvId;
    FirebaseFirestore db;
    String idLocate, nameLocate;
    int id;
    RecyclerView rv_weather;
    ProgressDialog progressDialog;
    SwipeRefreshLayout swiperefresh;


    public WeatherFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             final Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_weather, container, false);
        tvId = view.findViewById(R.id.tvId);
        swiperefresh = view.findViewById(R.id.swiperefresh);
        rv_weather = view.findViewById(R.id.rv_weather);
        listOfWeathers = new ArrayList<>();

        // Start: Tạo Progress Dialog
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Weather Loading");
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(true);
        progressDialog.show();
        // End
        detaiWeatherAdapter = new DetaiWeatherAdapter(getContext(), listOfWeathers);
        // Nhận Latlon từ Map fragment
        if (getArguments() != null) {
            String valueLog = getArguments().getString("valueLog");
            String valueLat = getArguments().getString("valueLat");
            if (getArguments().getString("valueLog") != null && getArguments().getString("valueLat") != null) {
                db = FirebaseFirestore.getInstance();
                double lat = Double.parseDouble(valueLat);
                double lon = Double.parseDouble(valueLog);
                this.id = getIdLocate(lat, lon);
                UpdateLocateOfDevices();

//                listOfWeathers.clear();
//                rv_weather.setAdapter(null);
//                showWeatherLocate();
//                detaiWeatherAdapter.notifyDataSetChanged();
//                rv_weather.setAdapter(detaiWeatherAdapter);

            }
            if (getArguments().getString("idLocation") != null) {
                String id = getArguments().getString("idLocation");

                deleteLocation(id);
            }
            setArguments(null);
        }

        showWeatherLocate();
        rv_weather.setAdapter(detaiWeatherAdapter);


        swiperefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listOfWeathers = new ArrayList<>();
                detaiWeatherAdapter = new DetaiWeatherAdapter(getContext(), listOfWeathers);
                showWeatherLocate();
                rv_weather.setAdapter(detaiWeatherAdapter);
                swiperefresh.setRefreshing(false);
            }
        });

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rv_weather.setLayoutManager(linearLayoutManager);
        return view;
    }

    public void deleteLocation(String id) {
        db = FirebaseFirestore.getInstance();
        db.collection(MainActivity.getID())
                .document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("data", "Delete location id devie successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("data", "Delete location id devie failure!", e);
                    }
                });
    }

    public ArrayList<Long> getIdLocateUser(ArrayList<Object> list) {
        HashMap<Long, ArrayList<String>> hashMapLocate = new HashMap<>(); // chứa id và time

        for (int i = 1; i < list.size(); i++) { // bỏ qua vị trí đầu tiên vì bị null
            HashMap<String, Object> hashMap = (HashMap<String, Object>) list.get(i);
            ArrayList<String> listKey = new ArrayList<>();
            for (String key : hashMap.keySet()) {
                listKey.add(key);
            }
            hashMapLocate.put((Long) hashMap.get(listKey.get(0)), listKey);
        }

        // End
        // Start: lấy list locate
        ArrayList<Long> listIdLocate = new ArrayList<>();
        for (Long string : hashMapLocate.keySet()) {
            listIdLocate.add(string);
        }
        // End
        return listIdLocate;
    }

    // Start: Lấy data weather forecast dạng Json từ api của openweathermap.org
    public String RequestAPIForecast(final int id) {
        FetchRunnable t = new FetchRunnable(id, getContext(), "forecast");

        Thread thread = new Thread(t);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return t.getValue();
    }

    // Start: Lấy data weather current dạng Json từ api của openweathermap.org
    public String RequestAPICurrent(final int id) {
        FetchRunnable t = new FetchRunnable(id, getContext(), "weather");

        Thread thread = new Thread(t);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return t.getValue();
    }

    // Start: Lấy tên của locate bằng LatLog (s/d openweather loglat)
    public int getIdLocate(double lat, double lon) {
        FetchRunnable t = new FetchRunnable(lat, lon, getContext(), "LatLng");

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
        return weatherCurrent.getId();
    }
    // End

    // Start: Cập nhật lại list locate trong id devices
    public void UpdateLocateOfDevices() {
        db = FirebaseFirestore.getInstance();


        db.collection(MainActivity.getID())
                .document(id + "")
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            Map<String, Object> hashMap = new HashMap<>();
                            int size = 0;
                            if (task.getResult().getData() == null) {
                                {
                                    Map<String, String> map = new HashMap<>();
                                    map.put("0", "");
                                    db.collection(MainActivity.getID())
                                            .document(String.valueOf(id))
                                            .set(map);
                                }

                            } else {
                                hashMap = task.getResult().getData();
                                size = hashMap.size();
                            }


                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                            final String time = sdf.format(new Date());

                            db.collection(MainActivity.getID())
                                    .document(String.valueOf(id))
                                    .update(time, "");
                        }


                    }
                });
    }

    // End
    public String getNameLocate(int id) {
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


    public void insertForecast(int id, String time) {
        WeatherForecast weatherForecast = new WeatherForecast();
        db = FirebaseFirestore.getInstance();
        String jsonForecast = null;
        String location = getNameLocate(id);
        jsonForecast = RequestAPIForecast(id);
        weatherForecast.fetchData(jsonForecast);

        Map<String, Object> hashMap = new HashMap<>();

        hashMap.put("city", weatherForecast.getCityForecast());
        hashMap.put("cnt", weatherForecast.getCnt());
        hashMap.put("cod", weatherForecast.getCod());
        hashMap.put("list", weatherForecast.getListForecast());

        Map<String, Object> hashMap1 = new HashMap<>();
        hashMap1.put("id", weatherForecast.getCityForecast().getId());
        db.collection("Cities")
                .document(location)
                .set(hashMap1);
        db.collection("Cities")
                .document(location)
                .collection(time)
                .document("forecast")
                .set(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("data", "Insert forecast complete");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("data", "Insert forecast error");
                    }
                });
    }

    public void insertIdLocateOfDevice(ArrayList<Object> arrayList) {
        db = FirebaseFirestore.getInstance();

        for (int i = 0; i < arrayList.size(); i++) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap = (HashMap<String, String>) arrayList.get(i);

            db.collection(MainActivity.getID())
                    .document(i + "")
                    .set(hashMap)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d("data", "Insert locate of device success");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Log.d("data", "Insert locate of device success");
                        }
                    });
        }
    }

    public void insertCurrent(int id, String time) {
        WeatherCurrent weatherCurrent = new WeatherCurrent();
        db = FirebaseFirestore.getInstance();
        String jsonCurrent = null;
        String location = getNameLocate(id);
        jsonCurrent = RequestAPICurrent(id);
        weatherCurrent.fetchData(jsonCurrent);

        Map<String, Object> hashMap = new HashMap<>();

        hashMap.put("base", weatherCurrent.getBase());
        hashMap.put("clouds", weatherCurrent.getClouds());
        hashMap.put("cod", weatherCurrent.getCod());
        hashMap.put("coord", weatherCurrent.getCoord());
        hashMap.put("dt", weatherCurrent.getDt());
        hashMap.put("id", weatherCurrent.getId());
        hashMap.put("main", weatherCurrent.getMain());
        hashMap.put("name", weatherCurrent.getName());
        hashMap.put("rain", weatherCurrent.getRain());
        hashMap.put("sys", weatherCurrent.getSys());
        hashMap.put("timezone", weatherCurrent.getTimezone());
        hashMap.put("weather", weatherCurrent.getWeather());
        hashMap.put("wind", weatherCurrent.getWind());

        Map<String, Object> hashMap1 = new HashMap<>();
        hashMap1.put("id", weatherCurrent.getId());
        db.collection("Cities")
                .document(location)
                .set(hashMap1);
        db.collection("Cities")
                .document(location)
                .collection(time)
                .document("current")
                .set(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("data", "Insert current complete");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("data", "Insert current error");
                    }
                });
    }

    public void showWeatherLocate() {
        db = FirebaseFirestore.getInstance();
        CollectionReference colRef = db.collection(MainActivity.getID());

        colRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Long> listIdLocate = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                listIdLocate.add(Long.parseLong(document.getId()));
                            }

                            int size = listIdLocate.size();
                            if (listIdLocate.size() > 1) {

//                                for (int id : listIdLocate) {
                                for (int i = 1; i < listIdLocate.size(); i++) {
                                    String a = String.valueOf(listIdLocate.get(i));
                                    int  id = Integer.parseInt(a);
                                    idLocate = String.valueOf(id);
                                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                                    String time = sdf.format(new Date());

                                    String name = getNameLocate(id);
                                    nameLocate = name;

                                    insertDevice(id,time);
                                    insertCurrent(id,time);
                                    insertForecast(id,time);

                                    getWeather(name,time);

                                }

                                progressDialog.dismiss();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(getContext(), "Not data history", Toast.LENGTH_LONG).show();
                            }


                            int i = 0;
                        } else {
                            Log.d("data", "Error getting document id devices: ", task.getException());
                        }
                    }
                });

        // OK
//        colRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                if (queryDocumentSnapshots.isEmpty()) {
//                    Log.d("error", "Collection is empty");
//                } else {
//                    // Start: Lấy date của locate
//                    ArrayList<Object> list = (ArrayList<Object>) queryDocumentSnapshots.toObjects(Object.class);
//                    ArrayList<Long> listIdLocate = new ArrayList<>();
//                    if (list.size() > 1) { //   có history của devices
//                        listIdLocate = getIdLocateUser(list);
//                        for (long id : listIdLocate) {
//                            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
//                            String time = sdf.format(new Date());
//
//                            String name = getNameLocate((int) id);
//                            insertCurrent((int) id, time);
//                            insertForecast((int) id, time);
//
//                            getWeather(name, time);
//
//                        }
//
////                        progressDialog.dismiss();
//                    } else {
//                        Toast.makeText(getContext(), "Không có History", Toast.LENGTH_LONG).show();
//                    }
//                }
//            }
//        });
    }

    private void insertDevice(int id, String time) {
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put(time, "");

        db = FirebaseFirestore.getInstance();
        db.collection(MainActivity.getID())
                .document(id + "")
                .update(hashMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Log.d("data", "Update time according to id device");
                    }
                });
    }


    public void getWeather(String locate, String date) {
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Cities").document(locate).collection(date).document("forecast");
        docRef
                .get()
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
                            HashMap<String, ArrayList<DetailWeather>> detailWeatherHashMap = fetchTimeOfDate(arrayList, city);
                            listOfWeathers.add(new ListOfWeather(city, detailWeatherHashMap));
                            detaiWeatherAdapter.notifyDataSetChanged();
                        } else {
                            Log.d("error", "No document!");
                        }
                    }
                });
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

        return new City(coords, country, name, sunrise, sunset, timezone, id);
    }


    // Phân loại times theo từng date
    private HashMap<String, ArrayList<DetailWeather>> fetchTimeOfDate(ArrayList<Object> arrayList, City city) {
        ArrayList<DetailWeather> detailWeatherList = new ArrayList<>(); // list chứa toàn bộ data của date
        for (int i = 0; i < arrayList.size(); i++) {
            detailWeatherList.add(getWeatherOfList((HashMap<String, Object>) arrayList.get(i), city));
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
    public DetailWeather getWeatherOfList(HashMap<String, Object> hashMap, City city) {

        String dt_txt = hashMap.get("dt_txt").toString();

        HashMap<String, Object> hashMapClouds = (HashMap<String, Object>) hashMap.get("clouds");
        String clouds = hashMapClouds.get("all").toString();

        String rain = "0";
        if (hashMap.containsKey("rain")) {
            HashMap<String, Object> hashMapRains = (HashMap<String, Object>) hashMap.get("rain");

            if (hashMapRains != null) {
                rain = hashMapRains.get("3h").toString();
            }
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

        String country = city.getCountry();

        return new DetailWeather(dt_txt, clouds, rain, weathers, winds, mains, country, id);
    }

}
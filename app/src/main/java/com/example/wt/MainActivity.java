package com.example.wt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wt.Adapter.ViewPagerAdapter;
import com.example.wt.Fragment.MapFragment;
import com.example.wt.Fragment.HistoryFragment;
import com.example.wt.Fragment.WeatherFragment;
import com.example.wt.Model.City;
import com.example.wt.Model.Coords;
import com.example.wt.Model.DetailWeather;
import com.example.wt.Model.Mains;
import com.example.wt.Model.Weathers;
import com.example.wt.Model.Winds;
import com.example.wt.Parameter.Current.WeatherCurrent;
import com.example.wt.Parameter.Forecast.WeatherForecast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    public static final String KEY_API = "4075de6ade4bc3ff857acc98a3d8395c";
    private static String ID;
    City city;
    HashMap<String, ArrayList<DetailWeather>> detailWeatherHashMap;
    TabLayout tabLayout;
    NonSwipeableViewPager viewPager;
    Toolbar toolbar;
    FirebaseFirestore db;

    public ArrayList<String> getLocate(ArrayList<Object> list) {
        HashMap<String, ArrayList<String>> hashMapLocate = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            HashMap<String, Object> hashMap = (HashMap<String, Object>) list.get(i);
            ArrayList<String> listKey = new ArrayList<>();
            for (String key : hashMap.keySet()) {
                listKey.add(key);
            }
            hashMapLocate.put((String) hashMap.get(listKey.get(0)), listKey);
        }
        // End
        // Start: lấy list locate
        ArrayList<String> listLocate = new ArrayList<>();
        for (String string : hashMapLocate.keySet()) {
            listLocate.add(string);
        }
        // End

        return listLocate;
    }


    //End
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Start: Access data offline
        db = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .build();
        db.setFirestoreSettings(settings);
        // End

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_paper);
        toolbar = findViewById(R.id.toolbar);
        boolean flag = true;
//        while (true) {
            flag = isNetworkAvailable();
//            if (flag) {
//                break;
//            } else {
//                try {
//                    Toast.makeText(this, "No Internet, please connect internet!!!", Toast.LENGTH_SHORT).show();
//                    Thread.sleep(1000);
//                    Log.d("data", "Internet failer");
//
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }

        if (flag) { // có internet
            this.ID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
//        this.ID = "12345";
            // Kiểm tra có id devices không
            isHistory();


//            setSupportActionBar(toolbar);


            WeatherFragment weatherFragment = new WeatherFragment();
            HistoryFragment historyFragment = new HistoryFragment();
            MapFragment mapFragment = new MapFragment();


            ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);

            viewPagerAdapter.AddFragment(weatherFragment, "WEATHER");
            viewPagerAdapter.AddFragment(historyFragment, "HISTORY");
            viewPagerAdapter.AddFragment(mapFragment, "MAP");

            // Start: send lon lat lấy được từ Map fragment
            Intent intent = getIntent();
            String keyLog = intent.getStringExtra("keyLog"),
                    keyLat = intent.getStringExtra("keyLat");
            if (intent != null) {
                Bundle bundle = new Bundle();
                bundle.putString("valueLog", keyLog);
                bundle.putString("valueLat", keyLat);
                weatherFragment.setArguments(bundle);
            }
            // End


            viewPager.setAdapter(viewPagerAdapter);
            tabLayout.setupWithViewPager(viewPager);
        }
        else{
            Toast.makeText(this, "No Internet, please connect internet!!!", Toast.LENGTH_LONG).show();
            finish();
        }


    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
//        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
////        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
//        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
        boolean haveConectedWifi = false;
        boolean haveConectedMobile = false;
        NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
        HashMap<String, String> hashMap = new HashMap<>();
        for (NetworkInfo ni : networkInfos) {
            String name = ni.getTypeName();
            String state = ni.getState().toString();
            hashMap.put(name, state);
//            if (ni.getTypeName().equalsIgnoreCase("WIFI")) {
//                haveConectedWifi = true;
//                if (ni.getTypeName().equalsIgnoreCase("MOBILE")) {
//                    haveConectedMobile = true;
//                }
//            }

        }
        if (hashMap.get("MOBILE").equals("CONNECTED") || hashMap.get("WIFI").equals("CONNECTED")) {
            return true;
        }
        return false;
    }


    public void isHistory() {
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection(MainActivity.getID()).document("0");
        docRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot.exists()) {
                                Log.d("data", " DocumentSnapshot data: " + documentSnapshot.getData());

                            } else { // không có data
                                Log.d("data", "No such document");

                                db = FirebaseFirestore.getInstance();
                                HashMap<String, Integer> hashMap = new HashMap<>();
                                hashMap.put("0", 0);
                                db.collection(MainActivity.getID()).document("0").set(hashMap);


                            }
                        } else {
                            Log.d("data", "Go failed with" + task.getException());
                        }
                    }
                });
    }

    public static String getID() {
        return ID;
    }

    public void getArrayWeather(String locate, String date) {
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

                            int i = 0;

                            setData(city, detailWeatherHashMap);
                        } else {
                            Log.d("error", "No document!");
                        }
                    }
                });


    }

    private void setData(City
                                 city, HashMap<String, ArrayList<DetailWeather>> detailWeatherHashMap) {
        this.city = city;
        this.detailWeatherHashMap = detailWeatherHashMap;
    }


    // Phân loại times theo từng date
    private HashMap<String, ArrayList<DetailWeather>> fetchTimeOfDate
    (ArrayList<Object> arrayList) {
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
        String rain = "";
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

    // Lấy toàn bộ data của city
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


}

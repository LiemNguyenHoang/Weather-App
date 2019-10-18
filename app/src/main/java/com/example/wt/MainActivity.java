package com.example.wt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;

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
    City city;
    HashMap<String, ArrayList<DetailWeather>> detailWeatherHashMap;

    TabLayout tabLayout;
    ViewPager viewPager;
    Toolbar toolbar;
    FirebaseFirestore db;

    // Start: Lấy data weather forecast dạng Json từ api của openweathermap.org
    public String RequestAPIForecast(final int id) {
        FetchRunnable t = new FetchRunnable(id, this, "forecast");

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
        FetchRunnable t = new FetchRunnable(id, this, "weather");

        Thread thread = new Thread(t);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return t.getValue();
    }

    // Start: cập nhật và insert weather forecast
    public void updateForecast(String locate, String time) {
        WeatherForecast weatherForecast = new WeatherForecast();
        db = FirebaseFirestore.getInstance();
        String json = null;
        json = RequestAPIForecast(1567788);
        weatherForecast.fetchData(json);

        Map<String, Object> hashMap = new HashMap<>();

        hashMap.put("city", weatherForecast.getCityForecast());
        hashMap.put("cnt", weatherForecast.getCnt());
        hashMap.put("cod", weatherForecast.getCod());
        hashMap.put("list", weatherForecast.getListForecast());

        db.collection("Cities")
                .document(locate)
                .collection(time)
                .document("forecast")
                .update(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("data", "Written complete");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("data", "written error");
                    }
                });
    }

    public void insertForecast(String locate, String time) {
        WeatherForecast weatherForecast = new WeatherForecast();
        db = FirebaseFirestore.getInstance();
        String jsonForecast = null;
        jsonForecast = RequestAPIForecast(1567788);
        weatherForecast.fetchData(jsonForecast);

        Map<String, Object> hashMap = new HashMap<>();

        hashMap.put("city", weatherForecast.getCityForecast());
        hashMap.put("cnt", weatherForecast.getCnt());
        hashMap.put("cod", weatherForecast.getCod());
        hashMap.put("list", weatherForecast.getListForecast());

        Map<String, Object> hashMap1 = new HashMap<>();
        hashMap1.put("id", "12345");
        db.collection("Cities")
                .document(locate)
                .set(hashMap1);
        db.collection("Cities")
                .document(locate)
                .collection(time)
                .document("forecast")
                .set(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("data", "Written complete");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("data", "written error");
                    }
                });
    }
    // End

    // Start: cập nhật và insert weather current
    public void updateCurrent(String locate, String time) {
        WeatherCurrent weatherCurrent = new WeatherCurrent();
        db = FirebaseFirestore.getInstance();
        String json = null;
        json = RequestAPICurrent(1567788);
        weatherCurrent.fetchData(json);

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

        db.collection("Cities")
                .document(locate)
                .collection(time)
                .document("current")
                .update(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("data", "Written complete");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("data", "written error");
                    }
                });
    }

    public void insertCurrent(String locate, String time) {
        WeatherCurrent weatherCurrent = new WeatherCurrent();
        db = FirebaseFirestore.getInstance();
        String jsonCurrent = null;
        jsonCurrent = RequestAPICurrent(1567788);
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
        hashMap1.put("id", "12345");
        db.collection("Cities")
                .document(locate)
                .set(hashMap1);
        db.collection("Cities")
                .document(locate)
                .collection(time)
                .document("current")
                .set(hashMap)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("data", "Written complete");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("data", "written error");
                    }
                });
    }

    // End
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

    public void updateWeatherLocate() {
        boolean flag = false;
        CollectionReference colRef = null;
        try {
            colRef = db.collection("12345");
        } catch (Exception e) {
            flag = true;
        }

        int i = 0;
        colRef.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                if (queryDocumentSnapshots.isEmpty()) {
                    Log.d("error", "Collection is empty");
                } else {
                    // Start: Lấy date của locate
                    ArrayList<Object> list = (ArrayList<Object>) queryDocumentSnapshots.toObjects(Object.class);
                    ArrayList<String> listLocate = getLocate(list);
                    // End
                    int iasd = 0;
                    for (String locate : listLocate) {
                        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                        String time = sdf.format(new Date());
                        boolean flag = false;
                        try {
                            db.collection("Cities").document(locate).collection(time);
                        } catch (Exception e) {
                            flag = true;
                            e.printStackTrace();
                        }
                        if (flag) {
                            insertForecast(locate, time);
                            insertCurrent(locate,time);
                        } else {
                            insertForecast(locate, time);
                            insertCurrent(locate,time);
                        }





                    }
                    int i = 0;


                }
            }
        });

    }

    //End
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        // Lấy locate cửa từng user trong Users

//        ArrayList<String> arrayListLocate = new ArrayList<>();
//        arrayListLocate = getArrayLocate("12345");
//        for(String locate:arrayListLocate){
        db = FirebaseFirestore.getInstance();
            updateWeatherLocate(); // update/ insert weather lên firebase
        // Lấy id của locate


        // Lấy weather theo time
//        String locate = "Bình Phước",
//                date = "9-10-2019";
//        getArrayWeather(locate, date);
//        int i = 0;

        // End

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_paper);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.AddFragment(new WeatherFragment(), "WEATHER");
        viewPagerAdapter.AddFragment(new HistoryFragment(), "HISTORY");
        viewPagerAdapter.AddFragment(new MapFragment(), "MAP");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);


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

    private void setData(City city, HashMap<String, ArrayList<DetailWeather>> detailWeatherHashMap) {
        this.city = city;
        this.detailWeatherHashMap = detailWeatherHashMap;
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

        return new City(coords, country, name, sunrise, sunset, timezone);
    }
}

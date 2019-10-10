package com.example.wt;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.example.wt.Adapter.ViewPagerAdapter;
import com.example.wt.Fragment.DailyFragment;
import com.example.wt.Fragment.HourlyFragment;
import com.example.wt.Fragment.NowFragment;
import com.example.wt.Model.ListOfWeather;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    // HashMap chứa ArrayList của HashMap khác
    HashMap<String, ArrayList<HashMap<String, Object>>> hashMapWeather;

    TabLayout tabLayout;
    ViewPager viewPager;
    Toolbar toolbar;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = FirebaseFirestore.getInstance();

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_paper);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), 0);
        viewPagerAdapter.AddFragment(new NowFragment(), "WEATHER");
        viewPagerAdapter.AddFragment(new HourlyFragment(), "HISTORY");
        viewPagerAdapter.AddFragment(new DailyFragment(), "MAP");

        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        // Lấy locate cửa từng user trong Users
        /*
        ArrayList<String> arrayListLocate = new ArrayList<>();
        arrayListLocate = getArrayLocate("12345");
        */

        // Lấy weather theo time
        String locate = "Bình Phước",
                date = "9-10-2019";
        getArrayWeather(locate, date);
    }

    public ArrayList<String> getArrayLocate(String id) {
        db.collection("Users").document(id)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("A", "Listen failed.", e);
                            return;
                        }
                        Map<String, Object> map = documentSnapshot.getData(); // lưu date vào mảng
                        Log.d("asd", map.get("1").toString());
                        String s = map.get("1").toString();
                        if (documentSnapshot != null && documentSnapshot.exists()) {
                            Log.d("A", "Current data: " + documentSnapshot.getData());
                        } else {
                            Log.d("A", "Current data: null");
                        }
                    }
                });

        return null;
    }

    public void getArrayWeather(String locate, String date) {
        db.collection("Cities").document(locate).collection(date).document("forecast")
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                        Map<String, Object> map = documentSnapshot.getData();

                        Map<String, Object> weathersMap = new HashMap<>();
                        // lấy toàn bộ data của list
                        ArrayList<ListOfWeather> arrayList = (ArrayList<ListOfWeather>) map.get("list");
                        fetchTimeOfDate(arrayList);

                    }
                });

    }
    // Phân loại times theo từng date
    private void fetchTimeOfDate(ArrayList<ListOfWeather> arrayList) {
        HashMap<String, Object> hashMap = null; // tạm chứa weather của từng arrayList
        ArrayList<HashMap<String, Object>> arrayList1 = new ArrayList<>();
        hashMapWeather = new HashMap<>();
        String[] strings = null;

        // xử lý arrayList(0)
        for (int i = 0; i < arrayList.size(); i++) {
            hashMap = arrayList.get(i);
            strings = hashMap.get("dt_txt").toString().split(" ");
            hashMap.put("dt_txt", strings[1]);
            arrayList1 = hashMapWeather.get(strings[0]);
            if (hashMapWeather.get(strings[0]) == null) {
                arrayList1 = new ArrayList<>();
            }
            arrayList1.add(hashMap);
            hashMapWeather.put(strings[0], arrayList1);
        }

    }
}

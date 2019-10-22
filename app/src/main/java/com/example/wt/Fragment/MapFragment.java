package com.example.wt.Fragment;


import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wt.FetchRunnable;
import com.example.wt.MainActivity;
import com.example.wt.Parameter.Current.WeatherCurrent;
import com.example.wt.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import static android.content.Context.LOCATION_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends Fragment implements OnMapReadyCallback {
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private static final int DEFAULT_ZOOM = 15;
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final String TAG = "data";
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;
    private Button btnLoglat;
    private TextView tvLon;
    private TextView tvLat;
    private boolean firstTime;

    private boolean mLocationPermissionGranted;
    private Location mLastKnownLocation;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private Marker currentMarker;


//    private void getLocationPermission() {
//        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            mLocationPermissionGranted = true;
//        } else {
//            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
//        }
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
                break;
            }
        }
    }

//    private void updateLocationUI() {
//        if (mMap == null)
//            return;
//        try {
//            if (mLocationPermissionGranted) {
//                mMap.setMyLocationEnabled(true);
//                mMap.getUiSettings().setMyLocationButtonEnabled(true);
//                mMap.getUiSettings().setZoomControlsEnabled(true);
//                mMap.setMyLocationEnabled(true);
//                mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
//                    @Override
//                    public boolean onMyLocationButtonClick() {
//                        return false;
//                    }
//                });
//                mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//                    @Override
//                    public void onMapClick(LatLng latLng) {
//
//                        mMap.addMarker(new MarkerOptions()
//                                .position(latLng)
//                                .title("Here"));
//                    }
//                });
//                mLastKnownLocation = null;
//                getLocationPermission();
//            }
//
//        } catch (SecurityException e) {
//            Log.e("Exception %s", e.getMessage());
//        }
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
            firstTime = true;
        btnLoglat = view.findViewById(R.id.btnLoglat);
        tvLon = view.findViewById(R.id.tvLon);
        tvLon.setText("0.0");
        tvLat = view.findViewById(R.id.tvLat);
        tvLat.setText("0.0");

        if (mLocationPermissionGranted) {
            Log.d("data", "Permission OK");
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);
        } else {

            Log.d("data", "Permission Failer");
            Toast.makeText(getContext(), "Permission Failer", Toast.LENGTH_LONG).show();
        }

//        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        currentMarker = null;

        int checkPermissionCoarseLocaltion = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        int checkPermissionFineLocation = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        if (checkPermissionCoarseLocaltion != PackageManager.PERMISSION_GRANTED && checkPermissionCoarseLocaltion != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            mLocationPermissionGranted = true;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        if (mLocationPermissionGranted) { // được cấp permission


            this.mMap = googleMap;
            this.mMap.getUiSettings().setZoomControlsEnabled(true);
            this.mMap.setMyLocationEnabled(true);
            this.mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                @Override
                public boolean onMyLocationButtonClick() {
//                    if(firstTime==true){
//                        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
//                            @Override
//                            public boolean onMyLocationButtonClick() {
//                                return false;
//                            }
//                        });
//                    }

                    LatLng latLng1 = mMap.getCameraPosition().target;
//                    mMap.;

                    firstTime = false;
                    LatLng latLng = mMap.getCameraPosition().target;

                    int i = 0;
                    // Start: Check lon lat có lấy được weather
                    double lat = latLng.latitude;
                    double lon = latLng.longitude;
//                int id = getIdLocate(lat,lon);
//                String name = getNameLocate(id);
//                    if(lat!=0.0&&lon!=0.0){
//
                    tvLat.setText(lat + "");
                    tvLon.setText(lon + "");
//                    }
                    return false;
                }
            });
            this.mMap.setOnMyLocationClickListener(new GoogleMap.OnMyLocationClickListener() {
                @Override
                public void onMyLocationClick(@NonNull Location location) {
                    Toast.makeText(getContext(), location.getLatitude() + ":" + location.getLongitude(), Toast.LENGTH_SHORT).show();

                    if (currentMarker != null) {
                        currentMarker.remove();
                    }
                    int i = 0;
                    currentMarker = mMap.addMarker(new MarkerOptions()
                            .title("Here")
                            // Sets the zoom
//                        .bearing(90)
                            .position(new LatLng(location.getLatitude(), location.getLongitude())));

                    currentMarker.showInfoWindow();

                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(new LatLng(location.getLatitude(), location.getLongitude()))             // Sets the center of the map to location user
                            .zoom(10)                   // Sets the zoom
//                        .bearing(90)                // Sets the orientation of the camera to east
//                        .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                            .build();                   // Creates a CameraPosition from the builder
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                }
            });
            this.mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    tvLat.setText(String.valueOf(latLng.latitude));
                    tvLon.setText(String.valueOf(latLng.longitude));
                    Toast.makeText(getContext(), latLng.latitude + ":" + latLng.longitude, Toast.LENGTH_SHORT).show();
                    if (currentMarker != null) {
                        currentMarker.remove();
                    }
                    currentMarker = mMap.addMarker(new MarkerOptions()
                            .title("Here")
                            // Sets the zoom
//                        .bearing(90)
                            .position(latLng));


                    CameraPosition cameraPosition = new CameraPosition.Builder()
                            .target(latLng)             // Sets the center of the map to location user
                            .zoom(10)                   // Sets the zoom
//                        .bearing(90)                // Sets the orientation of the camera to east
//                        .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                            .build();                   // Creates a CameraPosition from the builder
                    mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

                    currentMarker.showInfoWindow();
                }
            });
            btnLoglat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Start: Check lon lat có lấy được weather
                    double lat = Double.parseDouble(tvLat.getText().toString());
                    double lon = Double.parseDouble(tvLon.getText().toString());

                    if ((tvLat.getText().toString().equals("0.0")==false) && (tvLon.getText().toString().equals("0.0")==false)) {


                    }
                    boolean resultLocation = checkLocation(lat, lon);
                    // End
                    if (resultLocation == false) {
                        Toast.makeText(getContext(), "Failed to get the weather !!!", Toast.LENGTH_SHORT).show();

                    } else {

                        boolean b3 = (tvLat.getText().toString().equals("0.0") == true && tvLon.getText().toString().equals("0.0") == true);

                        if ((tvLat.getText().toString().equals("0.0")==false) && (tvLon.getText().toString().equals("0.0")==false)) {
                            Intent intent = new Intent(getActivity().getBaseContext(),
                                    MainActivity.class);
                            intent.putExtra("keyLog", tvLon.getText());
                            intent.putExtra("keyLat", tvLat.getText());
                            getActivity().startActivity(intent);

                            Toast.makeText(getContext(), tvLon.getText() + "|" + tvLat.getText(), Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "Failed to get location !!!", Toast.LENGTH_SHORT).show();
                        }
                    }

                }
            });
        }


//        this.mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//            @Override
//            public void onMapClick(LatLng latLng) {
//
//            }
//        });

//        LatLng sydney = new LatLng(-33.852, 151.211);
//        googleMap.addMarker(new MarkerOptions().position(sydney)
//                .title("Marker in Sydney"));
//        googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public boolean checkLocation(double lat, double lon) {
        FetchRunnable t = new FetchRunnable(lat, lon, getContext(), "LatLng");

        Thread thread = new Thread(t);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        String json = t.getValue();
        int id = 0;
        String name = "";
        JSONObject jsonRoot = null;
        try {
            jsonRoot = new JSONObject(json);
            id = jsonRoot.getInt("id");
            name = jsonRoot.getString("main");
            if (name.equals("") || id == 0) {
                return false;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return true;
    }

//    private void getDeviceLocation() {
//        try {
//            if (mLocationPermissionGranted) {
//                Task locationResult = mFusedLocationProviderClient.getLastLocation();
//                locationResult.addOnCompleteListener(new OnCompleteListener() {
//                    @Override
//                    public void onComplete(@NonNull Task task) {
//                        if (task.isSuccessful()) {
//                            mLastKnownLocation = (Location) task.getResult();
//                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(mLastKnownLocation.getLatitude(),
//                                    mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
//                        } else {
//                            Log.d(TAG, "Current location is null. Using defaults.");
//                            Log.e(TAG, "Exception: %s", task.getException());
//                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
//                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
//                        }
//                    }
//                });
//            }
//
//        } catch (SecurityException e) {
//            Log.e("Exception %s", e.getMessage());
//        }
//    }
//
//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//        super.onCreateOptionsMenu(menu, inflater);
//        inflater.inflate(R.menu.current_place_menu, menu);
//    }


//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == R.id.option_get_place) {
//            showCurrentPlace();
//        }
//        return true;
//    }

//    private void showCurrentPlace() {
//        if (mMap == null) {
//            return;
//        }
//        if (mLocationPermissionGranted) {
//            // Start: Show current  place
//            //  get a list of likely places at the device's current location
//
//            // define the data types to return
////            List<Place.Field> placeFields = Collections.singletonList(Place.Field.NAME);
//////
//////            FindCurrentPlaceRequest request =
//////                    FindCurrentPlaceRequest.newInstance(placeFields);
//////
//////
//////            if(ContextCompat.checkSelfPermission(getContext(), String.valueOf(PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION))==PackageManager.PERMISSION_GRANTED){
//////                Task<FindCurrentPlaceResponse> placeResponse
//////               =
////            }
//            // End
//        } else {
//            Log.i(TAG, "The user did not grant location permission.");
//            mMap.addMarker(new MarkerOptions()
//                    .title(getString(R.string.default_info_title))
//                    .position(mDefaultLocation)
//                    .snippet(getString(R.string.default_info_snippet)));
//
//            // Prompt the user for permission.
//            getLocationPermission();
//        }
//
//
//    }
}



/*

public class MapFragment extends Fragment implements LocationListener, OnMapReadyCallback {

    public MapFragment() {
        // Required empty public constructor
    }

    public static final int REQUEST_ID_ACCESS_COURSE_FINE_LOCATION = 100;
    public static final int AUTOCOMPLETE_REQUEST_CODE = 1;
    private static final String HERE = "HERE";
    GoogleMap myMap;
    SupportMapFragment mapFragment;
    TextView tvLog, tvLat;
    Marker currentMarker;
    ImageButton btnLoglat;
//    ProgressDialog myProgress;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        currentMarker = null;
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        tvLog = view.findViewById(R.id.tv_log);
        tvLat = view.findViewById(R.id.tv_lat);
        btnLoglat = view.findViewById(R.id.btnLoglat);
        // Tạo Progress Bar
//        myProgress = new ProgressDialog(getContext());
//        myProgress.setTitle("Map Loading ...");
//        myProgress.setMessage("Please wait...");
//        myProgress.setCancelable(true);
//        // Hiển thị Progress Bar
//        myProgress.show();

//        if (mapFragment == null) {
//            mapFragment = SupportMapFragment.newInstance();
//        }

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                onMyMapReady(googleMap);
//                    googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
//                        @Override
//                        public void onMapClick(LatLng latLng) {
//                            tvLog.setText(String.valueOf(latLng.longitude));
//                            tvLat.setText(String.valueOf(latLng.latitude));
//                        }
//                    });
            }
        });


        getChildFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();
        return view;
    }

    private void onMyMapReady(GoogleMap googleMap) {
        // Lấy đối tượng Google Map ra:
        myMap = googleMap;

        // Thiết lập sự kiện đã tải Map thành công
        myMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {

            @Override
            public void onMapLoaded() {

                // Đã tải thành công thì tắt Dialog Progress đi
//                myProgress.dismiss();

                // Hiển thị vị trí người dùng.
                askPermissionsAndShowMyLocation();
            }
        });
        myMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        myMap.getUiSettings().setZoomControlsEnabled(true);
        myMap.setMyLocationEnabled(true);

    }

    private void askPermissionsAndShowMyLocation() {


        // Với API >= 23, bạn phải hỏi người dùng cho phép xem vị trí của họ.
        if (Build.VERSION.SDK_INT >= 23) {
            int accessCoarsePermission
                    = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
            int accessFinePermission
                    = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);

            if (accessCoarsePermission != PackageManager.PERMISSION_GRANTED
                    || accessFinePermission != PackageManager.PERMISSION_GRANTED) {

                // Các quyền cần người dùng cho phép.
                String[] permissions = new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION};

                // Hiển thị một Dialog hỏi người dùng cho phép các quyền trên.
                ActivityCompat.requestPermissions(getActivity(), permissions,
                        REQUEST_ID_ACCESS_COURSE_FINE_LOCATION);

                return;
            }
        }

        // Hiển thị vị trí hiện thời trên bản đồ.
//        this.showMyLocation();
        myMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {

            }
        });
        myMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                tvLat.setText(latLng.latitude + "");
                tvLog.setText(latLng.longitude + "");
                if (currentMarker != null) {

                    currentMarker.remove();
                }
                myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));

                CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(latLng)             // Sets the center of the map to location user
                        .zoom(10)                   // Sets the zoom
//                        .bearing(90)                // Sets the orientation of the camera to east
//                        .tilt(40)                   // Sets the tilt of the camera to 30 degrees
                        .build();                   // Creates a CameraPosition from the builder
                myMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                // Lấy tên location
                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                String city = "";
                try {
                    List<Address> addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
                    if (addresses.size() > 0) {
                        city = addresses.get(0).getAdminArea();
                    } else {
                        city = "Unknown";
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


                // Thêm Marker cho Map:
                MarkerOptions option = new MarkerOptions();
                option.title("My Location");
                option.snippet(city);
                option.position(latLng);
                currentMarker = myMap.addMarker(option);
                currentMarker.showInfoWindow();

            }
        });

        btnLoglat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Start: Check lon lat có lấy được weather
                double lat = Double.parseDouble(tvLat.getText().toString());
                double lon = Double.parseDouble(tvLog.getText().toString());
//                int id = getIdLocate(lat,lon);
//                String name = getNameLocate(id);

                boolean resultLocation = checkLocation(lat,lon);
                // End
                if(!resultLocation){
                    Toast.makeText(getContext(), "Failed to get the weather !!!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!tvLat.getText().toString().equals("")&&!tvLog.getText().toString().equals("")){
                    Intent intent = new Intent(getActivity().getBaseContext(),
                            MainActivity.class);
                    intent.putExtra("keyLog", tvLog.getText());
                    intent.putExtra("keyLat", tvLat.getText());
                    getActivity().startActivity(intent);

                    Toast.makeText(getContext(), tvLog.getText() + "|" + tvLat.getText(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(getContext(), "Failed to get location !!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // Initialize the AutocompleteSupportFragment.
//        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
//                getSupportFragmentManager()).findFragmentById(R.id.autocomplete_fragment);
//
//// Specify the types of place data to return.
//        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));
//
//// Set up a PlaceSelectionListener to handle the response.
//        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
//            @Override
//            public void onPlaceSelected(Place place) {
//                // TODO: Get info about the selected place.
//                Log.i("TAG", "Place: " + place.getName() + ", " + place.getId());
//            }
//
//            @Override
//            public void onError(Status status) {
//                // TODO: Handle the error.
//                Log.i("TAG", "An error occurred: " + status);
//            }
//        });
    }

    // Khi người dùng trả lời yêu cầu cấp quyền (cho phép hoặc từ chối).
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //
        switch (requestCode) {
            case REQUEST_ID_ACCESS_COURSE_FINE_LOCATION: {


                // Chú ý: Nếu yêu cầu bị bỏ qua, mảng kết quả là rỗng.
                if (grantResults.length > 1
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(getActivity(), "Permission granted!", Toast.LENGTH_SHORT).show();

                    // Hiển thị vị trí hiện thời trên bản đồ.
                    this.showMyLocation();
                }
                // Hủy bỏ hoặc từ chối.
                else {
                    Toast.makeText(getActivity(), "Permission denied!", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    // Tìm một nhà cung cấp vị trị hiện thời đang được mở.
    private String getEnabledLocationProvider() {
        LocationManager locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);


        // Tiêu chí để tìm một nhà cung cấp vị trí.
        Criteria criteria = new Criteria();

        // Tìm một nhà cung vị trí hiện thời tốt nhất theo tiêu chí trên.
        // ==> "gps", "network",...
        String bestProvider = locationManager.getBestProvider(criteria, true);

        boolean enabled = locationManager.isProviderEnabled(bestProvider);

        if (!enabled) {
            Toast.makeText(getActivity(), "No location provider enabled!", Toast.LENGTH_SHORT).show();
            Log.i(HERE, "No location provider enabled!");
            return null;
        }
        return bestProvider;
    }

    // Chỉ gọi phương thức này khi đã có quyền xem vị trí người dùng.
    private void showMyLocation() {

        LocationManager locationManager = (LocationManager) getContext().getSystemService(LOCATION_SERVICE);

        String locationProvider = this.getEnabledLocationProvider();

        if (locationProvider == null) {
            return;
        }

        int i = 0;
        // Millisecond
        final long MIN_TIME_BW_UPDATES = 1000;
        // Met
        final float MIN_DISTANCE_CHANGE_FOR_UPDATES = 1;

        Location myLocation = null;
        try {

            // Đoạn code nay cần người dùng cho phép (Hỏi ở trên ***).
            locationManager.requestLocationUpdates(
                    locationProvider,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES, (LocationListener) this);

            // Lấy ra vị trí.
            myLocation = locationManager
                    .getLastKnownLocation(locationProvider);

            if (myLocation == null) {
                tvLat.setText(0 + "");
                tvLog.setText(0 + "");
            }
        }
        // Với Android API >= 23 phải catch SecurityException.
        catch (SecurityException e) {
            Toast.makeText(getActivity(), "Show My Location Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            Log.e(HERE, "Show My Location Error:" + e.getMessage());
            e.printStackTrace();
            return;
        }

        if (myLocation != null) {

//            LatLng latLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
//
//            // Thêm Marker cho Map:
//            MarkerOptions option = new MarkerOptions();
//            option.title("My Location");
//            option.snippet("....");
//            option.position(latLng);
//            currentMarker = myMap.addMarker(option);
//            currentMarker.showInfoWindow();
//
//            myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 13));
//
//            int i = 0;
//            CameraPosition cameraPosition = new CameraPosition.Builder()
//                    .target(latLng)             // Sets the center of the map to location user
//                    .zoom(150)                   // Sets the zoom
//                    .bearing(90)                // Sets the orientation of the camera to east
//                    .tilt(40)                   // Sets the tilt of the camera to 30 degrees
//                    .build();                   // Creates a CameraPosition from the builder
//            myMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        } else {
            Toast.makeText(getActivity(), "Location not found!", Toast.LENGTH_SHORT).show();
            Log.i(HERE, "Location not found");
        }


    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }

    @Override
    public void onPause() {
        super.onPause();
        if (currentMarker != null) {
            currentMarker.remove();
        }

    }

    // Start: Lấy data weather current dạng Json từ api của openweathermap.org
//    public String RequestAPICurrent(final int id) {
//        FetchRunnable t = new FetchRunnable(id, getContext(), "weather");
//
//        Thread thread = new Thread(t);
//        thread.start();
//        try {
//            thread.join();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        return t.getValue();
//    }



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
}


 */
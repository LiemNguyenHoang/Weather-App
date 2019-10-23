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
import com.google.android.gms.location.LocationServices;
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
import com.google.gson.JsonObject;

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
    private String name,country;
    private boolean firstTime;

    private boolean mLocationPermissionGranted;
    private Location mLastKnownLocation;
    private FusedLocationProviderClient mFusedLocationProviderClient;

    private Marker currentMarker;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
            firstTime = true;

            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());
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

            try {
                if (mLocationPermissionGranted) {
                    Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                    locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful()) {
                                // Set the map's camera position to the current location of the device.
                               if(task.getResult()!=null){
                                   mLastKnownLocation = task.getResult();

                                   double lon = mLastKnownLocation.getLongitude();
                                   double lat = mLastKnownLocation.getLatitude();
                                   tvLat.setText(lat+"");
                                   tvLon.setText(lon+"");
                                   mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                           new LatLng(mLastKnownLocation.getLatitude(),
                                                   mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                               }
                               else{
                                   Log.e("Data", "Không load dc map");
                               }
                            } else {
                                Log.d(TAG, "Current location is null. Using defaults.");
                                Log.e(TAG, "Exception: %s", task.getException());
                                mMap.moveCamera(CameraUpdateFactory
                                        .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                            }
                        }
                    });
                }
            } catch (SecurityException e)  {
                Log.e("Exception: %s", e.getMessage());
            }

            this.mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng latLng) {
                    tvLat.setText(String.valueOf(latLng.latitude));
                    tvLon.setText(String.valueOf(latLng.longitude));

                    // Start: lấy name và country
                    FetchRunnable t = new FetchRunnable(Double.parseDouble(tvLat.getText().toString()), Double.parseDouble(tvLon.getText().toString()), getContext(), "LatLng");

                    Thread thread = new Thread(t);
                    thread.start();
                    try {
                        thread.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    String json = t.getValue();
                    JSONObject jsonRoot = null;
                    name="";
                    country="";
                    try {
                        jsonRoot = new JSONObject(json);
                        JSONObject jsonObject = jsonRoot.getJSONObject("sys");
                        country=jsonObject.getString("country");
                        name = jsonRoot.getString("name");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    // End

//                    Toast.makeText(getContext(), latLng.latitude + ":" + latLng.longitude, Toast.LENGTH_SHORT).show();
                    if (currentMarker != null) {
                        currentMarker.remove();
                    }
                    currentMarker = mMap.addMarker(new MarkerOptions()
                            .title(name+", "+country)
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

                    boolean resultLocation = checkLocation(lat, lon);
                    // End
                    if (resultLocation == false) {
                        Toast.makeText(getContext(), "Failed to get the weather !!!", Toast.LENGTH_SHORT).show();

                    } else {
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
        JSONObject jsonRoot = null;
        try {
            jsonRoot = new JSONObject(json);

            JSONObject jsonObject = jsonRoot.getJSONObject("sys");
            country=jsonObject.getString("country");
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
}
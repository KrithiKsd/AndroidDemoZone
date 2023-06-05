package com.example.inclass11;
/*
a. Assignment #. InClass11
b. File Name : MainActivity.java
c. Full name of the student 1: Krithika Kasaragod
d. Full name of the student 2: Sahana Srinivas
*/

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.inclass11.databinding.ActivityMainBinding;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    ActivityMainBinding binding;
    private final OkHttpClient client = new OkHttpClient();
    ArrayList<Latitude_Longitude> listLatLong;
    private static final int COLOR_BLUE_ARGB = 0xFF006BEE;
    private static final int POLYLINE_STROKE_WIDTH_PX = 12;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTitle(getString(R.string.title_paths_activity));


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }

    private void getLatLongList(GoogleMap mMap) {

        Request request = new Request.Builder()
                .url("https://www.theappsdr.com/map/route")
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("TAG", "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    listLatLong = new ArrayList<>();
                    try {

                        ResponseBody responseBody = response.body();
                        String responseValue = responseBody.string();
                        JSONObject jsonObject = new JSONObject(responseValue);
                        JSONArray jsonArray = jsonObject.getJSONArray("path");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject json = jsonArray.getJSONObject(i);

                            Latitude_Longitude latLong = new Latitude_Longitude();
                            latLong.setLatitude(json.getString("latitude"));
                            latLong.setLongitude(json.getString("longitude"));
                            listLatLong.add(latLong);

                        }

                        Log.d("TAG", "onResponse: list**" + listLatLong);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                ArrayList<LatLng> latLongList = new ArrayList<>();
                                for (Latitude_Longitude item : listLatLong) {
                                    latLongList.add(new LatLng(Double.parseDouble(item.getLatitude()),
                                            Double.parseDouble(item.getLongitude())));
                                }

                                Polyline polyline = mMap.addPolyline(new PolylineOptions()
                                        .clickable(true)
                                        .addAll(latLongList));
                                polyline.setWidth(POLYLINE_STROKE_WIDTH_PX);
                                polyline.setColor(COLOR_BLUE_ARGB);


                                mMap.addMarker(new MarkerOptions().position(latLongList.get(0)));
                                mMap.addMarker(new MarkerOptions().position(latLongList.get(latLongList.size() - 1)));


                                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                                for (LatLng item : latLongList) {
                                    builder.include(item);
                                }

                                mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));

                            }

                        });


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Log.d("TAG", "onResponse: Failure");
                }

            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        getLatLongList(googleMap);
    }

}
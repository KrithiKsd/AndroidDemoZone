package com.example.homework08;
/*
a. Assignment #. Homework 08
b. File Name : MapFragment.java
c. Full name of the student 1: Krithika Kasaragod
*/

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.homework08.databinding.FragmentMapBinding;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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


public class MapFragment extends Fragment implements OnMapReadyCallback {

    FragmentMapBinding binding;
    private ArrayList<LatLng> mList;
    private static final int COLOR_BLUE_ARGB = 0xFF006BEE;
    private static final int POLYLINE_STROKE_WIDTH_PX = 12;
    String type="";
    private final OkHttpClient client = new OkHttpClient();
    ArrayList<DirectionPath> listLatLong;
    String startAddress, endAddress;

    GoogleMap mMap;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    void updateList(ArrayList<LatLng> list, String start, String end){
        if(mList!=null){
            mList.clear();
        }
        this.mList= list;
        startAddress= start;
        endAddress= end;
        setMap(mList, startAddress, endAddress);
    }

    private void setMap(ArrayList<LatLng> mList,String start, String end) {

        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentMapBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SupportMapFragment mapFragment = (SupportMapFragment)getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap=googleMap;
        if(mMap!=null){
            mMap.clear();
        }

        if(mList!=null && mList.size()>0){

            Polyline polyline = mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .addAll(mList));
            polyline.setWidth(POLYLINE_STROKE_WIDTH_PX);
            polyline.setColor(COLOR_BLUE_ARGB);


           /* this.mMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                @Override
                // Return null here, so that getInfoContents() is called next.
                public View getInfoWindow(Marker arg0) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {
                    // Inflate the layouts for the info window, title and snippet.

                    View infoWindow = getLayoutInflater().inflate(R.layout.custom_info_contents,
                            (FrameLayout).findFragmentById(R.id.map), false);

                    TextView title = infoWindow.findViewById(R.id.title);
                    title.setText(marker.getTitle());

                    TextView snippet = infoWindow.findViewById(R.id.snippet);
                    snippet.setText(marker.getSnippet());

                    return infoWindow;
                }



            });*/

            /*MarkerInfoWindowAdapter markerInfoWindowAdapter = new MarkerInfoWindowAdapter(getActivity());
            googleMap.setInfoWindowAdapter(markerInfoWindowAdapter);*/

       /*     Geocoder geo = new Geocoder(getActivity(), Locale.getDefault());
            List<Address> addressStart = null;
            List<Address> addressSEnd = null;
            try {
                addressStart = geo.getFromLocation(mList.get(0).latitude, mList.get(0).longitude, 1);
                addressSEnd= geo.getFromLocation(mList.get(mList.size() - 1).latitude, mList.get(mList.size() - 1).longitude, 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (addressStart.size() > 0) {
                binding.txtMarkerText.setVisibility(View.VISIBLE);
                mMap.addMarker(new MarkerOptions().position(mList.get(0)).title(addressStart.get(0).getCountryName()
                        + ". Address:" + addressStart.get(0).getAddressLine(0)));
                binding.txtMarkerText.setText(addressStart.get(0).getCountryName()
                        + ". Address:" + addressStart.get(0).getAddressLine(0));


                mMap.addMarker(new MarkerOptions().position(mList.get(mList.size() - 1)).title(addressSEnd.get(0).getCountryName()
                        + ". Address:" + addressSEnd.get(0).getAddressLine(0)));
                binding.txtMarkerText.setText(addressSEnd.get(0).getCountryName()
                        + ". Address:" + addressSEnd.get(0).getAddressLine(0));*/
            //  }

            mMap.addMarker(new MarkerOptions().position(mList.get(0)).title(startAddress));
            mMap.addMarker(new MarkerOptions().position(mList.get(mList.size() - 1)).title(endAddress));


            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (LatLng item : mList) {
                builder.include(item);
            }

            mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(builder.build(), 100));



            binding.btnRestaurant.setVisibility(View.VISIBLE);
            binding.btnGasStation.setVisibility(View.VISIBLE);


            binding.btnRestaurant.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    type="restaurant";
                    method_get_nearBy(type);
                }
            });

            binding.btnGasStation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    type="gas_station";
                    method_get_nearBy(type);
                }
            });


        }else {
            LatLng wdc = new LatLng(40.7128, -74.0060);
            mMap.addMarker(new MarkerOptions()
                    .position(wdc)
                    .title("NYC"));

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(wdc, 10));


        }

    }
    public static LatLng midPoint (LatLng start, LatLng end) throws IOException {

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(end).include(start);
        LatLngBounds latLngBounds = builder.build();
        //LatLngBounds bounds = LatLngBounds.builder().include(start).include(end).build();
           /* LatLngBounds bounds = new LatLngBounds(start, end);
            Log.d("BAD!", bounds.toString() + " CENTRE: " + bounds.getCenter().toString());
            bounds = LatLngBounds.builder().include(start).include(end).build();
            Log.d("GOOD", bounds.toString() + " CENTRE: " + bounds.getCenter().toString());*/
            return latLngBounds.getCenter();

    }

    private void method_get_nearBy(String type) {
       /* LatLng mp = midPoint(new LatLng(mList.get(0).latitude,mList.get(0).longitude),new LatLng(mList.get(mList.size()-1).latitude,mList.get(mList.size()-1).longitude));

        //LatLngBounds.builder().include(mList.get(0)).include(mList.get(mList.size()-1)).build().getCenter();
        Log.d("TAG", "method_get_nearBy: start**"+mList.get(0).latitude+mList.get(0).longitude+"**"+mList.get(mList.size()-1).latitude+mList.get(mList.size()-1).longitude);
        Log.d("TAG", "method_get_nearBy: center**"+mp);*/

        String url="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+mList.get(0).latitude+","+mList.get(0).longitude
                +"&radius=3000&types="+type+"&key="+getString(R.string.api_key);

        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
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
                        JSONArray jsonArray = jsonObject.getJSONArray("results");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject json = jsonArray.getJSONObject(i);
                            JSONObject jsonGeometry= json.getJSONObject("geometry");
                            JSONObject jsonLocation= jsonGeometry.getJSONObject("location");
                            DirectionPath directionPath= new DirectionPath();
                            directionPath.setLatitude(jsonLocation.getString("lat"));
                            directionPath.setLongitude(jsonLocation.getString("lng"));

                            directionPath.setName(json.getString("name"));

                            listLatLong.add(directionPath);


                        }
                        Log.d("TAG", "onResponse: list** type*******" + listLatLong);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                method_get_destination(type);
                                //ArrayList<LatLng> latLongList = new ArrayList<>();
                                for (DirectionPath item : listLatLong) {

                                    mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(item.getLatitude()),
                                            Double.parseDouble(item.getLongitude()))).title(item.name));
                                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(item.getLatitude()),
                                            Double.parseDouble(item.getLongitude())), 12));

                                }

                            }

                        });


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    Log.d("TAG", "onResponse: Failure");
                }

            }
        });
    }

    void method_get_destination(String type){
        String url="https://maps.googleapis.com/maps/api/place/nearbysearch/json?location="+mList.get(mList.size()-1).latitude+","+mList.get(mList.size()-1).longitude
                +"&radius=3000&types="+type+"&key="+getString(R.string.api_key);

        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d("TAG", "onFailure: " + e.getMessage());
            }
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    // listLatLong = new ArrayList<>();
                    if(listLatLong!=null) {
                        try {

                            ResponseBody responseBody = response.body();
                            String responseValue = responseBody.string();
                            JSONObject jsonObject = new JSONObject(responseValue);
                            JSONArray jsonArray = jsonObject.getJSONArray("results");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject json = jsonArray.getJSONObject(i);
                                JSONObject jsonGeometry = json.getJSONObject("geometry");
                                JSONObject jsonLocation = jsonGeometry.getJSONObject("location");
                                DirectionPath directionPath = new DirectionPath();
                                directionPath.setLatitude(jsonLocation.getString("lat"));
                                directionPath.setLongitude(jsonLocation.getString("lng"));

                                directionPath.setName(json.getString("name"));

                                listLatLong.add(directionPath);


                            }
                            Log.d("TAG", "onResponse: list** type*******" + listLatLong);
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {


                                    // ArrayList<LatLng> latLongList = new ArrayList<>();
                                    for (DirectionPath item : listLatLong) {

                                        mMap.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(item.getLatitude()),
                                                Double.parseDouble(item.getLongitude()))).title(item.name));
                                       /*mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(Double.parseDouble(item.getLatitude()),
                                               Double.parseDouble(item.getLongitude())), 10));*/

                                    }
                                    ArrayList<LatLng> latLongList = new ArrayList<>();
                                    for (DirectionPath  item : listLatLong) {
                                        latLongList.add(new LatLng(Double.parseDouble(item.getLatitude()),
                                                Double.parseDouble(item.getLongitude())));
                                    }

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
                    }
                }
                else {
                    Log.d("TAG", "onResponse: Failure");
                }

            }
        });
    }



}
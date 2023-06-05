package com.example.homework08;
/*
a. Assignment #. Homework 08
b. File Name : SearchLocationFragment.java
c. Full name of the student 1: Krithika Kasaragod
*/
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.homework08.databinding.FragmentSearchLoactionBinding;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;


public class SearchLocationFragment extends Fragment {

    FragmentSearchLoactionBinding binding;
    Place startPlace, endPlace;
    private final OkHttpClient client = new OkHttpClient();
    ArrayList<DirectionPath> listLatLong;
    String startAddress, endAddress;
    public SearchLocationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSearchLoactionBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (!Places.isInitialized()) {
            Places.initialize(getContext(), getString(R.string.api_key), Locale.US);
        }

        AutocompleteSupportFragment autocompleteFragmentStart = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment_start_location);

        autocompleteFragmentStart.setHint("Select your start location");
        autocompleteFragmentStart.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        autocompleteFragmentStart.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                Log.i("TAG", "Place: " + place.getName() + ", " + place.getId());
                startPlace= place;
            }
            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i("TAG", "An error occurred: " + status);

            }
        });

        AutocompleteSupportFragment autocompleteFragmentEnd = (AutocompleteSupportFragment)
                getChildFragmentManager().findFragmentById(R.id.autocomplete_fragment_endLocation);
        autocompleteFragmentEnd.setHint("Select your end location");
        autocompleteFragmentEnd.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        autocompleteFragmentEnd.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // TODO: Get info about the selected place.
                Log.i("TAG", "Place: " + place.getName() + ", " + place.getId());
                endPlace= place;
            }
            @Override
            public void onError(@NonNull Status status) {
                // TODO: Handle the error.
                Log.i("TAG", "An error occurred: " + status);
            }
        });


        binding.btnGetDirection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(startPlace!=null && endPlace!=null){

                    methodGetDirection();
                  
                }
            }
        });
    }

    private void methodGetDirection() {
        String url="https://maps.googleapis.com/maps/api/directions/json?origin=place_id:"+startPlace.getId()+"&destination=place_id:"+endPlace.getId()+"&key="+getString(R.string.api_key);
        Request request = new Request.Builder()
                .url(url)
                .method("GET", null)
                .build();

        Log.d("TAG", "methodGetDirection:url** "+url);
        Log.d("TAG", "methodGetDirection:startPlace "+startPlace.getId());
        Log.d("TAG", "methodGetDirection:endPlace "+endPlace.getId());

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
                        JSONArray jsonArray = jsonObject.getJSONArray("routes");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject json = jsonArray.getJSONObject(i);
                            JSONArray jsonLegs = json.getJSONArray("legs");


                            for(int j=0;j<jsonLegs.length();j++){
                                JSONObject jsonObjLegs = jsonLegs.getJSONObject(j);
                                startAddress =jsonObjLegs.getString("start_address");
                                endAddress = jsonObjLegs.getString("end_address");

                                JSONArray jsonSteps = jsonObjLegs.getJSONArray("steps");
                                for(int k=0;k<jsonSteps.length();k++){
                                    DirectionPath latLong = new DirectionPath();
                                    JSONObject jsonLatLong = jsonSteps.getJSONObject(k);

                                    JSONObject jsonStart=jsonLatLong.getJSONObject("start_location");
                                    latLong.setLatitude(jsonStart.getString("lat"));
                                    latLong.setLongitude(jsonStart.getString("lng"));

                                  /*  latLong.setEndAddress(jsonLatLong.getString("end_address"));
                                    latLong.setStartAddress(jsonLatLong.getString("start_address"));*/

                                    listLatLong.add(latLong);

                                    JSONObject jsonEnd=jsonLatLong.getJSONObject("end_location");
                                    latLong.setLatitude(jsonEnd.getString("lat"));
                                    latLong.setLongitude(jsonEnd.getString("lng"));



                                    listLatLong.add(latLong);
                                }
                            }

                        }

                        Log.d("TAG", "onResponse: list**" + listLatLong);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                ArrayList<LatLng> latLongList = new ArrayList<>();
                                for (DirectionPath item : listLatLong) {
                                    latLongList.add(new LatLng(Double.parseDouble(item.getLatitude()),
                                            Double.parseDouble(item.getLongitude())));
                                }

                                mListener.gotoMapFragment(latLongList,startAddress,endAddress);

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
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            if (context instanceof IListener) {
                mListener = (IListener) context;
            } else {
                throw new RuntimeException();
            }
        } catch (RuntimeException e) {
        }
    }

    IListener mListener;

    interface IListener {
        void gotoMapFragment(ArrayList<LatLng> list,String start, String end);
    }
}
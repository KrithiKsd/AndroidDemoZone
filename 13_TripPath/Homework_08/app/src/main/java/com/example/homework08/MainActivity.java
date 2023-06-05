package com.example.homework08;
/*
a. Assignment #. Homework 08
b. File Name : MainActivity.java
c. Full name of the student 1: Krithika Kasaragod
*/

/*
In this assignment you will create an app that enables the user to get the driving directions between two locations, and also view interesting places along the path. The app should provide the following functions:

The ability of the user to pick the start and end location.
You should use the Google Place Autocomplete API https://developers.google.com/maps/documentation/places/android-sdk/autocomplete (Links to an external site.) for the user to enter and select the start and the end location.
The app should display the user selected locations (show the addresses)
The ability to find the driving directions from the selected start and end locations
You should use the the Google Directions API https://developers.google.com/maps/documentation/directions/get-directions (Links to an external site.)
Draw the path from the start and end locations on a Google Map.
The map zoom should automatically be setup to include the path polyline and the start and end location markers.
The user should also be able to search for places of type "gas_station" and "restaurant" with in the displayed map.
Use the Google Place Search API https://developers.google.com/maps/documentation/places/web-service/search (Links to an external site.)
Show the results on the map using markers.
* */


import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.gms.maps.model.LatLng;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchLocationFragment.IListener {

    private static int AUTOCOMPLETE_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public void gotoMapFragment(ArrayList<LatLng> list, String start, String end) {
        MapFragment fragment= (MapFragment) getSupportFragmentManager().findFragmentById(R.id.fragmentMap);
        if(fragment!=null){
            fragment.updateList(list,start,end);
        }

    }
}
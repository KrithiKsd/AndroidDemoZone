package com.example.inclass11;

import com.google.android.gms.maps.model.LatLng;
/*
a. Assignment #. InClass11
b. File Name : Latitude_Longitude.java
c. Full name of the student 1: Krithika Kasaragod
d. Full name of the student 2: Sahana Srinivas
*/
public class Latitude_Longitude {
    String latitude,longitude;

    public Latitude_Longitude() {
    }

    public Latitude_Longitude(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "LatLong{" +
                "latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                '}';
    }
}

package com.example.homework08;
/*
a. Assignment #. Homework 08
b. File Name : DirectionPath.java
c. Full name of the student 1: Krithika Kasaragod
*/
public class DirectionPath {
    String latitude,longitude;
    String startAddress, endAddress;

    String name;

    public DirectionPath(String latitude, String longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public DirectionPath() {
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



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartAddress() {
        return startAddress;
    }

    public void setStartAddress(String startAddress) {
        this.startAddress = startAddress;
    }

    public String getEndAddress() {
        return endAddress;
    }

    public void setEndAddress(String endAddress) {
        this.endAddress = endAddress;
    }

    @Override
    public String toString() {
        return "DirectionPath{" +
                "latitude='" + latitude + '\'' +
                ", longitude='" + longitude + '\'' +
                ", startAddress='" + startAddress + '\'' +
                ", endAddress='" + endAddress + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}

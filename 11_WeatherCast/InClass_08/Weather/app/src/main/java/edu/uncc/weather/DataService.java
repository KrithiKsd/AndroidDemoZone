package edu.uncc.weather;
/*
a. Assignment #. InClass08
b. File Name : DataService.java
c. Full name of the student 1: Krithika Kasaragod
*/
import java.io.Serializable;
import java.util.ArrayList;

public class DataService {

    static final String ARG_API_KEY = "0ebf862fa976f147c4452961548c973c";
    static final String ARG_MODE = "JSON";
    static final String ARG_UNITS = "imperial";

    static public final ArrayList<City> cities = new ArrayList<City>() {{
        add(new City("US", "Charlotte"));
        add(new City("US", "Chicago"));
        add(new City("US", "New York"));
        add(new City("US", "Miami"));
        add(new City("US", "San Francisco"));
        add(new City("US", "Baltimore"));
        add(new City("US", "Houston"));
        add(new City("UK", "London"));
        add(new City("UK", "Bristol"));
        add(new City("UK", "Cambridge"));
        add(new City("UK", "Liverpool"));
        add(new City("AE", "Abu Dhabi"));
        add(new City("AE", "Dubai"));
        add(new City("AE", "Sharjah"));
        add(new City("JP", "Tokyo"));
        add(new City("JP", "Kyoto"));
        add(new City("JP", "Hashimoto"));
        add(new City("JP", "Osaka"));
    }};

    static class City implements Serializable {
        private String country;
        private String city;
        private String lat, lon;

        public City() {
        }

        public City(String country, String city) {
            this.country = country;
            this.city = city;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLon() {
            return lon;
        }

        public void setLon(String lon) {
            this.lon = lon;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        @Override
        public String toString() {
            return city + "," + country;
        }
    }

    static class ForeCast implements Serializable {
        private String dateValue, temperature, minTemp, maxTemp, humidity, description;
        private String iconID;

        public String getIconID() {
            return iconID;
        }

        public void setIconID(String iconID) {
            this.iconID = iconID;
        }

        public String getDateValue() {
            return dateValue;
        }

        public void setDateValue(String dateValue) {
            this.dateValue = dateValue;
        }

        public String getTemperature() {
            return temperature;
        }

        public void setTemperature(String temperature) {
            this.temperature = temperature;
        }

        public String getMinTemp() {
            return minTemp;
        }

        public void setMinTemp(String minTemp) {
            this.minTemp = minTemp;
        }

        public String getMaxTemp() {
            return maxTemp;
        }

        public void setMaxTemp(String maxTemp) {
            this.maxTemp = maxTemp;
        }

        public String getHumidity() {
            return humidity;
        }

        public void setHumidity(String humidity) {
            this.humidity = humidity;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        @Override
        public String toString() {
            return "ForeCast{" +
                    "dateValue='" + dateValue + '\'' +
                    ", temperature='" + temperature + '\'' +
                    ", minTemp='" + minTemp + '\'' +
                    ", maxTemp='" + maxTemp + '\'' +
                    ", humidity='" + humidity + '\'' +
                    ", description='" + description + '\'' +
                    '}';
        }
    }
}

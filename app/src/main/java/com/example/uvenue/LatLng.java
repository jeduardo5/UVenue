package com.example.uvenue;

import android.location.Location;

public class LatLng {

    double Latitude;
    double Longitude;

    public LatLng(Location location){
        Latitude = location.getLatitude();
        Longitude = location.getLongitude();
    }

    public double getLatitude() {
        return Latitude;
    }

    public String getLatitudeStr() {
        return Double.toString(getLatitude());
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public String getLongitudeStr() {
        return Double.toString(getLongitude());
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }
}

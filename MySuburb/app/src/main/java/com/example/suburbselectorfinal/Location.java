package com.example.suburbselectorfinal;

public class Location {

    private final double latitude, longitude;

    public Location(double latitude, double longitude) {
        if(latitude > 0) {
            this.latitude = 0 - latitude;
        } else {
            this.latitude = latitude;
        }
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }
}

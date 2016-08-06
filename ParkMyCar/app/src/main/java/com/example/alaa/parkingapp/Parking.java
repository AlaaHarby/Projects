package com.example.alaa.parkingapp;

/**
 * Created by Alaa on 7/21/2016.
 */
public class Parking {

    double latitude;
    double longitude;
    String email;
    String info;

    public Parking() {}

    public Parking(double latitude, double longitude, String email, String info) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.info = info;
        this.email = email;
    }

    public double getLongitude() {
        return longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public String getInfo() {
        return info;
    }

    public String getEmail() {
        return email;
    }
}

package com.example.alaa.parkingapp;

/**
 * Created by Alaa on 7/21/2016.
 */
public class Parking {

    double latitude;
    double longitude;
    String email;
    String info;
    String parkingID;

    public Parking() {}

    public Parking(String parkingID, double latitude, double longitude, String email, String info) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.info = info;
        this.email = email;
        this.parkingID = parkingID;
    }

    public String getParkingID() {
        return parkingID;
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

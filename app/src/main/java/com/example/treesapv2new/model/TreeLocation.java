package com.example.treesapv2new.model;

public class TreeLocation {

    private double longitude;
    private double latitude;

    public TreeLocation() {
        this.longitude = 0.0;
        this.latitude = 0.0;
    }

    public TreeLocation(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public TreeLocation(String coordinates) {
        GPSCoordinates coords = new GPSCoordinates();
        try {
            coords.parse(coordinates);
            this.latitude = coords.getLatitude();
            this.longitude = coords.getLongitude();
        } catch (Exception e) {

        }
    }

    public double getLatitude() {
        return latitude;
    }

//    public String getLatString(){
//        return latitude.toString();
//    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLatitude(String latitude) {
        String coordinates = latitude + "," + this.longitude;
        GPSCoordinates coords = new GPSCoordinates();
        try {
            coords.parse(coordinates);
            this.latitude = coords.getLatitude();
        } catch (Exception e) {

        }
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setLongitude(String longitude) {
        String coordinates = this.latitude + "," + longitude;
        GPSCoordinates coords = new GPSCoordinates();
        try {
            coords.parse(coordinates);
            this.longitude = coords.getLongitude();
        } catch (Exception e) {

        }
    }

    public String toString() {
        return "("+latitude+", "+longitude+")";
    }
}

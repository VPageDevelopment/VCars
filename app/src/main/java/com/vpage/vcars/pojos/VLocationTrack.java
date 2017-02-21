package com.vpage.vcars.pojos;


public class VLocationTrack {

    private double latitude;
    private double longitude;
    private String dateTime;
    private String location;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "VLocationTrack{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", dateTime='" + dateTime + '\'' +
                ", location='" + location + '\'' +
                '}';
    }
}

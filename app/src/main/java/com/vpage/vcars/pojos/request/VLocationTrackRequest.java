package com.vpage.vcars.pojos.request;


public class VLocationTrackRequest {

    private String user;
    private double latitude;
    private double longitude;
    private String location;

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return "VLocationTrackRequest{" +
                " user='" + user + '\'' +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", location='" + location + '\'' +
                '}';
    }
}

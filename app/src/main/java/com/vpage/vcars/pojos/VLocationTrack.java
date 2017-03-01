package com.vpage.vcars.pojos;


public class VLocationTrack {

    private double latitude;
    private double longitude;
    private String date;
    private String address;
    private String location;
    private String city;

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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return "VLocationTrack{" +
                "latitude=" + latitude +
                ", longitude=" + longitude +
                ", date='" + date + '\'' +
                ", address='" + address + '\'' +
                ", location='" + location + '\'' +
                ", city='" + city + '\'' +
                '}';
    }
}

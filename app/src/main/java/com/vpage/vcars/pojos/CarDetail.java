package com.vpage.vcars.pojos;


public class CarDetail {

    private String carName;
    private String carDistance;
    private String carVarient;

    public String getCarName() {
        return carName;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public String getCarDistance() {
        return carDistance;
    }

    public void setCarDistance(String carDistance) {
        this.carDistance = carDistance;
    }

    public String getCarVarient() {
        return carVarient;
    }

    public void setCarVarient(String carVarient) {
        this.carVarient = carVarient;
    }

    @Override
    public String toString() {
        return "CarDetail{" +
                "carName='" + carName + '\'' +
                ", carDistance='" + carDistance + '\'' +
                ", carVarient='" + carVarient + '\'' +
                '}';
    }
}

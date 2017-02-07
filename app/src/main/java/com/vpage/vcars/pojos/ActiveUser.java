package com.vpage.vcars.pojos;

public class ActiveUser {

    private String hq;

    private String appleStore;

    private String userDisplayName;

    private String devicePlatformName;

    private String avatar;

    private String deviceIdentity;

    private String ha;

    private String googleStore;

    private String session;

    private String email;

    private String studentid;

    private String studentKey;

    private String place;

    private String uid;

    private String newAlert;

    private String deviceToken;

    private String LoginType;

    public String getLoginType() {
        return LoginType;
    }

    public void setLoginType(String loginType) {
        LoginType = loginType;
    }

    public String getHq() {
        return hq;
    }

    public void setHq(String hq) {
        this.hq = hq;
    }

    public String getAppleStore() {
        return appleStore;
    }

    public void setAppleStore(String appleStore) {
        this.appleStore = appleStore;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    public String getDevicePlatformName() {
        return devicePlatformName;
    }

    public void setDevicePlatformName(String devicePlatformName) {
        this.devicePlatformName = devicePlatformName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDeviceIdentity() {
        return deviceIdentity;
    }

    public void setDeviceIdentity(String deviceIdentity) {
        this.deviceIdentity = deviceIdentity;
    }

    public String getHa() {
        return ha;
    }

    public void setHa(String ha) {
        this.ha = ha;
    }

    public String getGoogleStore() {
        return googleStore;
    }

    public void setGoogleStore(String googleStore) {
        this.googleStore = googleStore;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStudentid() {
        return studentid;
    }

    public void setStudentid(String studentid) {
        this.studentid = studentid;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }


    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getNewAlert() {
        return newAlert;
    }

    public void setNewAlert(String newAlert) {
        this.newAlert = newAlert;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getStudentKey() {
        return studentKey;
    }

    public void setStudentKey(String studentKey) {
        this.studentKey = studentKey;
    }



    @Override
    public String toString() {
        return "ActiveUser{" +
                "hq='" + hq + '\'' +
                ", appleStore='" + appleStore + '\'' +
                ", userDisplayName='" + userDisplayName + '\'' +
                ", devicePlatformName='" + devicePlatformName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", deviceIdentity='" + deviceIdentity + '\'' +
                ", ha='" + ha + '\'' +
                ", googleStore='" + googleStore + '\'' +
                ", session='" + session + '\'' +
                ", email='" + email + '\'' +
                ", studentid='" + studentid + '\'' +
                ", studentKey='" + studentKey + '\'' +
                ", place='" + place + '\'' +
                ", uid='" + uid + '\'' +
                ", newAlert='" + newAlert + '\'' +
                ", deviceToken='" + deviceToken + '\'' +
                '}';
    }
}

package com.vpage.vcars.pojos;

public class ActiveUser {


    private String userDisplayName;

    private String devicePlatformName;

    private String profileImage;

    private String deviceIdentity;

    private String googleStore;

    private String email;

    private String userId;

    private String place;

    private String uid;

    private String deviceToken;

    private String LoginType;

    public String getLoginType() {
        return LoginType;
    }

    public void setLoginType(String loginType) {
        LoginType = loginType;
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

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getDeviceIdentity() {
        return deviceIdentity;
    }

    public void setDeviceIdentity(String deviceIdentity) {
        this.deviceIdentity = deviceIdentity;
    }

    public String getGoogleStore() {
        return googleStore;
    }

    public void setGoogleStore(String googleStore) {
        this.googleStore = googleStore;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }



    @Override
    public String toString() {
        return "ActiveUser{" +
                ", userDisplayName='" + userDisplayName + '\'' +
                ", devicePlatformName='" + devicePlatformName + '\'' +
                ", profileImage='" + profileImage + '\'' +
                ", deviceIdentity='" + deviceIdentity + '\'' +
                ", googleStore='" + googleStore + '\'' +
                ", email='" + email + '\'' +
                ", userId='" + userId + '\'' +
                ", place='" + place + '\'' +
                ", uid='" + uid + '\'' +
                ", deviceToken='" + deviceToken + '\'' +
                '}';
    }
}

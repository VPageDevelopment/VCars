package com.vpage.vcars.pojos.request;

public class CheckUserRequest {

    private String phoneNo;
    private String otp;
    private String userID;
    private String loginType;
    private String devicePlatformName;
    private String deviceIdentity;
     private String fbTokenId;
    private String deviceToken;
    private String email;
    private String country;
    private String profilePic;

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getProfilePic() {
        return profilePic;
    }

    public void setProfilePic(String profilePic) {
        this.profilePic = profilePic;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getDevicePlatformName() {
        return devicePlatformName;
    }

    public void setDevicePlatformName(String devicePlatformName) {
        this.devicePlatformName = devicePlatformName;
    }

    public String getDeviceIdentity() {
        return deviceIdentity;
    }

    public void setDeviceIdentity(String deviceIdentity) {
        this.deviceIdentity = deviceIdentity;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getFbTokenId() {
        return fbTokenId;
    }

    public void setFbTokenId(String fbTokenId) {
        this.fbTokenId = fbTokenId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public String toString() {
        return "CheckUserRequest{" +
                "phoneNo='" + phoneNo + '\'' +
                ", OTP='" + otp + '\'' +
                ", userID='" + userID + '\'' +
                ", loginType='" + loginType + '\'' +
                ", devicePlatformName='" + devicePlatformName + '\'' +
                ", deviceIdentity='" + deviceIdentity + '\'' +
                ", fbTokenId='" + fbTokenId + '\'' +
                ", deviceToken='" + deviceToken + '\'' +
                ", email='" + email + '\'' +
                ", country='" + country + '\'' +
                ", profilePic='" + profilePic + '\'' +
                '}';
    }
}

package com.vpage.vcars.pojos.request;

public class SignupRequest {

    private String classcode;

    private String hq;

    private String avatar;

    private String devicePlatformName;

    private String password;

    private String deviceIdentity;

    private String loginType;

    private String version;

    private String ha;

    private String appVersion;

    private String email;

    private String deviceToken;

    private int grade;

    private boolean clientEncryptedPassword;

    private String userId;

    private String fbTokenId;

    private String loginId;

    private String fname;

    private String lname;

    private String country;

    private String city;

    private String state;

    private DeviceInfoData deviceInfoData;

    private String referralCode;

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getFbTokenId() {
        return fbTokenId;
    }

    public void setFbTokenId(String fbTokenId) {
        this.fbTokenId = fbTokenId;
    }

    public String getClasscode() {
        return classcode;
    }

    public void setClasscode(String classcode) {
        this.classcode = classcode;
    }

    public String getHq() {
        return hq;
    }

    public void setHq(String hq) {
        this.hq = hq;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getDevicePlatformName() {
        return devicePlatformName;
    }

    public void setDevicePlatformName(String devicePlatformName) {
        this.devicePlatformName = devicePlatformName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceIdentity() {
        return deviceIdentity;
    }

    public void setDeviceIdentity(String deviceIdentity) {
        this.deviceIdentity = deviceIdentity;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getHa() {
        return ha;
    }

    public void setHa(String ha) {
        this.ha = ha;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public boolean isClientEncryptedPassword() {
        return clientEncryptedPassword;
    }

    public void setClientEncryptedPassword(boolean clientEncryptedPassword) {
        this.clientEncryptedPassword = clientEncryptedPassword;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public DeviceInfoData getDeviceInfoData() {
        return deviceInfoData;
    }

    public void setDeviceInfoData(DeviceInfoData deviceInfoData) {
        this.deviceInfoData = deviceInfoData;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    @Override
    public String toString() {
        return "SignupRequest{" +
                "classcode='" + classcode + '\'' +
                ", hq='" + hq + '\'' +
                ", avatar='" + avatar + '\'' +
                ", devicePlatformName='" + devicePlatformName + '\'' +
                ", password='" + password + '\'' +
                ", deviceIdentity='" + deviceIdentity + '\'' +
                ", loginType='" + loginType + '\'' +
                ", version='" + version + '\'' +
                ", ha='" + ha + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", email='" + email + '\'' +
                ", deviceToken='" + deviceToken + '\'' +
                ", grade=" + grade +
                ", clientEncryptedPassword=" + clientEncryptedPassword +
                ", userId='" + userId + '\'' +
                ", fbTokenId='" + fbTokenId + '\'' +
                ", loginId='" + loginId + '\'' +
                ", fname='" + fname + '\'' +
                ", lname='" + lname + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", deviceInfoData=" + deviceInfoData +
                ", referralCode='" + referralCode + '\'' +
                '}';
    }
}

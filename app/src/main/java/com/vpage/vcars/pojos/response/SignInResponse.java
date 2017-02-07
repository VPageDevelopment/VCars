package com.vpage.vcars.pojos.response;


public class SignInResponse {
    private String hq;

    private String appleStore;

    private String error;

    private String apiVersionMismatch;

    private String userDisplayName;

    private String devicePlatformName;

    private String avatar;

    private String deviceIdentity;

    private String ha;

    private String googleStore;

    private String session;

    private String email;

    private boolean success;

    private String userId;

    private String userKey;

    private String loginType;

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

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getApiVersionMismatch() {
        return apiVersionMismatch;
    }

    public void setApiVersionMismatch(String apiVersionMismatch) {
        this.apiVersionMismatch = apiVersionMismatch;
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

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserKey() {
        return userKey;
    }

    public void setUserKey(String userKey) {
        this.userKey = userKey;
    }

    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    @Override
    public String toString() {
        return "SignInResponse{" +
                "hq='" + hq + '\'' +
                ", appleStore='" + appleStore + '\'' +
                ", error='" + error + '\'' +
                ", apiVersionMismatch='" + apiVersionMismatch + '\'' +
                ", userDisplayName='" + userDisplayName + '\'' +
                ", devicePlatformName='" + devicePlatformName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", deviceIdentity='" + deviceIdentity + '\'' +
                ", ha='" + ha + '\'' +
                ", googleStore='" + googleStore + '\'' +
                ", session='" + session + '\'' +
                ", email='" + email + '\'' +
                ", success=" + success +
                ", userId='" + userId + '\'' +
                ", userKey='" + userKey + '\'' +
                '}';
    }
}

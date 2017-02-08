package com.vpage.vcars.pojos.response;


public class SignInResponse {

    private String error;

    private String apiVersionMismatch;

    private String userDisplayName;

    private String devicePlatformName;

    private String userProfileImage;

    private String deviceIdentity;

    private String googleStore;

    private String email;

    private boolean success;

    private String userId;

    private String loginType;


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

    public String getDeviceIdentity() {
        return deviceIdentity;
    }

    public void setDeviceIdentity(String deviceIdentity) {
        this.deviceIdentity = deviceIdentity;
    }

    public String getUserProfileImage() {
        return userProfileImage;
    }

    public void setUserProfileImage(String userProfileImage) {
        this.userProfileImage = userProfileImage;
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


    public String getLoginType() {
        return loginType;
    }

    public void setLoginType(String loginType) {
        this.loginType = loginType;
    }

    @Override
    public String toString() {
        return "SignInResponse{" +
                ", error='" + error + '\'' +
                ", apiVersionMismatch='" + apiVersionMismatch + '\'' +
                ", userDisplayName='" + userDisplayName + '\'' +
                ", devicePlatformName='" + devicePlatformName + '\'' +
                ", userProfileImage='" + userProfileImage + '\'' +
                ", deviceIdentity='" + deviceIdentity + '\'' +
                ", googleStore='" + googleStore + '\'' +
                ", email='" + email + '\'' +
                ", success=" + success +
                ", userId='" + userId + '\'' +
                '}';
    }
}

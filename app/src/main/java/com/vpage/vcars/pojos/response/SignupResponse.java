package com.vpage.vcars.pojos.response;

public class SignupResponse {

    private String googleStore;

    private String error;

    private String email;

    private String userDisplayName;

    private String userProfileImage;

    private boolean success;

    private String userId;

    public String getGoogleStore() {
        return googleStore;
    }

    public void setGoogleStore(String googleStore) {
        this.googleStore = googleStore;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    public String getProfileImage() {
        return userProfileImage;
    }

    public void setProfileImage(String userProfileImage) {
        this.userProfileImage = userProfileImage;
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

    @Override
    public String toString() {
        return "SignupResponse{" +
                "googleStore='" + googleStore + '\'' +
                ", error='" + error + '\'' +
                ", email='" + email + '\'' +
                ", userDisplayName='" + userDisplayName + '\'' +
                ", userProfileImage='" + userProfileImage + '\'' +
                ", success=" + success +
                ", userId='" + userId + '\'' +
                '}';
    }
}

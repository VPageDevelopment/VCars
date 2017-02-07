package com.vpage.vcars.pojos.response;

public class SignupResponse {

    private String googleStore;

    private String appleStore;

    private String hq;

    private String error;

    private String email;

    private String session;

    private String expDate;

    private String userDisplayName;

    private String avatar;

    private boolean success;

    private String ha;

    private String userId;

    private String firebaseToken;

    private String studentKey;

    private String referralCode;

    public String getGoogleStore() {
        return googleStore;
    }

    public void setGoogleStore(String googleStore) {
        this.googleStore = googleStore;
    }

    public String getAppleStore() {
        return appleStore;
    }

    public void setAppleStore(String appleStore) {
        this.appleStore = appleStore;
    }

    public String getHq() {
        return hq;
    }

    public void setHq(String hq) {
        this.hq = hq;
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

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getExpDate() {
        return expDate;
    }

    public void setExpDate(String expDate) {
        this.expDate = expDate;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getHa() {
        return ha;
    }

    public void setHa(String ha) {
        this.ha = ha;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    public String getStudentKey() {
        return studentKey;
    }

    public void setStudentKey(String studentKey) {
        this.studentKey = studentKey;
    }

    public String getReferralCode() {
        return referralCode;
    }

    public void setReferralCode(String referralCode) {
        this.referralCode = referralCode;
    }

    @Override
    public String toString() {
        return "SignupResponse{" +
                "googleStore='" + googleStore + '\'' +
                ", appleStore='" + appleStore + '\'' +
                ", hq='" + hq + '\'' +
                ", error='" + error + '\'' +
                ", email='" + email + '\'' +
                ", session='" + session + '\'' +
                ", expDate='" + expDate + '\'' +
                ", userDisplayName='" + userDisplayName + '\'' +
                ", avatar='" + avatar + '\'' +
                ", success=" + success +
                ", ha='" + ha + '\'' +
                ", userId='" + userId + '\'' +
                ", firebaseToken='" + firebaseToken + '\'' +
                ", studentKey='" + studentKey + '\'' +
                '}';
    }
}

package com.vpage.vcars.pojos.response;

import java.util.Arrays;

public class CheckUserResponse {

    private String[] passwordRecoveryQuestion;

    private String error;

    private String exists;

    private String userDisplayName;

    private String existBy;

    private String success;

    private String userId;

    private String userKey;

    private String appleStore;

    private String googleStore;


    public String getAppleStore() {
        return appleStore;
    }

    public void setAppleStore(String appleStore) {
        this.appleStore = appleStore;
    }

    public String getGoogleStore() {
        return googleStore;
    }

    public void setGoogleStore(String googleStore) {
        this.googleStore = googleStore;
    }

    public String[] getPasswordRecoveryQuestion() {
        return passwordRecoveryQuestion;
    }

    public void setPasswordRecoveryQuestion(String[] passwordRecoveryQuestion) {
        this.passwordRecoveryQuestion = passwordRecoveryQuestion;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getExists() {
        return exists;
    }

    public void setExists(String exists) {
        this.exists = exists;
    }

    public String getUserDisplayName() {
        return userDisplayName;
    }

    public void setUserDisplayName(String userDisplayName) {
        this.userDisplayName = userDisplayName;
    }

    public String getExistBy() {
        return existBy;
    }

    public void setExistBy(String existBy) {
        this.existBy = existBy;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
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

    @Override
    public String toString() {
        return "CheckUserResponse{" +
                "passwordRecoveryQuestion=" + Arrays.toString(passwordRecoveryQuestion) +
                ", error='" + error + '\'' +
                ", exists='" + exists + '\'' +
                ", userDisplayName='" + userDisplayName + '\'' +
                ", existBy='" + existBy + '\'' +
                ", success='" + success + '\'' +
                ", userId='" + userId + '\'' +
                ", userKey='" + userKey + '\'' +
                ", appleStore='" + appleStore + '\'' +
                ", googleStore='" + googleStore + '\'' +
                '}';
    }
}

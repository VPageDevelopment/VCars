package com.vpage.vcars.tools;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vpage.vcars.pojos.FacebookUserprofile;
import com.vpage.vcars.pojos.response.CheckUserResponse;
import com.vpage.vcars.pojos.response.SignInResponse;
import com.vpage.vcars.pojos.response.SignupResponse;


public class VCarRestTools {

    private static final String TAG = VCarRestTools.class.getName();


    private static final Object monitor = new Object();
    private static VCarRestTools vCarRestTools = null;

    public static VCarRestTools getInstance() {
        if (vCarRestTools == null) {
            synchronized (monitor) {
                if (vCarRestTools == null)
                    vCarRestTools = new VCarRestTools();
            }
        }
        return vCarRestTools;
    }


    public SignInResponse getSignInData(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, SignInResponse.class);
    }


    public SignupResponse getRegisterData(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, SignupResponse.class);
    }


    public CheckUserResponse getCheckStudentData(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, CheckUserResponse.class);
    }

    public FacebookUserprofile getFacebookUserProfile(String jsonString) {
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, FacebookUserprofile.class);
    }



}

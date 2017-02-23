package com.vpage.vcars.httputils;

import android.util.Log;

import com.vpage.vcars.pojos.request.SignInRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.vpage.vcars.R;
import com.vpage.vcars.pojos.request.CheckUserRequest;
import com.vpage.vcars.pojos.request.SignupRequest;
import com.vpage.vcars.pojos.request.VLocationTrackRequest;
import com.vpage.vcars.pojos.response.CheckUserResponse;
import com.vpage.vcars.pojos.response.SignInResponse;
import com.vpage.vcars.pojos.response.SignupResponse;
import com.vpage.vcars.pojos.response.VLocationTrack.VLocationTrackResponse;
import com.vpage.vcars.tools.VCarsApplication;
import com.vpage.vcars.tools.VCarRestTools;
import com.vpage.vcars.tools.VTools;
import com.vpage.vcars.tools.utils.LogFlag;

import org.json.JSONObject;
import java.io.UnsupportedEncodingException;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class VCarRestClient {


    private static final String TAG =  VCarRestClient.class.getName();

    private static final String CONTENT_TYPE_JSON = "application/json";

    JSONObject jsonParams = null;
    StringEntity parsedJsonParams = null;

    RequestParams requestParams = null;


    public static void cancelAllRequests() {
        HttpManager.asyncHttpClient.cancelAllRequests(true);
        HttpManager.syncHttpClient.cancelAllRequests(true);

    }



    public void setSignInParams(SignInRequest signInRequest) {

        try {
            Gson gson = new GsonBuilder().create();
            String jsonParams = gson.toJson(signInRequest);
            if (LogFlag.bLogOn)Log.d(TAG, jsonParams);
          //  parsedJsonParams = new StringEntity(VTools.getRequestWithAppVersion(jsonParams));
            parsedJsonParams = new StringEntity(jsonParams);


        } catch (UnsupportedEncodingException e) {
            if (LogFlag.bLogOn)Log.e(TAG, "ERROR: ", e);
        }
    }

    public SignInResponse signIn() {

        String signInUrl = VCarsApplication.getContext().getResources().getString(R.string.signin);
        if (LogFlag.bLogOn)Log.d(TAG, signInUrl);
        final SignInResponse[] signInResponses = {null};

        HttpManager.post(signInUrl, parsedJsonParams, new JsonHttpResponseHandler() {


            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {

                signInResponses[0] = VCarRestTools.getInstance().getSignInData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, signInResponses[0].toString());
            }


            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (LogFlag.bLogOn)Log.d(TAG, "Error " + statusCode);
                signInResponses[0] = null;
            }

        });
        return signInResponses[0];
    }

    public void setRegisterParams(SignupRequest registerRequest) {

        try {
            Gson gson = new GsonBuilder().create();
            String jsonParams = gson.toJson(registerRequest);
            parsedJsonParams = new StringEntity(VTools.getRequestWithAppVersion(jsonParams));

            if (LogFlag.bLogOn)Log.d(TAG, jsonParams);
        } catch (UnsupportedEncodingException e) {
            if (LogFlag.bLogOn)Log.e(TAG, "ERROR: ", e);
        }
    }

    public SignupResponse signup() {
        String registerUrl = VCarsApplication.getContext().getResources().getString(R.string.register);
        if (LogFlag.bLogOn)Log.d(TAG, registerUrl);
        final SignupResponse[] registerResponses = {null};

        HttpManager.post(registerUrl, parsedJsonParams, new JsonHttpResponseHandler() {


            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {

                registerResponses[0] = VCarRestTools.getInstance().getRegisterData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, registerResponses[0].toString());
            }

        });
        return registerResponses[0];
    }


    public CheckUserResponse checkStudent(CheckUserRequest checkUserRequest) {
        if (LogFlag.bLogOn)Log.d(TAG, "CheckUserResponse");
        final CheckUserResponse[] checkUserResponses = {null};
        String getCheckUserUrl = VCarsApplication.getContext().getResources().getString(R.string.check_student);

        getCheckUserUrl = getCheckUserUrl.replace("studentid", checkUserRequest.getStudentid());
        getCheckUserUrl = getCheckUserUrl + "?loginType=" + checkUserRequest.getLoginType();
        getCheckUserUrl = getCheckUserUrl + "&emailid=";
        getCheckUserUrl = getCheckUserUrl + "&devicePlatformName=" + checkUserRequest.getDevicePlatformName();
        getCheckUserUrl = getCheckUserUrl + "&deviceIdentity=" + checkUserRequest.getDeviceIdentity();
        getCheckUserUrl = getCheckUserUrl + "&deviceToken=" + checkUserRequest.getDeviceToken();
        getCheckUserUrl = getCheckUserUrl + "&deviceToken=" + checkUserRequest.getDeviceToken();
        getCheckUserUrl = getCheckUserUrl + "&fbTokenId=" + checkUserRequest.getFbTokenId();
        getCheckUserUrl = getCheckUserUrl + "&profilePic=" + checkUserRequest.getProfilePic();
        getCheckUserUrl = getCheckUserUrl + "&country=" + checkUserRequest.getCountry();

        if (LogFlag.bLogOn)Log.d(TAG, getCheckUserUrl);

        HttpManager.get(getCheckUserUrl, null, new JsonHttpResponseHandler() {


            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {

                checkUserResponses[0] = VCarRestTools.getInstance().getCheckStudentData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG, checkUserResponses[0].toString());
            }
        });
        return checkUserResponses[0];
    }


    public boolean doAction(final String url, String httpMethod, Object request) throws UnsupportedEncodingException {
        final boolean[] response = new boolean[1];
        if (httpMethod.equals(HttpMethod.GET)) {
            HttpManager.get(url, null, new JsonHttpResponseHandler() {


                public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {
                    response[0] = true;
                }


                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    if (LogFlag.bLogOn)Log.d(TAG, "onFailure: GET REST Call: " + url + " Failed");
                    response[0] = false;
                }
            });
        } else if (httpMethod.equals(HttpMethod.POST)) {
            StringEntity parsedJsonParams = null;
            if (request != null) {
                Gson gson = new GsonBuilder().create();
                String jsonParams = gson.toJson(request);
                parsedJsonParams = new StringEntity(VTools.getRequestWithAppVersion(jsonParams));

            }
            HttpManager.post(url, parsedJsonParams, new JsonHttpResponseHandler() {

                public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {
                    response[0] = true;
                }


                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    if (LogFlag.bLogOn)Log.d(TAG, "onFailure: POST REST Call: " + url + " Failed");
                    response[0] = false;
                }

            });
        } else if (httpMethod.equals(HttpMethod.PUT)) {
            HttpManager.put(url, null, new JsonHttpResponseHandler() {

                public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {
                    response[0] = true;
                }


                public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                    super.onFailure(statusCode, headers, responseString, throwable);
                    if (LogFlag.bLogOn)Log.d(TAG, "onFailure: PUT REST Call: " + url + " Failed");
                    response[0] = false;
                }
            });
        }
        return response[0];
    }


    public void locationStore(VLocationTrackRequest vLocationTrackRequest) {

        String locationStoreUrl = VCarsApplication.getContext().getResources().getString(R.string.store_location);

        locationStoreUrl = locationStoreUrl.replace("{user}", vLocationTrackRequest.getUser());
        locationStoreUrl = locationStoreUrl.replace("{lat}", vLocationTrackRequest.getLatitude()+"");
        locationStoreUrl = locationStoreUrl.replace("{lng}", vLocationTrackRequest.getLongitude()+"");
        locationStoreUrl = locationStoreUrl.replace("{location}",vLocationTrackRequest.getLocation());

        if (LogFlag.bLogOn)Log.d(TAG,"locationStoreUrl: "+locationStoreUrl);

        HttpManager.post(locationStoreUrl,null, new JsonHttpResponseHandler() {


            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {

                if (LogFlag.bLogOn)Log.d(TAG,"resultData: "+ resultData.toString());
            }


            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (LogFlag.bLogOn)Log.d(TAG, "Error " + statusCode);

            }

        });
    }


    public VLocationTrackResponse locationTrack(String user) {

        String locationTrackUrl = VCarsApplication.getContext().getResources().getString(R.string.track_location);

        locationTrackUrl = locationTrackUrl.replace("{user}",user);

        if (LogFlag.bLogOn)Log.d(TAG, locationTrackUrl);
        final VLocationTrackResponse[] vLocationTrackResponses = {null};

        HttpManager.post(locationTrackUrl,null, new JsonHttpResponseHandler() {


            public void onSuccess(int statusCode, Header[] headers, JSONObject resultData) {

                vLocationTrackResponses[0] = VCarRestTools.getInstance().getLocationTrackData(resultData.toString());
                if (LogFlag.bLogOn)Log.d(TAG,"vLocationTrackResponses: "+ vLocationTrackResponses[0].toString());
            }


            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (LogFlag.bLogOn)Log.d(TAG, "Error " + statusCode);
                vLocationTrackResponses[0] = null;
            }

        });
        return vLocationTrackResponses[0];
    }


}


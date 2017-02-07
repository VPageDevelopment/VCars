package com.vpage.vcars.tools;

import com.google.android.gms.common.api.GoogleApiClient;

import org.androidannotations.annotations.EBean;

@EBean(scope = EBean.Scope.Singleton)
public class VCarGooglePlusTools {


    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;

    public GoogleApiClient getmGoogleApiClient() {
        return mGoogleApiClient;
    }

    public void setmGoogleApiClient(GoogleApiClient mGoogleApiClient) {
        this.mGoogleApiClient = mGoogleApiClient;
    }
}

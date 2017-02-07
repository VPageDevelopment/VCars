package com.vpage.vcars.tools;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.vpage.vcars.tools.utils.LogFlag;


public class ConnectionDetector {

    private static final String TAG =ConnectionDetector.class.getName();


    Activity activity;

    public ConnectionDetector(Activity referActivity){
        this.activity = referActivity;
    }

    public boolean isConnectingToInternet(){
        try{
            ConnectivityManager connectivity = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null)
            {
                NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                NetworkInfo mobileinfo = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

                if (info != null)
                    if (info.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                if(mobileinfo!=null)
                    if (mobileinfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
            }
        }catch(Exception e){
            if (LogFlag.bLogOn) Log.e(TAG, e.getMessage(), e);
            return false;
        }
        return false;
    }
}

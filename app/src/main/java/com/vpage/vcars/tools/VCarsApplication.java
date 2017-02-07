package com.vpage.vcars.tools;

import android.app.Activity;
import android.content.Context;
import android.support.multidex.MultiDexApplication;
import android.util.Log;
import com.facebook.FacebookSdk;
import com.facebook.LoggingBehavior;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;
import com.vpage.vcars.BuildConfig;
import com.vpage.vcars.R;
import com.vpage.vcars.service.MyLifeCycleHandler;
import org.androidannotations.annotations.EApplication;


@EApplication
public class VCarsApplication extends MultiDexApplication {

    private static final String TAG = VCarsApplication.class.getName();


    private static Context mContext;
    private Tracker mTracker;

    private static boolean showSplash=false;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);    //To change body of overridden methods use File | Settings | File Templates.
        //MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        if(BuildConfig.DEBUG) {
            Log.d(TAG, "Runnning in debug mode");
        } else {
            Log.d(TAG, "Runnning in release mode");
        }

        FacebookSdk.sdkInitialize(getApplicationContext());
        FacebookSdk.addLoggingBehavior(LoggingBehavior.INCLUDE_ACCESS_TOKENS);

        registerActivityLifecycleCallbacks(new MyLifeCycleHandler());

    }


    synchronized public Tracker getDefaultTracker(Activity activity) {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(activity.getApplicationContext());
            // To enable debug logging use: adb shell setprop log.tag.GAv4 DEBUG
            mTracker = analytics.newTracker(activity.getResources().getString(R.string.ga_trackingId));
        }
        return mTracker;
    }

    public static boolean isShowSplash() {
        return showSplash;
    }

    public static void setShowSplash(boolean tShowSplash) {
        showSplash = tShowSplash;
    }

    public static Context getContext() {
        return mContext;
    }


}

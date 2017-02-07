package com.vpage.vcars.tools;

import android.app.Activity;
import android.util.Log;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.vpage.vcars.tools.utils.LogFlag;


import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EBean;

@EBean(scope = EBean.Scope.Singleton)
public class VGATools {

    private static final String TAG = VGATools.class.getName();

    @App
    VCarsApplication vCarsApplication;

    public  void reportPageViewToGoogle(Activity activity, String screenName) {

        Tracker tracker = vCarsApplication.getDefaultTracker(activity);

        // This screen name value will remain set on the tracker and sent with
        // hits until it is set to a new value or to null.
        tracker.setScreenName(screenName);
        if (LogFlag.bLogOn) Log.d(TAG, "page view " + screenName + " reported to google");

    }


    public  void reportActionToGoogle(Activity activity, String category, String action, String label) {

        Tracker tracker = vCarsApplication.getDefaultTracker(activity);

/*        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("UX")
                .setAction("click")
                .setLabel("submit")
                .build());*/

        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setLabel(label)
                .build());

        if (LogFlag.bLogOn) Log.d(TAG,  "action " + action +   " on " + label + " reported to google");
    }

}

package com.vpage.vcars.tools;



import android.app.Activity;
import android.util.Log;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import org.androidannotations.annotations.App;
import org.androidannotations.annotations.EBean;

@EBean(scope = EBean.Scope.Singleton)
public class VCarGATools {

    private static final String TAG = VCarGATools.class.getName();

    @App
    VCarsApplication vCarsApplication;



      public  void reportPageViewToGoogle(Activity activity, String screenName) {

          Tracker tracker = vCarsApplication.getDefaultTracker(activity);

            // This screen name value will remain set on the tracker and sent with
            // hits until it is set to a new value or to null.
          tracker.setScreenName(screenName);
          tracker.send(new HitBuilders.ScreenViewBuilder().build());
          GoogleAnalytics.getInstance(activity).dispatchLocalHits();
          Log.d(TAG, "page view " + screenName + " reported to google");
      }


    public  void reportActionToGoogle(Activity activity, String event, String action, String studentid) {

        Tracker tracker = vCarsApplication.getDefaultTracker(activity);


/*        tracker.send(new HitBuilders.EventBuilder()
                .setCategory("UX")
                .setAction("click")
                .setLabel("submit")
                .build());*/

        tracker.send(new HitBuilders.EventBuilder()
                .setCategory(event)
                .setAction(action)
                .setLabel(studentid)
                .build());

        Log.d(TAG, "action " + action +   " on " + event + " reported to google");

    }


}

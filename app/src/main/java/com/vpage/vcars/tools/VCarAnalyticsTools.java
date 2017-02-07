package com.vpage.vcars.tools;

import android.app.Activity;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

@EBean(scope = EBean.Scope.Singleton)
public class VCarAnalyticsTools {

    private static final String TAG = VCarAnalyticsTools.class.getName();

    @Bean
    VCarGATools vCarGATools;

    public   void  reportPageViews(Activity activity, String pageName) {

        vCarGATools.reportPageViewToGoogle(activity, pageName);
    }


    public void reportEvents(Activity activity, String eventId, String actionId, String studentid) {
        try {
            vCarGATools.reportActionToGoogle(activity, eventId, actionId, studentid);
        }catch (Exception e){

        }
    }


}

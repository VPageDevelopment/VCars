package com.vpage.vcars.tools;

import android.app.Activity;

import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EBean;

@EBean(scope = EBean.Scope.Singleton)
public class VAnalyticsTools {
    @Bean
    VGATools vgaTools;


    public   void  reportPageViews(Activity activity, String pageName) {


        vgaTools.reportPageViewToGoogle(activity, pageName);
    }


    public void reportEvents(Activity activity, String eventId, String actionId, String studentid) {
        try {
            vgaTools.reportActionToGoogle(activity, "UX", "Click", eventId);
        }catch (Exception e){

        }
    }

}

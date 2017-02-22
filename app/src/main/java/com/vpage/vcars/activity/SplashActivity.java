package com.vpage.vcars.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vpage.vcars.R;
import com.vpage.vcars.pojos.response.SignInResponse;
import com.vpage.vcars.tools.NetworkUtil;
import com.vpage.vcars.tools.VPreferences;
import com.vpage.vcars.tools.VTools;
import com.vpage.vcars.tools.utils.LogFlag;
import com.vpage.vcars.service.GCMClientManager;
import com.vpage.vcars.view.PlayGifView;
import org.androidannotations.annotations.*;


@WindowFeature({Window.FEATURE_NO_TITLE, Window.FEATURE_ACTION_BAR_OVERLAY})
@Fullscreen
@EActivity(R.layout.activity_splash)
public class SplashActivity extends Activity {

    private static final String TAG = SplashActivity.class.getName();

    private GCMClientManager pushClientManager;
    String PROJECT_NUMBER = "";

    int delay = 2000;

   /* @ViewById(R.id.splash_textV)
    TextView splash_textView;
*/
    @ViewById(R.id.mainlanding_layout)
    LinearLayout mainlanding_layout;

    @ViewById(R.id.splashImage)
    PlayGifView imageView;

    private boolean shouldKillActivity = false;


    @AfterViews
    public void onSplash() {
        if ((getIntent().getFlags() & Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT) != 0) {
            finish();
            return;
        }
        getGcmDeviceToken();
        setAnimStyle();

      // afterSplash();

       gotoHomePage();

    }


    private void gotoHomePage() {

        Intent intent = new Intent(getApplicationContext(), HomeActivity_.class);
        startActivity(intent);
        VTools.animation(this);
    }


    private void gotoCarRequestPage() {

        Intent intent = new Intent(getApplicationContext(), CurrentCarTrackActivity_.class);
        startActivity(intent);
        VTools.animation(this);
    }



    public void afterSplash() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                goToHome();


          /*      Boolean isAppInstalled = VPreferences.getAppInstallStatus("isInstalled");

                if (LogFlag.bLogOn) Log.d(TAG, "isInstalled: "+ isAppInstalled);

                // To check first installation by preference
                if (!isAppInstalled) {
                    gotoHelpScreenPage();
                } else {
                 //   gotoOTPGenerationPage();
                    gotoSignInPage();
                }*/
            }
        }, delay);
    }

    private void goToHome() {
        try {
            String isLoggedIn = VPreferences.get("isLoggedIn");
            String userdata  = VPreferences.get("userdata");

            if (isLoggedIn == null || isLoggedIn.isEmpty() || null == userdata || userdata.isEmpty()) {
                gotoGoogleSignInPage();
            } else {

                Gson gson = new GsonBuilder().create();
                VPreferences.save("activeUser", gson.toJson(userdata));
                Intent intent = new Intent(getApplicationContext(), HomeActivity_.class);
                intent.putExtra("ActiveUser", gson.toJson(VTools.getActiveUserFromUserData(userdata)));
                startActivity(intent);
                VTools.animation(this);
                finish();
            }
        } catch (Exception e) {
            if (LogFlag.bLogOn)Log.e(TAG,  e.getMessage());
        }
    }


    private void setAnimStyle() {

       /* final Animation splash_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splashanim);
        imageView.setVisibility(View.VISIBLE);
        imageView.startAnimation(splash_anim);*/

        imageView.setVisibility(View.VISIBLE);
        imageView.setImageResource(R.mipmap.splashimage);

    }

    private void gotoOTPGenerationPage() {
        shouldKillActivity = true;
        if (!checkInternetStatus()) {
            return;
        }
       Intent intent = new Intent(getApplicationContext(), GenerateOTPActivity_.class);
       startActivity(intent);
       VTools.animation(this);
    }

    private void gotoSignInPage() {
        shouldKillActivity = true;
        if (!checkInternetStatus()) {
            return;
        }
        Intent intent = new Intent(getApplicationContext(), SigninActivity_.class);
        startActivity(intent);
        VTools.animation(this);
    }

    private void gotoSignUpPage() {
        shouldKillActivity = true;
        if (!checkInternetStatus()) {
            return;
        }
        Intent intent = new Intent(getApplicationContext(), SignupActivity_.class);
        startActivity(intent);
        VTools.animation(this);
    }

    public  boolean checkInternetStatus() {
        String status = NetworkUtil.getConnectivityStatusString(this);
        boolean isNetworkAvailable;
        switch (status) {
            case "Connected to Internet with Mobile Data":
                isNetworkAvailable = true;
                break;
            case "Connected to Internet with WIFI":
                isNetworkAvailable = true;
                break;
            default:

                isNetworkAvailable = false;
                break;
        }
        VTools.showSnackBarBasedOnStatus(isNetworkAvailable,getString(R.string.noInternetMsg));
        return isNetworkAvailable;
    }


    private void gotoHelpScreenPage() {
        VPreferences.saveAppInstallStatus("isInstalled",true);
        Intent intent = new Intent(getApplicationContext(), HelpScreenActivity_.class);
        startActivity(intent);
        VTools.animation(this);

    }


    private void gotoGoogleSignInPage() {

        Intent intent = new Intent(getApplicationContext(), LoginActivity_.class);
        startActivity(intent);
        VTools.animation(this);
        finish();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (shouldKillActivity) {
            finish();
        }

        VTools.setSnackBarElements(mainlanding_layout, SplashActivity.this);
    }

    private void getGcmDeviceToken(){
        try {
            PROJECT_NUMBER = getString(R.string.G_PROJECT_NUMBER);
            if (LogFlag.bLogOn) Log.d(TAG, "PROJECT_NUMBER: "+ PROJECT_NUMBER);
            pushClientManager = new GCMClientManager(this, PROJECT_NUMBER);
            pushClientManager.registerIfNeeded(new GCMClientManager.RegistrationCompletedHandler() {
                @Override
                public void onSuccess(String registrationId, boolean isNewRegistration) {
                    if (LogFlag.bLogOn) Log.d(TAG, "gcmToken: "+ registrationId);
                    VPreferences.save("gcmToken", registrationId);
                }

                @Override
                public void onFailure(String error) {
                    if (LogFlag.bLogOn) Log.e(TAG, error);
                    super.onFailure(error);
                }
            });
        }catch (Exception e){
            if (LogFlag.bLogOn) Log.e(TAG, e.getMessage());
        }
    }
}

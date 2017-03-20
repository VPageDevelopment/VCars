package com.vpage.vcars.tools;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vpage.vcars.R;
import com.vpage.vcars.pojos.ActiveUser;
import com.vpage.vcars.pojos.VLocation;
import com.vpage.vcars.pojos.response.SignInResponse;
import com.vpage.vcars.tools.utils.LogFlag;
import com.vpage.vcars.tools.fab.FloatingActionButton;
import org.json.JSONException;
import org.json.JSONObject;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBarUtils;
import fr.castorflex.android.smoothprogressbar.SmoothProgressDrawable;

public class VTools {

    private static final String TAG =VTools.class.getName();

    private static final Object monitor = new Object();
    private static VTools vTools = null;

    private static Snackbar snackbar;
    private static LinearLayout layout;
    private static Activity activity;


    public static DisplayMetrics displayMetrics;
    public static WindowManager windowManager;
    public static int os;

    public static VTools getInstance() {
        if (vTools == null) {
            synchronized (monitor) {
                if (vTools == null)
                    vTools = new VTools();
            }
        }
        return vTools;
    }

    public static void setActivity(Activity homeActivity) {
        activity = homeActivity;
    }

    public static Activity getActivity() {
        return activity;
    }

    public static void forwardTransition(Activity transitionActivity) {
        transitionActivity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }


    public static void setDisplayMetrics() {
        os = Build.VERSION.SDK_INT;
        displayMetrics = new DisplayMetrics();
        windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE); // the results will be higher than using the activity context object or the getWindowManager() shortcut
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
    }


    public static void setSnackBarElements(LinearLayout linearLayout, final Activity referenceActivity) {
        layout = linearLayout;
        activity = referenceActivity;
    }
    public static void animation(Activity activity) {
        activity.overridePendingTransition(R.anim.from_middle, R.anim.to_middle);
    }


    public static void showSnackBarBasedOnStatus(boolean netStatus, String message) {

        if (netStatus) {
            if (snackbar != null) {
                if (snackbar.isShown()) {
                    snackbar.dismiss();
                }
            }
        } else {
            snackbar = Snackbar
                    .make(layout, message, Snackbar.LENGTH_INDEFINITE)
                    .setAction("HIDE", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });
            View view = snackbar.getView();
            float height = view.getHeight();
            view.setScaleY((float) .8);
            view.setY(height * .2F);

            TextView tv = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
            TextView tv1 = (TextView) view.findViewById(android.support.design.R.id.snackbar_action);
            tv1.setTextSize(14);
            tv1.setTextColor(activity.getResources().getColor(R.color.common_button_color));
            tv.setTextSize(14);
            tv.setTextColor(activity.getResources().getColor(R.color.common_button_color));
            snackbar.show();
        }
    }

    public static void showToast(String message) {
        try {
            final Context context = VCarsApplication.getContext();
            final CharSequence text = message;
            final int duration = Toast.LENGTH_SHORT;
            Handler mainHandler = new Handler(context.getMainLooper());
            Runnable myRunnable = new Runnable() {
                @Override
                public void run() {
                    Toast toast = Toast.makeText(context, "  " + text + "  ", duration);
                    toast.show();
                }
            };
            mainHandler.post(myRunnable);
        } catch (Exception e) {
            if (LogFlag.bLogOn) Log.e(TAG, e.toString());
        }

    }

    public static Boolean checkNetworkConnection(Activity activity) {
        ConnectionDetector connectionDetector = new ConnectionDetector(activity);
        Boolean isInternetPresent = connectionDetector.isConnectingToInternet();
        return isInternetPresent;
    }


    private static String chosenProfileImage;

    public static String getChosenProfileImage() {
        return chosenProfileImage;
    }

    public static void setChosenProfileImage(String chosenProfileImageNew) {
        chosenProfileImage = chosenProfileImageNew;
    }


    public static  void setProgressLoader(SmoothProgressBar loader_progressbar, Activity activity){
        loader_progressbar.setSmoothProgressDrawableBackgroundDrawable(
                SmoothProgressBarUtils.generateDrawableWithColors(
                        activity.getResources().getIntArray(R.array.gplus_colors),
                        ((SmoothProgressDrawable) loader_progressbar.getIndeterminateDrawable()).getStrokeWidth()));
    }


    public static  void StartProgressLoader(SmoothProgressBar loader_progressbar){
        loader_progressbar.setVisibility(View.VISIBLE);
        loader_progressbar.progressiveStart();
    }
    public static  void HideProgressLoader(SmoothProgressBar loader_progressbar){
        loader_progressbar.setVisibility(View.GONE);
        loader_progressbar.progressiveStop();
    }

    public static PopupWindow createPopUp(View popUpView) {
        PopupWindow mpopup = new PopupWindow(popUpView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true); //Creation of popup
        try {
            mpopup.setAnimationStyle(android.R.style.Animation_Dialog);
            mpopup.showAtLocation(popUpView, Gravity.CENTER_VERTICAL, 0, 0);    // Displaying popup

        } catch (Exception e) {
            if (LogFlag.bLogOn) Log.e(TAG, e.toString());
        }
        return mpopup;
    }

    public static void setFABStyle(FloatingActionButton FAB) {
        FAB.setSize(FloatingActionButton.SIZE_MINI);
        FAB.setColorNormalResId(R.color.colorPrimary);
        FAB.setColorPressedResId(R.color.White);
    }

    @SuppressLint("NewApi")
    public static void setLayoutBackgroud(RelativeLayout layout, int id){
        try{
            Drawable d;
            int sdk= Build.VERSION.SDK_INT;
            if(sdk> Build.VERSION_CODES.LOLLIPOP) {
                d=VCarsApplication.getContext().getDrawable(id);
            }else {
                d=VCarsApplication.getContext().getResources().getDrawable(id);
            }
            if(sdk< Build.VERSION_CODES.JELLY_BEAN) {
                layout.setBackgroundDrawable(d);
            }else {
                layout.setBackground(d);
            }
        }catch (Exception e){
            if (LogFlag.bLogOn) Log.e(TAG, e.toString());
        }
    }



    public static String getRequestWithAppVersion(String jsonParams) {
        JSONObject jsonObject = null;
        Log.d(TAG, "Additional request data");
        try {
            PackageInfo pinfo = VCarsApplication.getContext().getPackageManager().getPackageInfo(VCarsApplication.getContext().getPackageName(), 0);
            Log.d("pinfoCode",String.valueOf(pinfo.versionCode));
            Log.d("pinfoName",pinfo.versionName);
            jsonObject = new JSONObject(jsonParams);
            jsonObject.put("appVersion",pinfo.versionName);
            jsonObject.put("devicePlatformName", "Android");
        } catch (JSONException e) {
            if (LogFlag.bLogOn) Log.d(TAG, e.toString());
        } catch (PackageManager.NameNotFoundException e) {
            if (LogFlag.bLogOn) Log.d(TAG, e.toString());
        }
        Log.d(TAG, "AppVersion & DeVicePlatform-->"+jsonObject.toString());
        return jsonObject.toString();
    }


    public static ActiveUser getActiveUser(SignInResponse signInResponse) {
        ActiveUser activeUser = new ActiveUser();
        try {
            activeUser.setUserId(signInResponse.getUserId());
            activeUser.setUserDisplayName(signInResponse.getUserDisplayName());
            if(signInResponse.getLoginType()!=null) {
                if (signInResponse.getLoginType().equalsIgnoreCase(LoginType.VCars.name())) {
                    activeUser.setProfileImage(signInResponse.getUserProfileImage());
                }
                activeUser.setLoginType(signInResponse.getLoginType());
            }
            activeUser.setDeviceIdentity(signInResponse.getDeviceIdentity());
            activeUser.setDevicePlatformName(signInResponse.getDevicePlatformName());
            activeUser.setEmail(signInResponse.getEmail());
            activeUser.setGoogleStore(signInResponse.getGoogleStore());
            activeUser.setLoginType(signInResponse.getLoginType());

        }catch (Exception e){
            if (LogFlag.bLogOn) Log.e(TAG, e.toString());
        }
        return activeUser;
    }

    public ActiveUser getActiveUserData(String jsonString) {
        //    if (LogFlag.bLogOn) Log.d(TAG, jsonString);
        Gson gson = new GsonBuilder().create();
        ActiveUser activeUser = gson.fromJson(jsonString, ActiveUser.class);
        return activeUser;
    }


    public static ActiveUser getActiveUserFromUserData(String userData) {

        ActiveUser  userDataValues = VTools.getInstance().getActiveUserData(userData);
        ActiveUser activeUser = new ActiveUser();

        activeUser.setUserId(userDataValues.getUserId());
        activeUser.setUserDisplayName(userDataValues.getUserDisplayName());
        if(userDataValues.getLoginType()!=null) {
            if (userDataValues.getLoginType().equalsIgnoreCase(LoginType.VCars.name())) {
                activeUser.setProfileImage(userDataValues.getProfileImage());
            }
            activeUser.setLoginType(userDataValues.getLoginType());
        }
        activeUser.setDeviceIdentity(userDataValues.getDeviceIdentity());
        activeUser.setDevicePlatformName(userDataValues.getDevicePlatformName());
        activeUser.setEmail(userDataValues.getEmail());
        activeUser.setGoogleStore(userDataValues.getGoogleStore());
        activeUser.setLoginType(userDataValues.getLoginType());

        return activeUser;
    }



    public VLocation getLocationData(String jsonString) {
        if(jsonString == null || jsonString.length() ==0) {
            return null;
        }
        Log.d(TAG, jsonString);
        Gson gson = new GsonBuilder().create();
        return gson.fromJson(jsonString, VLocation.class);
    }

    public static Typeface getAvenirLTStdRoman() {
        Typeface   typeface = Typeface.createFromAsset(VCarsApplication.getContext().getAssets(),"AvenirLTStd-Roman.otf");
        return typeface;
    }
    public static Typeface getAvenirLTStdBlack() {
        Typeface   typeface = Typeface.createFromAsset(VCarsApplication.getContext().getAssets(), "AvenirLTStd-Black.otf");
        return typeface;
    }

    public static Typeface getAvenirLTStdBook() {
        Typeface   typeface = Typeface.createFromAsset(VCarsApplication.getContext().getAssets(),"AvenirLTStd-Book.otf");
        return typeface;
    }

    public static Typeface getAvenirLTStdHeavy() {
        Typeface   typeface = Typeface.createFromAsset(VCarsApplication.getContext().getAssets(),"AvenirLTStd-Heavy.otf");
        return typeface;
    }

    public static Typeface getAvenirLTStdLight() {
        Typeface   typeface = Typeface.createFromAsset(VCarsApplication.getContext().getAssets(),"AvenirLTStd-Light.otf");
        return typeface;
    }

    public static Typeface getAvenirLTStdMedium() {
        Typeface   typeface = Typeface.createFromAsset(VCarsApplication.getContext().getAssets(),"AvenirLTStd-Medium.otf");
        return typeface;
    }
    public static Typeface getLithosProBlack() {
        Typeface   typeface = Typeface.createFromAsset(VCarsApplication.getContext().getAssets(),"Lithos-Pro-Black_28554.ttf");
        return typeface;
    }


    public static void showAlertDialog(Activity activity,String message) {

        TextView title = new TextView(activity);
        // You Can Customise your Title here
        title.setText(activity.getResources().getString(R.string.app_name));
        title.setBackgroundColor(Color.BLACK);
        title.setPadding(10, 15, 15, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(20);

        AlertDialog alertDialog = new AlertDialog.Builder(activity).create();
        alertDialog.setCustomTitle(title);
        alertDialog.setMessage(message);

        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();

    }


}

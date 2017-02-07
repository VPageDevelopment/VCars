package com.vpage.vcars.tools.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import com.vpage.vcars.tools.VCarsApplication;


public class AlertUtils {

    private static final String TAG = "OustAndroid:" + AlertUtils.class.getName();

    private static String title;

    private static String message;



    private static final Object monitor = new Object();
    private static AlertUtils alertUtils = null;

    public static AlertUtils getInstance() {
        if (alertUtils == null) {
            synchronized (monitor) {
                if (alertUtils == null)
                    alertUtils = new AlertUtils();
            }
        }
        return alertUtils;
    }

    public static void setTitle(String title) {
        AlertUtils.title = title;
    }

    public static void setMessage(String message) {
        AlertUtils.message = message;
    }



    public static void showConfirm() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(VCarsApplication.getContext());

        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        // set positive button: Yes message
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // go to a new activity of the app
                //         delegate.confirmResponseClick(id);
                dialog.cancel();

            }
        });
        // set negative button: No message
        alertDialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // cancel the alert box the user
                //          delegate.confirmResponseClick(id);
                dialog.cancel();

            }
        });


        AlertDialog alertDialog = alertDialogBuilder.create();
        // show alert
        alertDialog.show();
    }

    public static void showAlert(Activity activity) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(activity);

        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setMessage(message);
        // set positive button: Yes message
        alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // go to a new activity of the app
                //          delegate.alertResponseClick(id);
                dialog.cancel();

            }
        });


        AlertDialog alertDialog = alertDialogBuilder.create();
        // show alert
        alertDialog.show();
    }


}

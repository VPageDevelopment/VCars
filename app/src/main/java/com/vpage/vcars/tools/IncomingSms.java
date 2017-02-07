package com.vpage.vcars.tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;
import android.util.Log;
import com.vpage.vcars.tools.utils.LogFlag;


public class IncomingSms extends BroadcastReceiver {


    private static final String TAG = IncomingSms.class.getName();


    public void onReceive(Context context, Intent intent) {

        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();

        try {


            String messageBody ="";




            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for(int i=0;i<pdusObj.length;i++) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);

                    String sender = smsMessage.getDisplayOriginatingAddress();
                    //You must check here if the sender is your provider and not another one with same text.

                    messageBody = smsMessage.getMessageBody();
                }
                if (LogFlag.bLogOn) Log.d(TAG,"SMS Received: "+messageBody);

                    Intent myIntent = new Intent("otp");
                    myIntent.putExtra("message",messageBody);
                    LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                    // Show Alert


            } // bundle is null

        } catch (Exception e) {
            if (LogFlag.bLogOn) Log.e(TAG, "Exception smsReceiver" +e);

        }
    }
}

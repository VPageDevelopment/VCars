package com.vpage.vcars.tools;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import com.vpage.vcars.tools.utils.LogFlag;

public class SMSReceiver extends BroadcastReceiver
{

    private static final String TAG = SMSReceiver.class.getName();
    private static SmsListener mListener;

    @Override
    public void onReceive(Context context, Intent intent)
    {
        /*// Get Bundle object contained in the SMS intent passed in
        Bundle bundle = intent.getExtras();
        SmsMessage[] smsm = null;
        String sms_str ="";
        if (bundle != null)
        {
            // Get the SMS message
            Object[] pdus = (Object[]) bundle.get("pdus");
            smsm = new SmsMessage[pdus.length];
            for (int i=0; i<smsm.length; i++){
                smsm[i] = SmsMessage.createFromPdu((byte[])pdus[i]);
                sms_str += "Sent From: " + smsm[i].getOriginatingAddress();
                sms_str += "\r\nMessage: ";
                sms_str += smsm[i].getMessageBody().toString();
                sms_str+= "\r\n";
            }
            if (LogFlag.bLogOn) Log.d(TAG,"SMS Received: "+sms_str);
            // Start Application's  MainActivty activity
            Intent smsIntent=new Intent(context,LoginActivity_.class);
            smsIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            smsIntent.putExtra("sms_str", sms_str);
            context.startActivity(smsIntent);
        }*/


        Bundle data  = intent.getExtras();

        Object[] pdus = (Object[]) data.get("pdus");
        String messageBody ="";

        for(int i=0;i<pdus.length;i++) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

            String sender = smsMessage.getDisplayOriginatingAddress();
            //You must check here if the sender is your provider and not another one with same text.

           messageBody = smsMessage.getMessageBody();
        }
        if (LogFlag.bLogOn) Log.d(TAG,"SMS Received: "+messageBody);
        if(!messageBody.isEmpty()){
            //Pass on the text to our listener.
            mListener.messageReceived(messageBody);
        }

    }


    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}

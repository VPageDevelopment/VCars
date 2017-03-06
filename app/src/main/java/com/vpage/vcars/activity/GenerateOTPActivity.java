package com.vpage.vcars.activity;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.vpage.vcars.R;
import com.vpage.vcars.tools.VTools;
import com.vpage.vcars.tools.utils.LogFlag;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import java.util.ArrayList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

@EActivity(R.layout.activity_generateotp)
public class GenerateOTPActivity extends Activity {

    private static final String TAG = GenerateOTPActivity.class.getName();

    @ViewById(R.id.txtPhoneNo)
    EditText txtPhoneNo;

    @ViewById(R.id.btnGenerateOtp)
    Button btnGenerateOtp;

    private static final int PERMISSION_SEND_SMS = 123;
    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 24601;

    private String phoneNumber,generatedOTP;

    @AfterViews
    public void initLoginView() {


        /*
        Intent sendIntent = new Intent(Intent.ACTION_VIEW);
        sendIntent.putExtra("sms_body", "Content of the SMS goes here...");
        sendIntent.setType("vnd.android-dir/mms-sms");
        startActivity(sendIntent);
        */

        btnGenerateOtp.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {

                phoneNumber = txtPhoneNo.getText().toString();
                // To Do get the members phone number from server or DB
                String oldUserPhoneNumber = "6788990003";
                generateOTP(oldUserPhoneNumber);
            }
        });


    }


 /*   @Override
    protected void onResume() {
        super.onResume();
        SMSReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                if (LogFlag.bLogOn) Log.d(TAG,"SMSReceiver: "+messageText);
                gotoLoginPage(messageText);
            }
        });
    }*/




    @UiThread
    public void generateOTP(String oldUserPhoneNumber) {


        if (phoneNumber != null) {
            // User ID is verified
            if(phoneNumber.equals(oldUserPhoneNumber)) {

                if (LogFlag.bLogOn) Log.d(TAG,"Already User ");

            }else {

                generatedOTPProcessFinish();
            }
        }else {
            if (LogFlag.bLogOn)Log.d(TAG," Please enter phoneNumber");
        }

    }


    @Background
    void generatedOTPProcessFinish() {


        // Server generates an OTP and waits for client to send this
        Random r = new Random();
        String otp = new String();
        for(int i=0 ; i < 8 ; i++) {
            otp += r.nextInt(10);
        }

        // Server starts a timer of 10 seconds during which the OTP is valid.
        TimeOutTask task = new TimeOutTask();
        Timer t = new Timer();
        t.schedule(task, 100000L);

        if(task.isTimedOut) {
            // User took more than 100 seconds and hence the OTP is invalid
            if (LogFlag.bLogOn)Log.d(TAG, "PASSWORD: " + "Time out!");
        } else {
            if (LogFlag.bLogOn)Log.d(TAG,"OTP GENERATED ");
            if (LogFlag.bLogOn)Log.d(TAG,"otp: "+otp);
            generatedOTP = otp;
            //requestSmsPermission();    //to be used for developing
            gotoLoginPage();   //for testing only
        }

        //Calling register method on register button click
        //  register();


        // Working code for Password generation
        // int passwordSize = 4;
        //  Log.d(TAG,"PASSWORD: "+new PasswordBuilder().addCharsOption("!@#$%&*()_-+=[]{}\\|:/?.,><", 1).addRangeOption('A', 'Z', 1).addRangeOption('a', 'z', 0).addRangeOption('0', '9', 1).setSize(passwordSize).build());


         /*
  // Working code for Password generation
               TOTP totp = new TOTP();

        String seed = "3132333435363738393031323334353637383930";
        long T0 = 0;
        long X = 30;
        long testTime[] = {59, 1111111109, 1111111111,1234567890, 2000000000};
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));

        try{


            for(int i=0; i<testTime.length; i++) {
                long T = (testTime[i] - T0)/X;
                steps = Long.toHexString(T).toUpperCase();
                while(steps.length() < 16) steps = "0" + steps;
                String fmtTime = String.format("%1$-10s", testTime[i]);
                String utcTime = df.format(new Date(testTime[i]*1000));

                 if (LogFlag.bLogOn)Log.d(TAG,"PASSWORD: "+totp.generateTOTP(seed, steps, "4", "HmacSHA1") );

                if (LogFlag.bLogOn)Log.d(TAG,"PASSWORD: "+totp.generateTOTP(seed, steps, "4", "HmacSHA256") );

                 if (LogFlag.bLogOn)Log.d(TAG,"PASSWORD: "+totp.generateTOTP(seed, steps, "4", "HmacSHA512"));

            }
        }catch (final Exception e){
             if (LogFlag.bLogOn)Log.d(TAG,"PASSWORD: "+  e.getMessage());
        }
*/

    }

    class TimeOutTask extends TimerTask {
        boolean isTimedOut = false;

        public void run() {
            isTimedOut = true;
        }
    }



    private void requestSmsPermission() {

        // check permission is given
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            // request permission (see result in onRequestPermissionsResult() method)
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, PERMISSION_SEND_SMS);
        } else {
            // permission already granted run sms send

            String message = "This the OTP number for user Mapee app "+generatedOTP;
            if (phoneNumber.length()>0 && message.length()>0){
                sendSMS(message);
            }
            else{
                Toast.makeText(getBaseContext(),
                        "Please enter both phone number and message.",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_CONTACTS: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted

                    String message = "This the OTP number for user Mapee app "+generatedOTP;
                    if (phoneNumber.length()>0 && message.length()>0){
                        sendSMS(message);
                    }
                    else{
                        Toast.makeText(getBaseContext(),
                                "Please enter both phone number and message.",
                                Toast.LENGTH_SHORT).show();
                    }

                } else {
                    // permission denied
                    Toast.makeText(getBaseContext(),
                            "Please enter both phone number and message.",
                            Toast.LENGTH_SHORT).show();
                }
                return;
            }
        }
    }




    private void sendSms( String message){
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }

    //---sends a SMS message to another device---
    private void sendSMS( String message)
    {
    	/*
        PendingIntent pi = PendingIntent.getActivity(this, 0,
                new Intent(this, test.class), 0);
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNumber, null, message, pi, null);
        */

       /* if (LogFlag.bLogOn)Log.d(TAG, "sendSMS Called");

        String SENT = "SMS_SENT";
        String DELIVERED = "SMS_DELIVERED";

        PendingIntent sentPI = PendingIntent.getBroadcast(this, 0,
                new Intent(SENT), 0);

        PendingIntent deliveredPI = PendingIntent.getBroadcast(this, 0,
                new Intent(DELIVERED), 0);

        //---when the SMS has been sent---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS sent",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        Toast.makeText(getBaseContext(), "Generic failure",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NO_SERVICE:
                        Toast.makeText(getBaseContext(), "No service",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        Toast.makeText(getBaseContext(), "Null PDU",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        Toast.makeText(getBaseContext(), "Radio off",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(SENT));

        //---when the SMS has been delivered---
        registerReceiver(new BroadcastReceiver(){
            @Override
            public void onReceive(Context arg0, Intent arg1) {
                switch (getResultCode())
                {
                    case Activity.RESULT_OK:
                        Toast.makeText(getBaseContext(), "SMS delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                    case Activity.RESULT_CANCELED:
                        Toast.makeText(getBaseContext(), "SMS not delivered",
                                Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new IntentFilter(DELIVERED));
*/

        try {
            SmsManager smsManager = SmsManager.getDefault();
            ArrayList<String> parts = smsManager.divideMessage(message);
            smsManager.sendMultipartTextMessage(phoneNumber, null, parts, null, null);
        } catch (Exception e) {
            if (LogFlag.bLogOn)Log.e(TAG, e.getMessage());
        }

        gotoLoginPage();
    }


    private void gotoLoginPage() {

        Intent intent = new Intent(getApplicationContext(), SigninActivity_.class);
        intent.putExtra("OTP",generatedOTP);
        startActivity(intent);
        VTools.animation(this);
        finish();
    }
}

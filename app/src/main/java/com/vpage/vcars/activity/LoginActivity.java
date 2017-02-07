package com.vpage.vcars.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import com.vpage.vcars.R;
import com.vpage.vcars.tools.IncomingSms;
import com.vpage.vcars.tools.VTools;
import com.vpage.vcars.tools.utils.LogFlag;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = LoginActivity.class.getName();

    @ViewById(R.id.editTextUsername)
    EditText editTextUsername;

    @ViewById(R.id.editTextPassword)
    EditText editTextPassword;

    @ViewById(R.id.editMobileNumber)
    EditText editMobileNumber;

    @ViewById(R.id.editOTP)
    EditText editOTP;

    private AppCompatButton buttonNext;

    //String variables to hold username password and phone
    private String username;
    private String password;
    private String phoneNumber;
    String generatedOTP;

    @AfterViews
    public void initLoginView() {

        Intent callingIntent=getIntent();

        generatedOTP = callingIntent.getStringExtra("OTP");


        buttonNext = (AppCompatButton) findViewById(R.id.buttonNext);


        //Adding a listener to button
        buttonNext.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {

        // To Do get the members phone number from server or DB
        String oldUserPhoneNumber = "6788990003";

        username = editTextUsername.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
        phoneNumber = editMobileNumber.getText().toString().trim();

        // for test placed static data
        generatedOTP = "9999";

       if(generatedOTP.equals(editOTP.getText().toString().trim())){
           gotoHomePage();
       }else {
           if (LogFlag.bLogOn) Log.d(TAG,"Invalid OTP ");
       }
    }

    private void gotoHomePage() {

        Intent intent = new Intent(getApplicationContext(), HomeActivity_.class);
        startActivity(intent);
        VTools.animation(this);
    }



    private IncomingSms receiver = new IncomingSms() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                if (LogFlag.bLogOn) Log.d(TAG,"SMSReceiver: "+message);

            }
        }
    };

    @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("AddedItem"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }


}

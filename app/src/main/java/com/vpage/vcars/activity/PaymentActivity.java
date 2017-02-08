package com.vpage.vcars.activity;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.vpage.vcars.R;
import com.vpage.vcars.tools.utils.LogFlag;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_paymemt)
@Fullscreen
public class PaymentActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = PaymentActivity.class.getName();

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.buttonPayment)
    Button buttonPayment;

    String selectedCar;

    @AfterViews
    public void onInitView() {

        Intent callingIntent=getIntent();

        selectedCar = callingIntent.getStringExtra("SelectedCar");

        setActionBarSupport();

        setView();
    }


    private void setActionBarSupport() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Payment");

    }

    private void setView() {

        buttonPayment.setOnClickListener(this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            if (LogFlag.bLogOn) Log.d(TAG, "Back Pressed ");
            onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
    }


}


package com.vpage.vcars.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.vpage.vcars.R;
import com.vpage.vcars.tools.VTools;
import com.vpage.vcars.tools.utils.LogFlag;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;

import java.util.Calendar;


@EActivity(R.layout.activity_carrequest)
@Fullscreen
public class CarRequestActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private static final String TAG = CarRequestActivity.class.getName();

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.buttonRequest)
    Button buttonRequest;

    @ViewById(R.id.carRequestDay)
    Button carRequestDay;

    @ViewById(R.id.radioLocality)
    RadioGroup radioLocality;

    @ViewById(R.id.radioLocal)
    RadioButton radioLocal;

    @ViewById(R.id.radioOutStation)
    RadioButton radioOutStation;

    @ViewById(R.id.localStation)
    TextView localStation;

    @ViewById(R.id.carRequestDate)
    TextView carRequestDateView;

    @ViewById(R.id.outStation)
    EditText outStation;

    @ViewById(R.id.outStationLayout)
    TextInputLayout outStationLayout;

    String selectedCar;


    private int year;
    private int month;
    private int day;

    static final int DATE_PICKER_ID = 1111;

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
        getSupportActionBar().setTitle(selectedCar+"");

    }

    private void setView() {

        buttonRequest.setOnClickListener(this);
        carRequestDay.setOnClickListener(this);
        radioLocality.setOnCheckedChangeListener(this);
        setCurrentDate();
        carRequestDateView.setText(String.valueOf((new StringBuilder().append(month+1).append("-").append(day).append("-")
                .append(year).append(" "))));
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

        switch (v.getId()){
            case R.id.buttonRequest:
                gotoPaymentPage();
                break;

            case R.id.carRequestDay:
                // On button click show datepicker dialog
                showDialog(DATE_PICKER_ID);
                break;
        }

    }

    private void gotoPaymentPage() {

        Intent intent = new Intent(getApplicationContext(), PaymentActivity_.class);
        intent.putExtra("SelectedCar","Car Selected");
        startActivity(intent);
        VTools.animation(this);
    }


    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if(radioLocal.isChecked())
        {
            localStation.setVisibility(View.VISIBLE);
            outStationLayout.setVisibility(View.GONE);
        }
        else if(radioOutStation.isChecked())
        {
            outStationLayout.setVisibility(View.VISIBLE);
            localStation.setVisibility(View.GONE);

        }
    }


    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_PICKER_ID:

                // open datepicker dialog.
                // set date picker for current date
                // add pickerListener listner to date picker
                return new DatePickerDialog(this, pickerListener, year, month,day);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {

        // when dialog box is closed, below method will be called.
        @Override
        public void onDateSet(DatePicker view, int selectedYear,
                              int selectedMonth, int selectedDay) {

            year  = selectedYear;
            month = selectedMonth;
            day   = selectedDay;


            // set selected date
            String carRequestDate = String.valueOf((new StringBuilder()
                    // Month is 0 based, just add 1
                    .append(month + 1).append("-").append(day).append("-")
                    .append(year).append(" ")));
            carRequestDateView.setText(carRequestDate);
            if (LogFlag.bLogOn) Log.d(TAG, "Car Request Date: "+carRequestDate);

        }
    };


    public void setCurrentDate() {

        final Calendar calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

    }


}

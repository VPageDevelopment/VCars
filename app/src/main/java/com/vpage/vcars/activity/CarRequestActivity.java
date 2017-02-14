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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


@EActivity(R.layout.activity_carrequest)
@Fullscreen
public class CarRequestActivity extends AppCompatActivity implements View.OnClickListener, RadioGroup.OnCheckedChangeListener {

    private static final String TAG = CarRequestActivity.class.getName();

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.buttonPick)
    Button buttonPick;

    @ViewById(R.id.carRequestFromDay)
    Button carRequestFromDay;

    @ViewById(R.id.carRequestToDay)
    Button carRequestToDay;

    @ViewById(R.id.radioLocality)
    RadioGroup radioLocality;

    @ViewById(R.id.radioLocal)
    RadioButton radioLocal;

    @ViewById(R.id.radioOutStation)
    RadioButton radioOutStation;

    @ViewById(R.id.localStation)
    TextView localStation;

    @ViewById(R.id.carRequestDays)
    TextView carRequestDays;

    @ViewById(R.id.outStation)
    EditText outStation;

    @ViewById(R.id.outStationLayout)
    TextInputLayout outStationLayout;

    @ViewById(R.id.errorText)
    TextView errorText;

    String selectedCar;

    String requestLocation = "",requestFromDate = "",requestToDate = "",noOfRequestDays;

    boolean dateSelectedFrom = false;


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

        buttonPick.setOnClickListener(this);
        carRequestFromDay.setOnClickListener(this);
        carRequestToDay.setOnClickListener(this);
        radioLocality.setOnCheckedChangeListener(this);


        if(radioLocal.isChecked())
        {
            localStation.setVisibility(View.VISIBLE);
            outStationLayout.setVisibility(View.GONE);
            localStation.setText("");  // To Do set the value from service response
            requestLocation = localStation.getText().toString();
        }
        else if(radioOutStation.isChecked())
        {
            outStationLayout.setVisibility(View.VISIBLE);
            localStation.setVisibility(View.GONE);
            requestLocation = outStation.getText().toString();
        }


        setCurrentDate();
        carRequestFromDay.setText(String.valueOf((new StringBuilder().append(day).append("-").append(month+1).append("-")
                .append(year).append(" "))));
        requestFromDate = carRequestFromDay.getText().toString();

        carRequestToDay.setText(String.valueOf((new StringBuilder().append(day).append("-").append(month+1).append("-")
                .append(year).append(" "))));
        requestToDate = carRequestToDay.getText().toString();

        noOfRequestDays = dayBetween(requestFromDate,requestToDate);

        carRequestDays.setText(noOfRequestDays);
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
            case R.id.buttonPick:
                if (requestLocation.equals("") || requestFromDate.equals("") || requestToDate.equals("") || noOfRequestDays.equals("")) {
                    if (LogFlag.bLogOn)Log.d(TAG, getResources().getString(R.string.nullMessage));
                    setErrorMessage( getResources().getString(R.string.nullMessage));
                }else {
                    gotoPaymentPage();
                }
                break;

            case R.id.carRequestFromDay:
                // On button click show datepicker dialog
                dateSelectedFrom = true;
                showDialog(DATE_PICKER_ID);
                break;

            case R.id.carRequestToDay:
                // On button click show datepicker dialog
                dateSelectedFrom = false;
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
            localStation.setText("");  // To Do set the value from service response
            requestLocation = localStation.getText().toString();
        }
        else if(radioOutStation.isChecked())
        {
            outStationLayout.setVisibility(View.VISIBLE);
            localStation.setVisibility(View.GONE);
            requestLocation = outStation.getText().toString();
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


            if(dateSelectedFrom){
                // set selected date
                carRequestFromDay.setText(String.valueOf((new StringBuilder().append(day).append("-").append(month+1).append("-")
                        .append(year).append(" "))));
                requestFromDate = carRequestFromDay.getText().toString();
            }else {

                // set selected date
                carRequestToDay.setText(String.valueOf((new StringBuilder().append(day).append("-").append(month+1).append("-")
                        .append(year).append(" "))));
                requestToDate = carRequestToDay.getText().toString();
            }

            noOfRequestDays = dayBetween(requestFromDate,requestToDate);

            carRequestDays.setText(noOfRequestDays);

        }
    };


    public void setCurrentDate() {

        final Calendar calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

    }

    public String dayBetween(String requestFromDate,String requestToDate)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH);
        Date date1 = null,date2 = null;
        try{
            date1 = sdf.parse(requestFromDate);
            date2 = sdf.parse(requestToDate);
        }catch(Exception e)
        {
            if (LogFlag.bLogOn)Log.e(TAG, e.getMessage());
        }

        long diff = (date2.getTime() - date1.getTime())/(24*60*60*1000);

        float dayCount = (float) diff / (24 * 60 * 60 * 1000);

        String noOfDays = "" + (int) dayCount + " Days";
        if (LogFlag.bLogOn)Log.d(TAG, "noOfDays: "+noOfDays);

        return (noOfDays);
    }

    void setErrorMessage(String errorMessage) {
        errorText.setVisibility(View.VISIBLE);
        errorText.setText(errorMessage);
    }

}

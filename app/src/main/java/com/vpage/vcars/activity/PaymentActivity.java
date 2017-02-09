package com.vpage.vcars.activity;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import com.vpage.vcars.R;
import com.vpage.vcars.tools.utils.LogFlag;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;
import java.util.Calendar;

@EActivity(R.layout.activity_paymemt)
@Fullscreen
public class PaymentActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = PaymentActivity.class.getName();

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.buttonPayment)
    Button buttonPayment;

    @ViewById(R.id.editTextAccountHolderName)
    EditText editTextAccountHolderName;

    @ViewById(R.id.editTextBankName)
    EditText editTextBankName;

    @ViewById(R.id.editTextCardNo)
    EditText editTextCardNo;

    @ViewById(R.id.cardExpiryDay)
    Button cardExpiryDay;

    @ViewById(R.id.expDate)
    TextView expDateDateView;

    @ViewById(R.id.errorText)
    TextView errorText;

    String selectedCar;
    String accountHolderName = "",bankName = "",cardNumber= "",cardExpiryDate = "";

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
        getSupportActionBar().setTitle("Payment");

    }

    private void setView() {

        buttonPayment.setOnClickListener(this);
        cardExpiryDay.setOnClickListener(this);
        setCurrentDate();
        expDateDateView.setText(String.valueOf((new StringBuilder().append(month+1).append("-").append(day).append("-")
                .append(year).append(" "))));
        cardExpiryDate= expDateDateView.getText().toString();
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
            case R.id.buttonPayment:
                // TO Do

                accountHolderName = editTextAccountHolderName.getText().toString();
                bankName = editTextBankName.getText().toString();
                cardNumber = editTextCardNo.getText().toString();
                if (accountHolderName.equals("") || bankName.equals("")|| cardNumber.equals("")|| cardExpiryDate.equals("")) {
                    if (LogFlag.bLogOn)Log.d(TAG, String.valueOf(R.string.nullMessage));
                    setErrorMessage(String.valueOf(R.string.nullMessage));
                    return;
                }
                break;

            case R.id.cardExpiryDay:
                // On button click show datepicker dialog
                showDialog(DATE_PICKER_ID);
                break;
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
            expDateDateView.setText(carRequestDate);
            cardExpiryDate= expDateDateView.getText().toString();
            if (LogFlag.bLogOn) Log.d(TAG, "Car Request Date: "+carRequestDate);

        }
    };


    public void setCurrentDate() {

        final Calendar calendar = Calendar.getInstance();

        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        day = calendar.get(Calendar.DAY_OF_MONTH);

    }

    void setErrorMessage(String errorMessage) {
        errorText.setVisibility(View.VISIBLE);
        errorText.setText(errorMessage);
    }

}


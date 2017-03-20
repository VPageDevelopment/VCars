package com.vpage.vcars.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import com.squareup.picasso.Picasso;
import com.vpage.vcars.R;
import com.vpage.vcars.tools.VTools;
import com.vpage.vcars.tools.utils.LogFlag;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;

@EActivity(R.layout.activity_cardetail)
@Fullscreen
public class CarDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = CarDetailActivity.class.getName();

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.carImage)
    ImageView carImage;

    @ViewById(R.id.buttonEnter)
    Button buttonEnter;

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
        getSupportActionBar().setTitle(selectedCar);

    }

    private void setView() {

        Picasso.with(this).load(R.drawable.carimage).into(carImage);

        buttonEnter.setOnClickListener(this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            if (LogFlag.bLogOn) Log.d(TAG, "Back Pressed ");
            gotoHomePage();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        gotoCarRequestPage();
    }

    private void gotoCarRequestPage() {

        Intent intent = new Intent(getApplicationContext(), CarRequestActivity_.class);
        intent.putExtra("SelectedCar","Car Selected");
        startActivity(intent);
        VTools.animation(this);
    }

    private void gotoHomePage() {

        Intent intent = new Intent(getApplicationContext(), HomeActivity_.class);
        startActivity(intent);
        VTools.animation(this);
        finish();
    }


    @Override
    public void onBackPressed()
    {
      //  super.onBackPressed();
        gotoHomePage();
    }



}

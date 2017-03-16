package com.vpage.vcars.activity;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import com.vpage.vcars.R;
import com.vpage.vcars.tools.VTools;
import com.vpage.vcars.tools.utils.LogFlag;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;
import java.util.ArrayList;
import java.util.List;

@EActivity(R.layout.activity_carattachment)
@Fullscreen
public class CarAttachmentActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private static final String TAG = CarAttachmentActivity.class.getName();

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.noDataText)
    TextView noDataText;

    @ViewById(R.id.editTextFeature)
    EditText editTextFeature;

    @ViewById(R.id.buttonAdd)
    Button buttonAdd;

    @ViewById(R.id.spinnerFeature)
    Spinner spinnerFeature;

    ArrayAdapter adapter;

    List<String> list = new ArrayList<>();


    @AfterViews
    public void onInitView() {

        setActionBarSupport();

        setView();
    }


    private void setActionBarSupport() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Car Details");

    }

    private void setView() {

        list.add("AC");
        list.add("Fan");
        list.add("TV");
        list.add("Screen in Front");
        list.add("Screen at Back");



        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);

        View.OnClickListener listener = new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if(!editTextFeature.getText().toString().isEmpty() && !list.contains(editTextFeature.getText().toString())){
                    list.add(editTextFeature.getText().toString());
                }else{
                    VTools.showToast("Check the Input ");
                }

                editTextFeature.setText("");
                adapter.notifyDataSetChanged();
            }
        };

        buttonAdd.setOnClickListener(listener);

        spinnerFeature.setAdapter(adapter);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerFeature.setOnItemSelectedListener(this);

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
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String selectedSpinnerItem = spinnerFeature.getSelectedItem().toString();
        editTextFeature.setText(selectedSpinnerItem);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

package com.vpage.vcars.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.vpage.vcars.R;
import com.vpage.vcars.tools.ActionEditText;
import com.vpage.vcars.tools.VTools;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;


@Fullscreen
@EActivity(R.layout.activity_feedback)
public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = FeedbackActivity.class.getName();

    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.name)
    EditText name;

    @ViewById(R.id.email)
    EditText email;

    @ViewById(R.id.comments)
    EditText comments;

    @ViewById(R.id.submitButton)
    Button submitButton;

    @AfterViews
    public void init() {

        setActionBarSupport();
        new ActionEditText(this);

        submitButton.setOnClickListener(this);

    }

    private void setActionBarSupport() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("Feedback");

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @SuppressLint("ShowToast")
    @Override
    public void onClick(View v) {
        // for null check of user input
        if(!name.getText().toString().isEmpty()&& !email.getText().toString().isEmpty()&& !comments.getText().toString().isEmpty()){

            // for connection check
            if (VTools.checkNetworkConnection(FeedbackActivity.this)) {
                callShareIntent();

            }else {
                Toast.makeText(this,getResources().getString(R.string.connection_check),Toast.LENGTH_SHORT).show();
            }

        }else {

            Toast.makeText(this,getResources().getString(R.string.empty_check),Toast.LENGTH_SHORT).show();
        }
    }

    void callShareIntent(){
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/html");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(comments.getText().toString()));
        startActivity(Intent.createChooser(sharingIntent,"Share using"));
    }

}

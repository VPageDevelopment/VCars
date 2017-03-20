package com.vpage.vcars.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vpage.vcars.R;
import com.vpage.vcars.tools.ActionEditText;
import com.vpage.vcars.tools.VTools;
import com.vpage.vcars.tools.utils.LogFlag;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Map;


@Fullscreen
@EActivity(R.layout.activity_feedback)
public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = FeedbackActivity.class.getName();


    private Bitmap bitmap;

    private int PICK_IMAGE_REQUEST = 1;

    private String UPLOAD_URL ="http://simplifiedcoding.16mb.com/VolleyUpload/upload.php";

    private String KEY_IMAGE = "image";

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

    @ViewById(R.id.uploadButton)
    Button uploadButton;

    @AfterViews
    public void init() {

        setActionBarSupport();
        new ActionEditText(this);

        submitButton.setOnClickListener(this);
        uploadButton.setOnClickListener(this);

    }

    @Override
    protected void onResume() {
        super.onResume();
        comments.requestFocus();

        comments.postDelayed(new Runnable() {

            @Override
            public void run() {
                InputMethodManager keyboard = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                keyboard.hideSoftInputFromWindow(comments.getWindowToken(), 0);
            }
        },200);
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


        switch (v.getId()) {

            case R.id.submitButton:

                // for null check of user input
                if (!name.getText().toString().isEmpty() && !email.getText().toString().isEmpty() && !comments.getText().toString().isEmpty()) {

                    // for connection check
                    if (VTools.checkNetworkConnection(FeedbackActivity.this)) {
                        callShareIntent();

                    } else {
                        Toast.makeText(this, getResources().getString(R.string.connection_check), Toast.LENGTH_SHORT).show();
                    }

                } else {

                    Toast.makeText(this, getResources().getString(R.string.empty_check), Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.uploadButton:
                showFileChooser();
                break;
        }
    }

    void callShareIntent(){
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/html");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(comments.getText().toString()));
        startActivity(Intent.createChooser(sharingIntent,"Share using"));
    }



    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    @Background
    public void uploadImage(){
        //Showing the progress dialog
        //   final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, UPLOAD_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String s) {
                        //Disimissing the progress dialog
                        //    loading.dismiss();
                        //Showing toast message of the response
                        Toast.makeText(FeedbackActivity.this, s , Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        //Dismissing the progress dialog
                        //  loading.dismiss();

                        //Showing toast
                        Toast.makeText(FeedbackActivity.this, volleyError.getMessage().toString(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Converting Bitmap to String
                String image = getStringImage(bitmap);

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put(KEY_IMAGE, image);

                //returning parameters
                return params;
            }
        };

        //Creating a Request Queue
        RequestQueue requestQueue = Volley.newRequestQueue(this);

        //Adding request to the queue
        requestQueue.add(stringRequest);
    }

    @UiThread
    public void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Getting the Bitmap from Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                if(bitmap != null){
                    uploadImage();
                }

            } catch (IOException e) {
                if (LogFlag.bLogOn) Log.e(TAG,e.getMessage());
            }
        }
    }

}

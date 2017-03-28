package com.vpage.vcars.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.SimpleAdapter;
import android.widget.TextView;
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
import com.vpage.vcars.tools.utils.AppConstant;
import com.vpage.vcars.tools.utils.LogFlag;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;


@Fullscreen
@EActivity(R.layout.activity_feedback)
public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private static final String TAG = FeedbackActivity.class.getName();


    private Bitmap bitmap;

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
    PopupWindow PopUp;

    String imagePath ="";


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

                String [] textPosition = new String[]{"Gallery", "Camera"};
                int[] imagesIcons = new int[]{
                        (android.R.drawable.ic_menu_gallery),
                        (android.R.drawable.ic_menu_camera)
                };

                setSharePopupView(textPosition, imagesIcons);
                break;
        }
    }

    void callShareIntent(){

        if(!imagePath.isEmpty()){
            sentMail(imagePath);

        }else if(!VTools.getCameraImagePath().isEmpty()){
            sentMail(VTools.getCameraImagePath());
        }else {
            Toast.makeText(FeedbackActivity.this, "Attachment is not completed" , Toast.LENGTH_LONG).show();
        }
    }


    void sentMail(String imageSelected){
        Uri uri = Uri.parse(imageSelected);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {"email@example.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "subject here");
        emailIntent.putExtra(Intent.EXTRA_TEXT, Html.fromHtml(comments.getText().toString()));
        emailIntent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(emailIntent, "Pick an Email provider"));
    }

    @Background
    public void uploadImage(){
        //Showing the progress dialog
        //   final ProgressDialog loading = ProgressDialog.show(this,"Uploading...","Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppConstant.UPLOAD_URL,
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

                //Creating parameters
                Map<String,String> params = new Hashtable<String, String>();

                //Adding parameters
                params.put(AppConstant.KEY_IMAGE, imagePath);

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
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), AppConstant.PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppConstant.PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
                //Getting the Bitmap from Gallery
               // bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                if(null != filePath){
                    if (LogFlag.bLogOn) Log.d(TAG,"FilePath: "+filePath);
                 //   imagePath = VTools.getRealPathFromURI(FeedbackActivity.this,filePath);
                    imagePath = filePath.getPath();
                    if (LogFlag.bLogOn) Log.d(TAG,"ImageTaken: "+imagePath);
                  //  uploadImage();
                }
        }
    }

    void setSharePopupView(final String[] textPosition, int[] imagesIcons) {

        View popUpView = getLayoutInflater().inflate(R.layout.popupview, null); // inflating popup layout
        PopUp = VTools.createPopUp(popUpView);


        PopUp.setBackgroundDrawable(new BitmapDrawable());
        PopUp.setOutsideTouchable(true);

        TextView popUpTitle = (TextView) popUpView.findViewById(R.id.popUpTitle);
        ListView listView = (ListView) popUpView.findViewById(R.id.listView);
        final ImageButton btnClose = (ImageButton) popUpView.findViewById(R.id.btnClose);


        popUpTitle.setText("Choose Image ");


        // create the grid item mapping
        String[] col_value = new String[]{"col_1", "col_2"};
        int[] col_id = new int[]{R.id.menuItemFlag, R.id.menuItemText};

        int[] col_1_values = imagesIcons;
        String[] col_2_values = textPosition;

        // prepare the list of all records
        List<HashMap<String, String>> fillMaps = new ArrayList<HashMap<String, String>>();
        for (int i = 0; i < imagesIcons.length; i++) {
            HashMap<String, String> map = new HashMap<String, String>();

            map.put("col_2", col_2_values[i]);
            map.put("col_1", Integer.toString(col_1_values[i]));

            fillMaps.add(map);
        }

        // fill in the grid_item layout
        SimpleAdapter adapter = new SimpleAdapter(this, fillMaps, R.layout.popuplist, col_value, col_id);
        listView.setAdapter(adapter);


        listView.setOnItemClickListener(this);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopUp.dismiss();
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        switch (i) {
            case 0:
                // Share
                if (LogFlag.bLogOn) Log.d(TAG, "ItemSelected 1: " + i);
                showFileChooser();
                break;
            case 1:
                // Current Driving
                if (LogFlag.bLogOn) Log.d(TAG, "ItemSelected 2: " + i);
                gotoCameraPage();
                break;

        }
        PopUp.dismiss();
    }

    private void gotoCameraPage() {

        Intent intent = new Intent(getApplicationContext(), CustomCameraActivity_.class);
        startActivity(intent);
        VTools.animation(this);
    }

}

package com.vpage.vcars.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.hardware.Camera.CameraInfo;
import android.hardware.Camera.ErrorCallback;
import android.hardware.Camera.Parameters;
import android.hardware.Camera.PictureCallback;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.KeyEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.vpage.vcars.R;
import com.vpage.vcars.tools.VTools;
import com.vpage.vcars.tools.utils.AppConstant;
import com.vpage.vcars.tools.utils.LogFlag;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

@EActivity(R.layout.activity_customcamera)
public class CustomCameraActivity extends Activity implements Callback, OnClickListener {

    String TAG = CustomCameraActivity.this.getClass().getName();


    private SurfaceHolder surfaceHolder;
    private Camera camera;

    private int cameraId;
    private boolean flashmode = false;
    private int rotation;
    private List<String> imageDataList;

    @ViewById(R.id.flipCamera)
    Button flipCamera;

    @ViewById(R.id.flash)
    Button flashCameraButton;

    @ViewById(R.id.captureImage)
    Button captureImage;

    @ViewById(R.id.surfaceView)
    SurfaceView surfaceView;

    @ViewById(R.id.preview)
    ImageView preview;

    boolean isAppWentToBg = false;

    boolean isWindowFocused = false;

    boolean isBackPressed = false;

    @Override
    protected void onStart() {

        if (LogFlag.bLogOn) Log.d(TAG, "onStart isAppWentToBg " + isAppWentToBg);

        applicationWillEnterForeground();
        super.onStart();
        VTools.forwardTransition(CustomCameraActivity.this);
    }


    @AfterViews
    public void initCustomCameraView() {
        VTools.setActivity(this);
        VTools.setDisplayMetrics();

        // camera surface view created
        cameraId = CameraInfo.CAMERA_FACING_BACK;

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
        flipCamera.setOnClickListener(this);
        captureImage.setOnClickListener(this);
        flashCameraButton.setOnClickListener(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        imageDataList = new ArrayList<String>();

        if (getFrontCameraId() != -1) {
            flipCamera.setVisibility(View.VISIBLE);
        }

        /*  if (!getBaseContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FLASH)) {
            flashCameraButton.setVisibility(View.GONE);
        }*/
    }



    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (!openCamera(CameraInfo.CAMERA_FACING_BACK)) {
            alertCameraDialog();
        }

    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        int action = event.getAction();
        int keyCode = event.getKeyCode();
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_UP:
                if (action == KeyEvent.ACTION_DOWN) {
                    takeImage();
                }
                return true;
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                if (action == KeyEvent.ACTION_DOWN) {
                    takeImage();
                }
                return true;
            default:
                return super.dispatchKeyEvent(event);
        }
    }

    private boolean openCamera(int id) {
        boolean result = false;
        cameraId = id;
        releaseCamera();
        try {
            camera = Camera.open(cameraId);
        } catch (Exception e) {
            if (LogFlag.bLogOn) Log.e(TAG, e.getMessage());
        }
        if (camera != null) {
            try {
                setUpCamera(camera);
                camera.setErrorCallback(new ErrorCallback() {

                    @Override
                    public void onError(int error, Camera camera) {

                    }
                });
                camera.setPreviewDisplay(surfaceHolder);
                camera.startPreview();
                result = true;
            } catch (IOException e) {
                if (LogFlag.bLogOn) Log.e(TAG, e.getMessage());
                result = false;
                releaseCamera();
            }
        }
        return result;
    }

    private void setUpCamera(Camera c) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        Camera.getCameraInfo(cameraId, info);
        rotation = getWindowManager().getDefaultDisplay().getRotation();
        int degree = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degree = 0;
                break;
            case Surface.ROTATION_90:
                degree = 90;
                break;
            case Surface.ROTATION_180:
                degree = 180;
                break;
            case Surface.ROTATION_270:
                degree = 270;
                break;

            default:
                break;
        }

        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            // frontFacing
            rotation = (info.orientation + degree) % 330;
            rotation = (360 - rotation) % 360;
        } else {
            // Back-facing
            rotation = (info.orientation - degree + 360) % 360;
        }
        c.setDisplayOrientation(rotation);
        Parameters params = c.getParameters();

        //  showFlashButton(params);

        List<String> focusModes = params.getSupportedFlashModes();
        if (focusModes != null) {
            if (focusModes
                    .contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                params.setFlashMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }
        }

        params.setRotation(rotation);
    }

    private void showFlashButton(Parameters params) {
        boolean showFlash = (getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_FLASH) && params.getFlashMode() != null)
                && params.getSupportedFlashModes() != null
                && params.getSupportedFocusModes().size() > 1;

        flashCameraButton.setVisibility(showFlash ? View.VISIBLE
                : View.INVISIBLE);

    }

    private void releaseCamera() {
        try {
            if (camera != null) {
                camera.setPreviewCallback(null);
                camera.setErrorCallback(null);
                camera.stopPreview();
                camera.release();
                camera = null;
            }
        } catch (Exception e) {
            if (LogFlag.bLogOn) Log.e(TAG, e.getMessage());
            camera = null;
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.flash:
                BackPressed();
                break;
            case R.id.flipCamera:
                flipCamera();
                break;
            case R.id.captureImage:
                takeImage();
                break;

            default:
                break;
        }
    }

    private void takeImage() {
        try {
            camera.takePicture(myShutterCallback, null, myPictureCallback_JPG);
        } catch (RuntimeException e) {
            if (LogFlag.bLogOn) Log.e(TAG, e.getLocalizedMessage());
        }
    }

    Camera.ShutterCallback myShutterCallback = new Camera.ShutterCallback() {

        @Override
        public void onShutter() {
            // TODO Auto-generated method stub

        }
    };

    PictureCallback myPictureCallback_JPG = new PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // TODO Auto-generated method stub
            if(data !=null) {
                int maxSize = 816;
                BitmapFactory.Options opt=new BitmapFactory.Options();
                opt.inJustDecodeBounds=true;
                BitmapFactory.decodeByteArray(data, 0, data.length, opt);
                int srcSize= Math.max(opt.outWidth, opt.outHeight);
                if (LogFlag.bLogOn) Log.d(TAG,"out w:"+opt.outWidth+" h:"+opt.outHeight);
                opt.inSampleSize=maxSize <srcSize ? (srcSize/maxSize):1;
                if (LogFlag.bLogOn) Log.d(TAG, "sample size " + opt.inSampleSize);
                opt.inJustDecodeBounds=false;
                Bitmap loadedImage= BitmapFactory.decodeByteArray(data, 0, data.length, opt);

                //Scaling and rotation
                float scale= Math.max((float)maxSize/opt.outWidth,(float)maxSize/opt.outHeight);
                Matrix matrix=new Matrix();
                System.out.println("sample out w:" + opt.outWidth + " h:" + opt.outHeight);
                if (cameraId == CameraInfo.CAMERA_FACING_BACK){
                    matrix.setRotate(90);
                    matrix.postScale(scale, scale);
                }
                else{
                    matrix.setRotate(270);
                    matrix.postScale(-scale, scale);
                }
                Bitmap source= Bitmap.createBitmap(loadedImage, 0, 0,loadedImage.getWidth(),loadedImage.getHeight(), matrix, true);
                preview.setImageBitmap(source);
                String uriString= MediaStore.Images.Media.insertImage(getContentResolver(), source, null, null);
                imageDataList.add(uriString);
                camera.startPreview();
            }
        }
    };

    private void flipCamera() {
        int id = (cameraId == CameraInfo.CAMERA_FACING_BACK ? CameraInfo.CAMERA_FACING_FRONT
                : CameraInfo.CAMERA_FACING_BACK);
        if (!openCamera(id)) {
            alertCameraDialog();
        }
    }

    int getFrontCameraId() {
        CameraInfo ci = new CameraInfo();
        for (int i = 0; i < Camera.getNumberOfCameras(); i++) {
            Camera.getCameraInfo(i, ci);
            if (ci.facing == CameraInfo.CAMERA_FACING_FRONT) return i;
        }
        return -1; // No front-facing camera found
    }

    private void alertCameraDialog() {
        AlertDialog.Builder dialog = createAlert(CustomCameraActivity.this,
                "Camera info", "error to open camera");
        dialog.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });
        dialog.show();
    }

    private Builder createAlert(Context context, String title, String message) {

        AlertDialog.Builder dialog = new AlertDialog.Builder(
                new ContextThemeWrapper(context,
                        android.R.style.Theme_Holo_Light_Dialog));
        dialog.setIcon(R.drawable.caricon);
        if (title != null)
            dialog.setTitle(title);
        else
            dialog.setTitle("Information");
        dialog.setMessage(message);
        dialog.setCancelable(false);
        return dialog;

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (LogFlag.bLogOn) Log.d(TAG, "onStop");
        applicationdidenterbackground();
    }


    private void applicationWillEnterForeground() {
        if (isAppWentToBg) {
            isAppWentToBg = false;
            //Toast.makeText(getApplicationContext(), "App is in foreground", Toast.LENGTH_SHORT).show();
        }
    }


    public void applicationdidenterbackground() {
        if (!isWindowFocused) {
            isAppWentToBg = true;
            //Toast.makeText(getApplicationContext(), "App is Going to Background", Toast.LENGTH_SHORT).show();
            if(isAppWentToBg){
                finish();
            }
        }
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {

        isWindowFocused = hasFocus;

        if (isBackPressed && !hasFocus) {
            isBackPressed = false;
            isWindowFocused = true;
        }

        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onBackPressed() {
        if (this instanceof CustomCameraActivity) {

        } else {
            isBackPressed = true;
        }
        BackPressed();
    }


    private void flashOnButton() {
        if (camera != null) {
            try {
                Parameters param = camera.getParameters();
                param.setFlashMode(!flashmode ? Parameters.FLASH_MODE_TORCH
                        : Parameters.FLASH_MODE_OFF);
                camera.setParameters(param);
                flashmode = !flashmode;
            } catch (Exception e) {
                if (LogFlag.bLogOn)  Log.e(TAG, e.getMessage());
            }

        }

    }

    void BackPressed() {

        if (imageDataList.size() > 0) {
            new takePicture().execute();
        } else {
            releaseCamera();
            finish();
        }

    }

    class takePicture extends AsyncTask<String, Void, String> {

        ProgressDialog progress;
        Bitmap loadedImage = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            releaseCamera();
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progress = ProgressDialog.show(CustomCameraActivity.this, "", "Updating album please wait..", true);
                    progress.setCancelable(false);
                }
            });
        }

        @Override
        protected String doInBackground(String... params) {
            for (String uriString : imageDataList) {
                try {
                   // loadedImage = MediaStore.Images.Media.getBitmap(getContentResolver(), Uri.parse(uriString));
                    if (LogFlag.bLogOn) Log.d(TAG,"ImageTaken: "+VTools.getRealPathFromURI(CustomCameraActivity.this,Uri.parse(uriString)));
                    VTools.setCameraImagePath(uriString);
                } catch (Exception e) {
                    if (LogFlag.bLogOn) Log.e(TAG,e.getMessage());
                }
            }
            return null;
        }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progress.dismiss();
                }
            });
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            Intent intent = new Intent(getApplicationContext(), FeedbackActivity_.class);
            startActivity(intent);
            finish();
        }

    }



}
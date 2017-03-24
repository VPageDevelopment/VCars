package com.vpage.vcars.tools.utils;

import android.os.Environment;

public class AppConstant {

    //The Android's default system path of your application database.
    public static String DB_PATH = "/data/data/com.vpage.vcars/databases/VcarLocationMarkerLite.sqlite";

    //replace this with name of your db file which you copied into asset folder
    public static String DB_NAME = "VcarLocationMarkerLite.sqlite";

    //Table name of DB
    public static String TB_NAME = "tblVcarTrack";

    public static String root = Environment.getExternalStorageDirectory().toString();

    public static int PICK_IMAGE_REQUEST = 1;

    public static String UPLOAD_URL ="http://simplifiedcoding.16mb.com/VolleyUpload/upload.php";

    public static String KEY_IMAGE = "image";

}

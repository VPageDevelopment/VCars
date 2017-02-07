package com.vpage.vcars.tools;


import android.content.Context;
import android.content.SharedPreferences;

import com.vpage.vcars.R;

import org.androidannotations.annotations.EBean;


@EBean(scope = EBean.Scope.Singleton)
public class VPreferences {

    private static final String TAG = VPreferences.class.getName();
    static String preferenceName = "VDrive";

    public static void save(String Key, String Value) {
        SharedPreferences preference;
        SharedPreferences.Editor editor;
        preference = VCarsApplication.getContext().getSharedPreferences(VCarsApplication.getContext().getString(R.string.app_name),
                Context.MODE_PRIVATE);
        editor = preference.edit();
        editor.putString(Key, Value);
        editor.commit();
    }

    public static String get(String Key) {
        SharedPreferences preference;
        String text;
        preference = VCarsApplication.getContext().getSharedPreferences(VCarsApplication.getContext().getString(R.string.app_name),
                Context.MODE_PRIVATE);
        text = preference.getString(Key, null);
        return text;
    }

    public static void clear(String Key) {
        SharedPreferences preference;
        SharedPreferences.Editor editor;
        preference = VCarsApplication.getContext().getSharedPreferences(VCarsApplication.getContext().getString(R.string.app_name),
                Context.MODE_PRIVATE);
        editor = preference.edit();
        editor.remove(Key);
        editor.commit();
    }

    public static void clearAll() {
        SharedPreferences preference;
        SharedPreferences.Editor editor;
        preference = VCarsApplication.getContext().getSharedPreferences(VCarsApplication.getContext().getString(R.string.app_name),
                Context.MODE_PRIVATE);
        editor = preference.edit();
        editor.clear();
        editor.commit();
    }

    public static void saveAppInstallStatus(String key,boolean value){
        SharedPreferences preference;
        SharedPreferences.Editor editor;
        preference = VCarsApplication.getContext().getSharedPreferences(VCarsApplication.getContext().getString(R.string.app_name),
                Context.MODE_PRIVATE);
        editor = preference.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }

    public static boolean getAppInstallStatus(String key){
        SharedPreferences preference;
        boolean val;
        preference = VCarsApplication.getContext().getSharedPreferences(VCarsApplication.getContext().getString(R.string.app_name),
                Context.MODE_PRIVATE);
        val = preference.getBoolean(key, false);
        return val;
    }

}

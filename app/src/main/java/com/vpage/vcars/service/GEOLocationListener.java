package com.vpage.vcars.service;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vpage.vcars.tools.VCarsApplication;
import com.vpage.vcars.tools.VPreferences;
import com.vpage.vcars.tools.utils.LogFlag;
import com.vpage.vcars.pojos.VLocation;
import java.io.IOException;
import java.util.List;

public class GEOLocationListener implements LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = GEOLocationListener.class.getName();

    private LocationRequest mLocationRequest;
    protected Location mobileLocation;
    private GoogleApiClient mGoogleApiClient;

    Activity activity;

    VLocation vLocation;

    private static int UPDATE_INTERVAL = 10000; // 10 sec
    private static int FATEST_INTERVAL = 5000; // 5 sec
    private static int DISPLACEMENT = 5; // 5 meters
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;

    public GEOLocationListener(Activity activity) {
        this.activity = activity;
        vLocation = new VLocation();
    }

    public void getLocation() {
        try {
            if (checkPlayServices()) {
                buildGoogleApiClient();
                createLocationRequest();
                mGoogleApiClient.connect();
            }
        } catch (Exception e) {
            if (LogFlag.bLogOn) Log.e(TAG, e.getMessage());
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onLocationChanged(Location location) {

        mobileLocation = location;
        // Displaying the new location on UI
        fetchLocation();
    }

    private String fetchLocation() {

      if (ActivityCompat.checkSelfPermission(VCarsApplication.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(VCarsApplication.getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
          if (LogFlag.bLogOn) Log.i(TAG, "Geo location disabled");
        } else {
          if (LogFlag.bLogOn) Log.i(TAG, "Geo location enabled");
        }


        mobileLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mobileLocation != null) {
            double latitude = mobileLocation.getLatitude();
            double longitude = mobileLocation.getLongitude();
            Geocoder geocoder = new Geocoder(VCarsApplication.getContext());
            try {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                for (Address address : addresses) {
                    Log.d(TAG, address.toString());
                    if (LogFlag.bLogOn) Log.d(TAG, address.toString());
                    vLocation.setLocation(address.getLocality());
                    vLocation.setLatitude(latitude);
                    vLocation.setLongitude(longitude);
                    vLocation.setState(address.getAdminArea());
                    vLocation.setCounty(address.getSubAdminArea());
                    vLocation.setPostalCode(address.getPostalCode());
                    vLocation.setCountryCode(address.getCountryCode());
                    vLocation.setCountryName(address.getCountryName());
                    if (LogFlag.bLogOn) Log.d(TAG, vLocation.toString());
                    Gson gson = new GsonBuilder().create();
                    VPreferences.save("CurrentLocation", gson.toJson(vLocation));
                    VPreferences.save("countryCode", vLocation.getCountryCode());
                }
            } catch (IOException e) {
                if (LogFlag.bLogOn) Log.e(TAG, e.getMessage());
                vLocation.setLocation(null);
            }
        } else {
            if (LogFlag.bLogOn) Log.i(TAG, "Not able to get Location");
        }
        return  vLocation.getLocation();
    }

    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(VCarsApplication.getContext())
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }
    @Override
    public void onConnected(Bundle bundle) {

         fetchLocation();

    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (LogFlag.bLogOn) Log.e(TAG, connectionResult.getErrorMessage());
    }
    /**
     * Creating location request object
     */
    protected void createLocationRequest() {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT);
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(VCarsApplication.getContext());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this.activity, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {

            }
        }
        return true;
    }

    public VLocation geVLocation()
    {
        if (LogFlag.bLogOn) Log.i(TAG, "geVLocation");
        return vLocation;
    }


    public void closeClient() {

        mGoogleApiClient.disconnect();
    }
}

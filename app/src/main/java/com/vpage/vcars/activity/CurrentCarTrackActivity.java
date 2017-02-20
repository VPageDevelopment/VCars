package com.vpage.vcars.activity;

import android.Manifest;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vpage.vcars.R;
import com.vpage.vcars.pojos.VLocation;
import com.vpage.vcars.tools.LocationsContentProvider;
import com.vpage.vcars.tools.LocationsDB;
import com.vpage.vcars.tools.VCarsApplication;
import com.vpage.vcars.tools.VPreferences;
import com.vpage.vcars.tools.VTools;
import com.vpage.vcars.tools.utils.LogFlag;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;
import java.io.IOException;
import java.util.List;


@WindowFeature({Window.FEATURE_NO_TITLE, Window.FEATURE_ACTION_BAR_OVERLAY})
@EActivity(R.layout.activity_currentcartrack)
@Fullscreen
public class CurrentCarTrackActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, View.OnClickListener,LoaderManager.LoaderCallbacks<Cursor>, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

    private static final String TAG = CurrentCarTrackActivity.class.getName();


    @ViewById(R.id.messageText)
    TextView messageText;

    @ViewById(R.id.progressText)
    TextView progressText;

    @ViewById(R.id.fullViewButton)
    Button fullViewButton;

    @ViewById(R.id.halfViewButton)
    Button halfViewButton;

    @ViewById(R.id.progressBar)
    ProgressBar progressBar;

    @ViewById(R.id.contentLayout)
    RelativeLayout contentLayout;

    @ViewById(R.id.progressLayout)
    RelativeLayout progressLayout;

    SupportMapFragment mapFragment;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    LatLng latLng;

    private GoogleMap mMap;

    Marker currLocationMarker;

    @AfterViews
    public void onInitView() {

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        setView();
    }

    void setView(){
        fullViewButton.setOnClickListener(this);
        halfViewButton.setOnClickListener(this);
      //  progressText.setText(" ");

        // Invoke LoaderCallbacks to retrieve and draw already saved locations in map
        getSupportLoaderManager().initLoader(0, null, this);

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);

        Boolean isInternetPresent = VTools.checkNetworkConnection(CurrentCarTrackActivity.this);
        if (isInternetPresent) {
            buildGoogleApiClient();

            mGoogleApiClient.connect();
        }

        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);

    }

    protected synchronized void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onConnected(Bundle bundle) {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        updateLocation(mLastLocation);


        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000); //5 seconds
        mLocationRequest.setFastestInterval(3000); //3 seconds
        mLocationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
        //mLocationRequest.setSmallestDisplacement(0.1F); //1/10 meter

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);



    }

    @Override
    public void onConnectionSuspended(int i) {
        // Toast.makeText(this,"onConnectionSuspended",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // Toast.makeText(this,"onConnectionFailed",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onLocationChanged(Location location) {

        updateLocation(location);

        latLng = new LatLng(location.getLatitude(), location.getLongitude());

        //zoom to current position:
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(20).build();

        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

    void updateLocation(Location location){

        if (location != null) {

            VLocation vLocation = new VLocation();
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            Geocoder geocoder = new Geocoder(VCarsApplication.getContext());
            try {
                List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
                for (Address address : addresses) {
                    if (LogFlag.bLogOn) Log.d(TAG, address.toString());
                    vLocation.setLocation(address.getLocality());
                    vLocation.setLatitude(latitude);
                    vLocation.setLongitude(longitude);
                    vLocation.setState(address.getAdminArea());
                    vLocation.setCounty(address.getSubAdminArea());
                    vLocation.setPostalCode(address.getPostalCode());
                    vLocation.setCountryCode(address.getCountryCode());
                    vLocation.setCountryName(address.getCountryName());
                    vLocation.setAddress(address.getAddressLine(0) + "\t" + address.getAddressLine(1)
                            + "\t" + address.getAddressLine(2));
                    if (LogFlag.bLogOn) Log.d(TAG, vLocation.toString());
                    Gson gson = new GsonBuilder().create();
                    VPreferences.save("CurrentLocation", gson.toJson(vLocation));
                    VPreferences.save("countryCode", vLocation.getCountryCode());
                }
            } catch (IOException e) {
                if (LogFlag.bLogOn) Log.e(TAG, e.getMessage());
                vLocation.setLocation(null);
            }


            //place marker at current position
            //mGoogleMap.clear();
            if (currLocationMarker != null) {
                currLocationMarker.remove();
            }
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(vLocation.getAddress());
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            currLocationMarker = mMap.addMarker(markerOptions);



        }else {
            if (LogFlag.bLogOn) Log.i(TAG, "Not able to get Location");
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {

            @Override
            public boolean onMarkerClick(Marker arg0) {
                //  if(arg0.getTitle().equals("MyHome")) // if marker source is clicked

                return true;
            }

        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.fullViewButton:
                contentLayout.setVisibility(View.GONE);
                progressLayout.setVisibility(View.GONE);
                halfViewButton.setVisibility(View.VISIBLE);
                break;

            case R.id.halfViewButton:
                contentLayout.setVisibility(View.VISIBLE);
                progressLayout.setVisibility(View.VISIBLE);
                halfViewButton.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        // Uri to the content provider LocationsContentProvider
        Uri uri = LocationsContentProvider.CONTENT_URI;

        // Fetches all the rows from locations table
        return new CursorLoader(this, uri, null, null, null, null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onMapClick(LatLng latLng) {
        // Creating an instance of ContentValues
        ContentValues contentValues = new ContentValues();

        // Setting latitude in ContentValues
        contentValues.put(LocationsDB.FIELD_LAT, latLng.latitude );

        // Setting longitude in ContentValues
        contentValues.put(LocationsDB.FIELD_LNG, latLng.longitude);

        // Setting zoom in ContentValues
        contentValues.put(LocationsDB.FIELD_ZOOM, mMap.getCameraPosition().zoom);

        callRouteDetector(contentValues);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {

        // Removing all markers from the Google Map
        mMap.clear();

        callRouteDetectorDelete();

    }

    @Background
    public void callRouteDetector(ContentValues contentValues){

        if(null != contentValues){
            callRouteDetectorFinish(contentValues);
        }
    }

    @UiThread
    public void callRouteDetectorFinish(ContentValues contentValues){
        /** Setting up values to insert the clicked location into SQLite database */
        getContentResolver().insert(LocationsContentProvider.CONTENT_URI, contentValues[0]);
    }

    @Background
    public void callRouteDetectorDelete(){
        callRouteDetectorFinishDelete();
    }

    @UiThread
    public void callRouteDetectorFinishDelete(){
        /** Deleting all the locations stored in SQLite database */
        getContentResolver().delete(LocationsContentProvider.CONTENT_URI, null, null);
    }
}


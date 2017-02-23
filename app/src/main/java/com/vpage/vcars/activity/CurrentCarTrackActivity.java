package com.vpage.vcars.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
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
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vpage.vcars.R;
import com.vpage.vcars.httputils.VCarRestClient;
import com.vpage.vcars.pojos.VLocation;
import com.vpage.vcars.pojos.VLocationTrack;
import com.vpage.vcars.pojos.request.VLocationTrackRequest;
import com.vpage.vcars.pojos.response.VLocationTrackResponse;
import com.vpage.vcars.tools.LocationsDB;
import com.vpage.vcars.tools.RoutDetector;
import com.vpage.vcars.tools.VCarsApplication;
import com.vpage.vcars.tools.VPreferences;
import com.vpage.vcars.tools.VTools;
import com.vpage.vcars.tools.utils.LogFlag;
import com.vpage.vcars.view.PlayGifView;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;
import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.List;


@WindowFeature({Window.FEATURE_NO_TITLE, Window.FEATURE_ACTION_BAR_OVERLAY})
@EActivity(R.layout.activity_currentcartrack)
@Fullscreen
public class CurrentCarTrackActivity extends AppCompatActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, View.OnClickListener{

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

    @ViewById(R.id.mapContentLayout)
    FrameLayout mapContentLayout;

    @ViewById(R.id.viewGif)
    PlayGifView loaderGif;

    SupportMapFragment mapFragment;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    LatLng latLng;

    private GoogleMap googleMap;

    Marker currLocationMarker;

    RoutDetector routDetector;

    PolylineOptions polyLines;
    LocationsDB locationsDB;

    VLocationTrack vLocationTrack = new VLocationTrack();

    VLocationTrackRequest vLocationTrackRequest = new VLocationTrackRequest();


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

        loaderGif.setImageResource(R.drawable.loader_gif);
        loaderGif.setVisibility(View.VISIBLE);

        locationsDB = new LocationsDB(CurrentCarTrackActivity.this);
        locationsDB.createDataBase();

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.googleMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        this.googleMap.setMyLocationEnabled(true);

        Boolean isInternetPresent = VTools.checkNetworkConnection(CurrentCarTrackActivity.this);
        if (isInternetPresent) {
            buildGoogleApiClient();
            mGoogleApiClient.connect();
        }

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
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {

        updateLocation(location);

        latLng = new LatLng(location.getLatitude(), location.getLongitude());

       //zoom to current position:
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(8).build();

        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

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

            if (currLocationMarker != null) {
                currLocationMarker.remove();
            }
            latLng = new LatLng(location.getLatitude(), location.getLongitude());
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title(vLocation.getAddress());
            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
            currLocationMarker = googleMap.addMarker(markerOptions);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss a");
            String formattedDate = simpleDateFormat.format(location.getTime());

            if (LogFlag.bLogOn) Log.d(TAG, "getTime: "+formattedDate);
            if (LogFlag.bLogOn) Log.d(TAG, "location: "+vLocation.getLocation());

            vLocationTrack.setLongitude(latLng.longitude);
            vLocationTrack.setLatitude(latLng.latitude);
            vLocationTrack.setDate(formattedDate);
            vLocationTrack.setLocation(vLocation.getLocation());

            storeLocation();

           // callRouteDetector(vLocationTrack);

        }else {
            if (LogFlag.bLogOn) Log.i(TAG, "Not able to get Location");
        }

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

    @Background
    public void callRouteDetector(VLocationTrack vLocationTrack){
        List<VLocationTrack> vLocationTrackList = null;
        try {
            vLocationTrackList = locationsDB.openDataBaseData(vLocationTrack);
        } catch (SQLException e) {
            if (LogFlag.bLogOn) Log.e(TAG, e.getMessage(), e);
        }

        if(null != vLocationTrackList){
            callRouteDetectorSelectFinish(vLocationTrackList);
        }
    }

    @UiThread
    public void callRouteDetectorSelectFinish(List<VLocationTrack> vLocationTrackList){
        callRouteTrack(vLocationTrackList);
    }

    @Background
    public void callRouteTrack(List<VLocationTrack> vLocationTrackList){

        LatLng sourceLocation = new LatLng(vLocationTrackList.get(0).getLatitude(),vLocationTrackList.get(0).getLongitude());
        LatLng currentLocation = new LatLng(vLocationTrackList.get((vLocationTrackList.size()-1)).getLatitude(),vLocationTrackList.get((vLocationTrackList.size()-1)).getLongitude());

        routDetector = new RoutDetector(CurrentCarTrackActivity.this,sourceLocation,currentLocation);
        polyLines =routDetector.showRoute();
        if(null != polyLines){
            callRouteTrackFinish();
        }
    }

    @UiThread
    public void callRouteTrackFinish(){
        if (LogFlag.bLogOn) Log.d(TAG, "callRouteTrackFinish");
        googleMap.addPolyline(polyLines);
        loaderGif.setVisibility(View.GONE);
        mapContentLayout.setVisibility(View.VISIBLE);

    }


    @Background
    public void storeLocation() {
        if (LogFlag.bLogOn)Log.d(TAG, "storeLocation");
        setStoreLocationRequestData();
        VCarRestClient vCarRestClient = new VCarRestClient();
        vCarRestClient.locationStore(vLocationTrackRequest);
        trackLocation();
    }

    @UiThread
    public void storeLocationFinish(){
        if (LogFlag.bLogOn) Log.d(TAG, "storeLocationFinish");
        loaderGif.setVisibility(View.GONE);
        mapContentLayout.setVisibility(View.VISIBLE);

    }


     void setStoreLocationRequestData() {

         vLocationTrackRequest.setUser("Meera");
         vLocationTrackRequest.setLatitude(vLocationTrack.getLatitude());
         vLocationTrackRequest.setLongitude(vLocationTrack.getLongitude());
         vLocationTrackRequest.setLocation(vLocationTrack.getLocation());

     }

    @Background
    public void trackLocation() {
        if (LogFlag.bLogOn)Log.d(TAG, "trackLocation");
        VCarRestClient vCarRestClient = new VCarRestClient();
        VLocationTrackResponse vLocationTrackResponse = vCarRestClient.locationTrack("Meera");
        if(null != vLocationTrackResponse){
            if (LogFlag.bLogOn) Log.d(TAG, "vLocationTrackResponse: "+vLocationTrackResponse.toString());
            storeLocationFinish();
        }

    }
}


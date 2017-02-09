package com.vpage.vcars.activity;

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
import com.vpage.vcars.adapter.HomeFragmentAdapter;
import com.vpage.vcars.pojos.VLocation;
import com.vpage.vcars.tools.ConnectionDetector;
import com.vpage.vcars.tools.VCarsApplication;
import com.vpage.vcars.tools.VPreferences;
import com.vpage.vcars.tools.fab.FloatingActionsMenu;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.ViewById;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import com.vpage.vcars.tools.fab.FloatingActionButton;
import com.vpage.vcars.tools.utils.LogFlag;

import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.util.List;

@EActivity(R.layout.activity_home)
@OptionsMenu(R.menu.menu_home)
@Fullscreen
public class HomeActivity  extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final String TAG = HomeActivity.class.getName();


    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.drawer_layout)
    DrawerLayout drawer;

    @ViewById(R.id.nav_view)
    NavigationView navigationView;

    @ViewById(R.id.viewpager)
    ViewPager viewPager;

    @ViewById(R.id.tabLayout)
    TabLayout tabLayout;

    @ViewById(R.id.tabContentLayout)
    RelativeLayout tabContentLayout;

    @ViewById(R.id.mapContentLayout)
    RelativeLayout mapContentLayout;

    @ViewById(R.id.fab_base_layout)
    RelativeLayout fabBaseLayout;

    @ViewById(R.id.multiple_actions)
    FloatingActionsMenu floatingActionsMenu;

    SupportMapFragment mapFragment;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    LatLng latLng;

    private GoogleMap mMap;

    Marker currLocationMarker;

    String[] tabItems;


    @AfterViews
    public void onInitHome() {
        setActionBarSupport();


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();


        navigationView.setNavigationItemSelectedListener(this);

        if (floatingActionsMenu.isExpanded()) {
            fabBaseLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            floatingActionsMenu.toggle();
        }
        SetFABButton();
        showTab();

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    Boolean checkNetworkConnection() {
        ConnectionDetector connectionDetector = new ConnectionDetector(HomeActivity.this);
        Boolean isInternetPresent = connectionDetector.isConnectingToInternet();
        return isInternetPresent;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
    }


    @Override
    public void onMapReady(GoogleMap gMap) {
        mMap = gMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);

        Boolean isInternetPresent = checkNetworkConnection();
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
    }


    private void setActionBarSupport() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle("");

    }


    public void showTab() {

        tabItems = getResources().getStringArray(R.array.homeTabTitle);
        tabLayout.setTabTextColors(getResources().getColor(R.color.text_color), getResources().getColor(R.color.colorAccent));
        tabLayout.addTab(tabLayout.newTab().setText(tabItems[0]));
        tabLayout.addTab(tabLayout.newTab().setText(tabItems[1]));
        tabLayout.addTab(tabLayout.newTab().setText(tabItems[2]));
        tabLayout.addTab(tabLayout.newTab().setText(tabItems[3]));

        HomeFragmentAdapter adapter = new HomeFragmentAdapter(getSupportFragmentManager(), tabLayout.getTabCount(),  HomeActivity.this);
        viewPager.setAdapter(adapter);

            tabLayout.getTabAt(0).select();
            viewPager.setCurrentItem(0);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }




    private void SetFABButton() {

        final FloatingActionButton viewStatus= new FloatingActionButton(getBaseContext());
        viewStatus.setIconDrawable(getResources().getDrawable(R.drawable.mapviewicon));
        viewStatus.setTitle("Map View");
        tabContentLayout.setVisibility(View.VISIBLE);

        floatingActionsMenu.addButton(viewStatus);

        View.OnClickListener listener = new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {

                if (floatingActionsMenu.isExpanded()) {
                    fabBaseLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                } else {
                    fabBaseLayout.setBackground(getResources().getDrawable(R.drawable.fab_label_background));
                }
                floatingActionsMenu.toggle();
            }
        };

        floatingActionsMenu.setAddButtonClickListener(listener);


        viewStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fabBaseLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                floatingActionsMenu.toggle();

                if(viewStatus.getTitle().equals("Map View")){
                    viewStatus.setIconDrawable(getResources().getDrawable(R.drawable.listviewicon));
                    viewStatus.setTitle("List View");
                    mapContentLayout.setVisibility(View.VISIBLE);
                    tabContentLayout.setVisibility(View.GONE);


                }else{
                    viewStatus.setIconDrawable(getResources().getDrawable(R.drawable.mapviewicon));
                    viewStatus.setTitle("Map View");
                    tabContentLayout.setVisibility(View.VISIBLE);
                    mapContentLayout.setVisibility(View.GONE);
                }
            }
        });

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.home) {
            return true;
        }else if((id == R.id.share)){
            callShareIntent();
        }

        return super.onOptionsItemSelected(item);
    }


    void callShareIntent(){

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/html");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, Html.fromHtml("<p>This is the code to share</p>"));
        startActivity(Intent.createChooser(sharingIntent,"Share code to Friends"));
    }
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}


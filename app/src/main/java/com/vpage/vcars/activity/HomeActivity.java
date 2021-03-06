package com.vpage.vcars.activity;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
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
import com.google.android.gms.plus.Plus;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;
import com.vpage.vcars.R;
import com.vpage.vcars.adapter.HomeFragmentAdapter;
import com.vpage.vcars.chat.ChatActivity;
import com.vpage.vcars.pojos.CarDetail;
import com.vpage.vcars.pojos.VLocation;
import com.vpage.vcars.tools.CarListCallBack;
import com.vpage.vcars.tools.CustomSearchView;
import com.vpage.vcars.tools.TabMessage;
import com.vpage.vcars.tools.VCarGooglePlusTools;
import com.vpage.vcars.tools.VCarsApplication;
import com.vpage.vcars.tools.VPreferences;
import com.vpage.vcars.tools.VTools;
import com.vpage.vcars.tools.fab.FloatingActionsMenu;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.OptionsItem;
import org.androidannotations.annotations.OptionsMenu;
import org.androidannotations.annotations.OptionsMenuItem;
import org.androidannotations.annotations.ViewById;
import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import com.vpage.vcars.tools.fab.FloatingActionButton;
import com.vpage.vcars.tools.utils.LogFlag;
import android.content.pm.PackageManager;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@EActivity(R.layout.activity_home)
@Fullscreen
public class HomeActivity  extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, BottomNavigationView.OnNavigationItemSelectedListener, AdapterView.OnItemClickListener, CarListCallBack {

    private static final String TAG = HomeActivity.class.getName();


    @ViewById(R.id.toolbar)
    Toolbar toolbar;

    @ViewById(R.id.bottom_navigation)
    BottomNavigationView bottomNavigationView;

    MenuItem searchMenuItem,homeMenuItem,userMenuItem,favouriteMenuItem,overflowMenuItem;

    CustomSearchView searchView;

 /*   @ViewById(R.id.bottomBar)
    BottomBar bottomBar;*/

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

    @ViewById(R.id.container_app_bar)
    LinearLayout mContainerToolbar;

    @ViewById(R.id.fabMenuTitle)
    TextView fabMenuTitle;

    MenuItem item;

    @Bean
    VCarGooglePlusTools vCarGooglePlusTools;

    SupportMapFragment mapFragment;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    LatLng latLng;

    private GoogleMap mMap;

    Marker currLocationMarker;

    String[] tabItems;

    PopupWindow PopUp;
    int currentTab = 0;



    @AfterViews
    public void onInitHome() {
      //  animateToolbarDroppingDown(mContainerToolbar);
        setActionBarSupport();

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        mContainerToolbar.setBackgroundColor(getResources().getColor(R.color.colorAccent));

        navigationView.setNavigationItemSelectedListener(this);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        fabMenuTitle.setVisibility(View.VISIBLE);
        floatingActionsMenu.setVisibility(View.VISIBLE);

        fabMenuTitle.setText("Map View");
        if (floatingActionsMenu.isExpanded()) {
            fabBaseLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            floatingActionsMenu.toggle();
        }
        SetFABButton();
        setBottomNavigationViewMenu();

        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


      /*  bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_user:
                        mContainerToolbar.setBackgroundColor(getResources().getColor(R.color.red));
                        if (LogFlag.bLogOn) Log.d(TAG, "ItemSelected : "+ TabMessage.get(tabId, false));
                        break;
                    case R.id.tab_home:
                        mContainerToolbar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                        if (LogFlag.bLogOn) Log.d(TAG, "ItemSelected : "+TabMessage.get(tabId, false));
                        break;
                    case R.id.tab_favorites:
                        mContainerToolbar.setBackgroundColor(getResources().getColor(R.color.brown));
                        if (LogFlag.bLogOn) Log.d(TAG, "ItemSelected : "+TabMessage.get(tabId, false));
                        break;
                    case R.id.tab_overflow:
                        mContainerToolbar.setBackgroundColor(getResources().getColor(R.color.violet));
                        if (LogFlag.bLogOn) Log.d(TAG, "ItemSelected : "+TabMessage.get(tabId, false));

                        String [] textPosition = new String[]{"Share", "Current Driving", "Report","Feedback"};
                        int[] imagesIcons = new int[]{
                                (R.drawable.share_white),
                                (R.drawable.car_white),
                                (R.drawable.report_white),
                                (R.drawable.feedback_white)
                        };

                        setSharePopupView(textPosition, imagesIcons);
                        break;
                    case R.id.tab_search:
                        mContainerToolbar.setBackgroundColor(getResources().getColor(R.color.orange));
                        if (LogFlag.bLogOn) Log.d(TAG, "ItemSelected : "+TabMessage.get(tabId, false));

                        break;

                }
            }
        });*/
    }


    void setBottomNavigationViewMenu(){
        searchMenuItem = bottomNavigationView.getMenu().findItem(R.id.search);
        homeMenuItem = bottomNavigationView.getMenu().findItem(R.id.home);
        userMenuItem = bottomNavigationView.getMenu().findItem(R.id.user);
        favouriteMenuItem = bottomNavigationView.getMenu().findItem(R.id.favourite);
        overflowMenuItem = bottomNavigationView.getMenu().findItem(R.id.overflow);
        if(searchMenuItem != null){
            showTab();
        }

    }


    void searchSelected() {

        searchView = (CustomSearchView) MenuItemCompat.getActionView(searchMenuItem);
     //   searchView = (CustomSearchView) bottomNavigationView.getMenu().findItem(R.id.search);

        if(searchView != null){
            if (LogFlag.bLogOn) Log.d(TAG, "CustomSearchView : "+searchView.getImeOptions());
        }

        View view = searchView.findViewById(R.id.search);
        view.setBackgroundColor(getResources().getColor(R.color.White));
        searchView.setQueryHint(getResources().getString(R.string.actionSearch));

        TextView textView = (TextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        textView.setTextColor(getResources().getColor(R.color.Black));
        textView.setHintTextColor(getResources().getColor(R.color.Black));

        ImageView searchCloseIcon = (ImageView)searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        searchCloseIcon.setImageResource(R.drawable.close);

        searchView.setOnSearchViewCollapsedEventListener(listenerCollapse);
        searchView.setOnSearchViewExpandedEventListener(listenerExpand);

    }

    final private CustomSearchView.OnSearchViewCollapsedEventListener listenerCollapse = new CustomSearchView.OnSearchViewCollapsedEventListener() {

        @Override
        public void onSearchViewCollapsed() {
            // show other actions

            homeMenuItem.setVisible(true);
            userMenuItem.setVisible(true);
            favouriteMenuItem.setVisible(true);
            overflowMenuItem.setVisible(true);

            // I'm doing my actual search here
        }
    };

    final private CustomSearchView.OnSearchViewExpandedEventListener listenerExpand = new CustomSearchView.OnSearchViewExpandedEventListener() {

        @Override
        public void onSearchViewExpanded() {
            // hide other actions


            homeMenuItem.setVisible(false);
            userMenuItem.setVisible(false);
            favouriteMenuItem.setVisible(false);
            overflowMenuItem.setVisible(false);

        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);

        Boolean isInternetPresent = VTools.checkNetworkConnection(HomeActivity.this);
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

            // to be used after service call made to have multiple markers on cars location in renters view

          /*  Double[] latitudeArray = {13.0636,13.0713,13.0440,12.5922,12.5833};
            Double[] longitudeArray = {80.1012,80.0636,80.1542,80.1452,80.1315};
             for(int i=0;i<latitudeArray.length;i++){
                 latLng = new LatLng(latitudeArray[i], longitudeArray[i]);
                 MarkerOptions markerOptions = new MarkerOptions();
                 markerOptions.position(latLng);
                 markerOptions.title(vLocation.getAddress());
                 markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
                 currLocationMarker = mMap.addMarker(markerOptions);
             }  */


        }else {
            if (LogFlag.bLogOn) Log.i(TAG, "Not able to get Location");
        }

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {

            @Override
            public boolean onMarkerClick(Marker arg0) {
              //  if(arg0.getTitle().equals("MyHome")) // if marker source is clicked
                gotoCarDetailPage();
                return true;
            }

        });
    }

    private void gotoCarDetailPage() {

        Intent intent = new Intent(getApplicationContext(), CarDetailActivity_.class);
        intent.putExtra("SelectedCar","Car Selected");
        startActivity(intent);
        VTools.animation(this);
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

        String[] carName = new String[] { "China", "India", "United States",
                "Indonesia", "Brazil", "Pakistan", "Nigeria", "Bangladesh",
                "Russia", "Japan" };

        List<CarDetail> carDetailList = new ArrayList<>();  // to be set after service call


        for (int i = 0; i < carName.length; i++)
        {
            CarDetail carDetail = new CarDetail();
            carDetail.setCarName(carName[i]);
            carDetailList.add(carDetail);
        }


     //   tabLayout.setupWithViewPager(viewPager);
        HomeFragmentAdapter adapter = new HomeFragmentAdapter(getSupportFragmentManager(),tabLayout.getTabCount(),HomeActivity.this,searchMenuItem,carDetailList);
        adapter.onCallBackToListScroll(this);
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);
        tabLayout.getTabAt(0).select();
        viewPager.setCurrentItem(0);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                viewPager.setCurrentItem(tab.getPosition());
             /*   if (LogFlag.bLogOn) Log.d(TAG, "TabSelected: "+tab.getPosition());

                View currentView = viewPager.getChildAt(tab.getPosition()-1);
                if(currentView != null){

                    Animation animation = AnimationUtils.loadAnimation(HomeActivity.this, ((tab.getPosition()-1) > currentTab) ? R.anim.slide_out_right : R.anim.slide_in_left);
                    currentView.startAnimation(animation);
                    currentTab = tab.getPosition()-1;

                }else {
                    if (LogFlag.bLogOn) Log.d(TAG, "currentView is empty");
                }*/
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
/*        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(viewPager));
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                viewPager.setCurrentItem(position);
                if (LogFlag.bLogOn) Log.d(TAG, "TabSelected: "+position);

                View currentView = viewPager.getChildAt(position-1);
                if(currentView != null){

                    Animation animation = AnimationUtils.loadAnimation(HomeActivity.this, ((position-1) > currentTab) ? R.anim.slide_out_right : R.anim.slide_in_left);
                    currentView.startAnimation(animation);
                    currentTab = position-1;

                }else {
                    if (LogFlag.bLogOn) Log.d(TAG, "currentView is empty");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });*/


    }


    private void SetFABButton() {

        /*final FloatingActionButton viewStatus= new FloatingActionButton(getBaseContext());
        viewStatus.setIconDrawable(getResources().getDrawable(R.drawable.mapviewicon));
        viewStatus.setTitle("Map View");*/

        final FloatingActionButton chatButton= new FloatingActionButton(getBaseContext());
        chatButton.setIconDrawable(getResources().getDrawable(R.drawable.chaticon));
        chatButton.setTitle("Chat");

        final FloatingActionButton carDetailButton= new FloatingActionButton(getBaseContext());
        carDetailButton.setIconDrawable(getResources().getDrawable(R.drawable.attachmenticon));
        carDetailButton.setTitle("Attachment");

        tabContentLayout.setVisibility(View.VISIBLE);

       // floatingActionsMenu.addButton(viewStatus);
        floatingActionsMenu.addButton(chatButton);
        floatingActionsMenu.addButton(carDetailButton);

        View.OnClickListener listener = new View.OnClickListener() {
            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {
                if(fabMenuTitle.getText().equals("Map View")){
                    fabMenuTitle.setText("List View");
                    mapContentLayout.setVisibility(View.VISIBLE);
                    tabContentLayout.setVisibility(View.GONE);

                }else if(fabMenuTitle.getText().equals("List View")){
                    fabMenuTitle.setText("Map View");
                    tabContentLayout.setVisibility(View.VISIBLE);
                    mapContentLayout.setVisibility(View.GONE);
                }

                if (floatingActionsMenu.isExpanded()) {
                    fabBaseLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                } else {
                    fabBaseLayout.setBackground(getResources().getDrawable(R.drawable.fab_label_background));
                }
                floatingActionsMenu.toggle();
            }
        };

        floatingActionsMenu.setAddButtonClickListener(listener);


       /* viewStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fabBaseLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                floatingActionsMenu.toggle();

                if(viewStatus.getTitle().equals("Map View")){
                    viewStatus.setIconDrawable(getResources().getDrawable(R.drawable.listviewicon));
                    viewStatus.setTitle("List View");
                    mapContentLayout.setVisibility(View.VISIBLE);
                    tabContentLayout.setVisibility(View.GONE);

                }else if(viewStatus.getTitle().equals("List View")){
                    viewStatus.setIconDrawable(getResources().getDrawable(R.drawable.mapviewicon));
                    viewStatus.setTitle("Map View");
                    tabContentLayout.setVisibility(View.VISIBLE);
                    mapContentLayout.setVisibility(View.GONE);
                }
            }
        });
*/
        carDetailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabBaseLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                floatingActionsMenu.toggle();
                gotoCarAttachmentPage();
            }
        });

        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabBaseLayout.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                floatingActionsMenu.toggle();
                gotoChatPage();
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

 /*   @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    //    getMenuInflater().inflate(R.menu.menu_home, menu);


        setMenu(menu);
        return true;
    }

    void setMenu(Menu menu){
        menu.add(0, 1, 1, menuIconWithText(getResources().getDrawable(R.drawable.shareicon), "Share"));
        menu.add(0, 2, 2, menuIconWithText(getResources().getDrawable(R.drawable.caricon), "Current Driving"));
        menu.add(0, 3, 3, menuIconWithText(getResources().getDrawable(R.drawable.reporticon), "Report"));

    }

    private CharSequence menuIconWithText(Drawable drawable, String title) {

        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        SpannableString spannableString = new SpannableString("  "+title);
        ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BOTTOM);
        spannableString.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        return spannableString;
    }
*/


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
        this.item = item;
        switch (id) {
            case R.id.nav_camera:
                break;
            case R.id.nav_gallery:
                break;
            case R.id.nav_manage:
                break;
            case R.id.nav_share:
                break;
            case R.id.nav_send:
                break;
            case R.id.nav_logout:
                onLogout();
                break;
            case R.id.home:
                mContainerToolbar.setBackgroundColor(getResources().getColor(R.color.colorAccent));
                if (LogFlag.bLogOn) Log.d(TAG, "ItemSelected : "+item.getTitle());
                break;
            case R.id.user:
                mContainerToolbar.setBackgroundColor(getResources().getColor(R.color.red));
                if (LogFlag.bLogOn) Log.d(TAG, "ItemSelected : "+item.getTitle());
                break;
            case R.id.favourite:
                mContainerToolbar.setBackgroundColor(getResources().getColor(R.color.brown));
                if (LogFlag.bLogOn) Log.d(TAG, "ItemSelected : "+item.getTitle());
                break;
            case R.id.overflow:
                mContainerToolbar.setBackgroundColor(getResources().getColor(R.color.violet));
                if (LogFlag.bLogOn) Log.d(TAG, "ItemSelected : "+item.getTitle());

                String [] textPosition = new String[]{"Share", "Current Driving", "Report","Feedback"};
                int[] imagesIcons = new int[]{
                        (R.drawable.share_white),
                        (R.drawable.car_white),
                        (R.drawable.report_white),
                        (R.drawable.feedback_white)
                };

                setSharePopupView(textPosition, imagesIcons);
                break;
            case R.id.search:
                mContainerToolbar.setBackgroundColor(getResources().getColor(R.color.orange));
                if (LogFlag.bLogOn) Log.d(TAG, "ItemSelected : "+item.getTitle());

                searchSelected();
                break;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void onLogout(){
        String loginType = VPreferences.get("loginType");

        if (loginType.equalsIgnoreCase("GoogleApp") && vCarGooglePlusTools.getmGoogleApiClient().isConnected()) {
            Log.d(TAG, "Logout from google");
            Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                    new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status status) {

                        }
                    });
            Plus.AccountApi.clearDefaultAccount(vCarGooglePlusTools.getmGoogleApiClient());
            vCarGooglePlusTools.getmGoogleApiClient().disconnect();

        }
        if (loginType.equalsIgnoreCase("FacebookApp")) {
            Log.d(TAG, "Logout from facebook");
            LoginManager.getInstance().logOut();
        }

        if (loginType.equalsIgnoreCase("VCars")) {
            Log.d(TAG, "Logout from VCars");
            VPreferences.clearAll();
        }
        VPreferences.save("isLoggedIn", "false");
        gotoSignInPage();
    }


    private void gotoSignInPage() {

        Intent intent = new Intent(getApplicationContext(), SigninActivity_.class);
        startActivity(intent);
        VTools.animation(this);

    }

    private void gotoCurrentCarTrackPage() {

        Intent intent = new Intent(getApplicationContext(), CurrentCarTrackActivity_.class);
        startActivity(intent);
        VTools.animation(this);
    }

    private void gotoCurrentCarViewPage() {

        Intent intent = new Intent(getApplicationContext(), CurrentCarViewActivity_.class);
        startActivity(intent);
        VTools.animation(this);
    }

    private void gotoReportPage() {

        Intent intent = new Intent(getApplicationContext(), ReportActivity_.class);
        startActivity(intent);
        VTools.animation(this);
    }


    private void gotoFeedbackPage() {

        Intent intent = new Intent(getApplicationContext(), FeedbackActivity_.class);
        startActivity(intent);
        VTools.animation(this);
    }

    private void gotoCarAttachmentPage() {

        Intent intent = new Intent(getApplicationContext(), CarAttachmentActivity_.class);
        startActivity(intent);
        VTools.animation(this);
    }

    private void gotoChatPage() {

        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
        startActivity(intent);
        VTools.animation(this);
    }

    public static void animateToolbarDroppingDown(View containerToolbar) {

        containerToolbar.setRotationX(-90);
        containerToolbar.setAlpha(0.2F);
        containerToolbar.setPivotX(0.0F);
        containerToolbar.setPivotY(0.0F);
        Animator alpha = ObjectAnimator.ofFloat(containerToolbar, "alpha", 0.2F, 0.4F, 0.6F, 0.8F, 1.0F).setDuration(4000);
        Animator rotationX = ObjectAnimator.ofFloat(containerToolbar, "rotationX", -90, 60, -45, 45, -10, 30, 0, 20, 0, 5, 0).setDuration(8000);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setInterpolator(new DecelerateInterpolator());
        animatorSet.playTogether(alpha, rotationX);
        animatorSet.start();
    }


    void setSharePopupView(final String[] textPosition, int[] imagesIcons) {

        View popUpView = getLayoutInflater().inflate(R.layout.popupview, null); // inflating popup layout
        PopUp = VTools.createPopUp(popUpView);


        PopUp.setBackgroundDrawable(new BitmapDrawable());
        PopUp.setOutsideTouchable(true);

        TextView popUpTitle = (TextView) popUpView.findViewById(R.id.popUpTitle);
        ListView listView = (ListView) popUpView.findViewById(R.id.listView);
        final ImageButton btnClose = (ImageButton) popUpView.findViewById(R.id.btnClose);


         popUpTitle.setText("OverFlow Menu");


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
                callShareIntent();
                break;
            case 1:
                // Current Driving
                if (LogFlag.bLogOn) Log.d(TAG, "ItemSelected 2: " + i);
                gotoCurrentCarTrackPage();
                break;
            case 2:
                // Report
                if (LogFlag.bLogOn) Log.d(TAG, "ItemSelected 3: " + i);
                gotoReportPage();
                break;
            case 3:
                // Feedback
                if (LogFlag.bLogOn) Log.d(TAG, "ItemSelected 3: " + i);
                gotoFeedbackPage();
                break;
        }
        PopUp.dismiss();
    }

    @Override
    public void onListScroll(int scrollState){
        if (scrollState == 1) {
            fabMenuTitle.setVisibility(View.GONE);
            floatingActionsMenu.setVisibility(View.INVISIBLE);

        }else {
            fabMenuTitle.setVisibility(View.VISIBLE);
            floatingActionsMenu.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onListClick() {

    }


}


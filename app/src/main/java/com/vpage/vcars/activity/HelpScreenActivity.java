package com.vpage.vcars.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.vpage.vcars.R;
import com.vpage.vcars.adapter.HelpFragmentAdapter;
import com.vpage.vcars.tools.CallBackToHelpScreenListener;
import com.vpage.vcars.tools.VTools;
import com.vpage.vcars.viewpagerindicator.CirclePageIndicator;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.ViewById;

import java.util.Timer;
import java.util.TimerTask;

@EActivity(R.layout.activity_helpscreen)
public class HelpScreenActivity extends FragmentActivity implements CallBackToHelpScreenListener {

    String TAG = HelpScreenActivity.class.getName();

    @ViewById(R.id.pageTitle)
    TextView pageTitle;

    @ViewById(R.id.relativeLayout)
    RelativeLayout baseLayout;

    @ViewById(R.id.pager)
    ViewPager mPager;

    @ViewById(R.id.indicator)
    CirclePageIndicator mIndicator;

    boolean isAppWentToBg = false;

    boolean isWindowFocused = false;

    boolean isBackPressed = false;

    HelpFragmentAdapter mAdapter;
    public static int currentSelectedItem = 0;
    Timer swipeTimer;


    @Override
    protected void onStart() {
        Log.d(TAG, "onStart isAppWentToBg " + isAppWentToBg);

        applicationWillEnterForeground();
        super.onStart();
        VTools.forwardTransition(HelpScreenActivity.this);
    }


    @AfterViews
    public void initHelpScreenView() {
        VTools.setActivity(this);
        VTools.setDisplayMetrics();

        mPager.setVisibility(View.VISIBLE);
        mIndicator.setVisibility(View.VISIBLE);
      /*  if (MTools.displayMetrics.widthPixels < getResources().getInteger(R.integer.lowScreenWidth)) {
            RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) baseLayout.getLayoutParams();
            params2.setMargins(0, 0, 0, 110); //substitute parameters for left, top, right, bottom
            baseLayout.setLayoutParams(params2);
        } else if (MTools.displayMetrics.widthPixels <= getResources().getInteger(R.integer.smallScreenWidth)) {
            if (MTools.displayMetrics.heightPixels <= getResources().getInteger(R.integer.smallScreenHeight)) {
                RelativeLayout.LayoutParams mPagerparams = (RelativeLayout.LayoutParams) mPager.getLayoutParams();
                mPagerparams.setMargins(10, 90, 10, 40); //substitute parameters for left, top, right, bottom
                mPager.setLayoutParams(mPagerparams);
                RelativeLayout.LayoutParams indicatorparams2 = (RelativeLayout.LayoutParams) mIndicator.getLayoutParams();
                indicatorparams2.setMargins(0, 0, 0, 10); //substitute parameters for left, top, right, bottom
                mIndicator.setLayoutParams(indicatorparams2);
            } else {
                RelativeLayout.LayoutParams mPagerparams = (RelativeLayout.LayoutParams) mPager.getLayoutParams();
                mPagerparams.setMargins(10, 0, 10, 0); //substitute parameters for left, top, right, bottom
                mPager.setLayoutParams(mPagerparams);
                RelativeLayout.LayoutParams indicatorparams = (RelativeLayout.LayoutParams) mIndicator.getLayoutParams();
                indicatorparams.setMargins(0, 0, 0, 10); //substitute parameters for left, top, right, bottom
                mIndicator.setLayoutParams(indicatorparams);
            }
        } else if (MTools.displayMetrics.widthPixels == getResources().getInteger(R.integer.mediumScreenWidth)) {
            RelativeLayout.LayoutParams mPagerparams = (RelativeLayout.LayoutParams) mPager.getLayoutParams();
            mPagerparams.setMargins(10, 0, 10, 0); //substitute parameters for left, top, right, bottom
            mPager.setLayoutParams(mPagerparams);
            RelativeLayout.LayoutParams indicatorparams = (RelativeLayout.LayoutParams) mIndicator.getLayoutParams();
            indicatorparams.setMargins(0, 0, 0, 5); //substitute parameters for left, top, right, bottom
            mIndicator.setLayoutParams(indicatorparams);
        } else if (MTools.displayMetrics.widthPixels == getResources().getInteger(R.integer.highScreenWidth)) {
            RelativeLayout.LayoutParams mPagerparams = (RelativeLayout.LayoutParams) mPager.getLayoutParams();
            mPagerparams.setMargins(10, 0, 10, 0); //substitute parameters for left, top, right, bottom
            mPager.setLayoutParams(mPagerparams);
            RelativeLayout.LayoutParams indicatorparams = (RelativeLayout.LayoutParams) mIndicator.getLayoutParams();
            indicatorparams.setMargins(0, 0, 0, 10); //substitute parameters for left, top, right, bottom
            mIndicator.setLayoutParams(indicatorparams);
        } else {
            RelativeLayout.LayoutParams mPagerparams = (RelativeLayout.LayoutParams) mPager.getLayoutParams();
            mPagerparams.setMargins(10, 0, 10, 0); //substitute parameters for left, top, right, bottom
            mPager.setLayoutParams(mPagerparams);
            RelativeLayout.LayoutParams indicatorparams = (RelativeLayout.LayoutParams) mIndicator.getLayoutParams();
            indicatorparams.setMargins(0, 0, 0, 0); //substitute parameters for left, top, right, bottom
            mIndicator.setLayoutParams(indicatorparams);
        }*/

        pageTitle.setRight(150);

        mAdapter = new HelpFragmentAdapter(getSupportFragmentManager());
        mPager.setAdapter(mAdapter);
        mAdapter.onCallBackToHelpScreen(this);

        mIndicator.setViewPager(mPager);
        mPager.setCurrentItem(currentSelectedItem);
        mIndicator.setCurrentItem(currentSelectedItem);
        mPager.setOnPageChangeListener(new CircularViewPagerHandler(mPager, mIndicator));

        swipeTimer = new Timer();
        swipeTimer.schedule(new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int count;
                        if (currentSelectedItem == mAdapter.getCount()) {
                            currentSelectedItem = count = 0;
                        } else {
                            count = currentSelectedItem++;
                        }

                        Log.d(TAG, "pagerLength:"+ mPager.getChildCount());
                        Log.d(TAG, "pagerNumber:"+ mPager.getCurrentItem());

                       /* if( mPager.getChildCount() == mPager.getCurrentItem() ){
                            button.setText("Next");
                        }else {
                            button.setText("Skip");
                        }*/

                        mPager.setCurrentItem(count, true);
                        mIndicator.setCurrentItem(count);
                    }
                });
            }
        }, getResources().getInteger(R.integer.Delay), getResources().getInteger(R.integer.Period));

        mIndicator.setViewPager(mPager);
        final float density = getResources().getDisplayMetrics().density;
        mIndicator.setBackgroundColor(Color.TRANSPARENT);
        mIndicator.setRadius(4 * density);
        mIndicator.setPageColor(Color.YELLOW);
        mIndicator.setFillColor(Color.GREEN);
        mIndicator.setStrokeColor(Color.TRANSPARENT);
        mIndicator.setStrokeWidth(2 * density);

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
        applicationdidenterbackground();
    }


    private void applicationWillEnterForeground() {
        if (isAppWentToBg) {
            isAppWentToBg = false;
            // Toast.makeText(getApplicationContext(), "App is in foreground", Toast.LENGTH_SHORT).show();
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
        if (this instanceof HelpScreenActivity) {

        } else {
            isBackPressed = true;
        }
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void gotoOTPGenerationPage() {

        Intent intent = new Intent(getApplicationContext(), GenerateOTPActivity_.class);
        startActivity(intent);
        VTools.animation(this);
    }

    @Override
    public void onButtonClick() {
        gotoOTPGenerationPage();
    }

    public class CircularViewPagerHandler implements ViewPager.OnPageChangeListener {
        private ViewPager mViewPager;
        private int mScrollState;
        private CirclePageIndicator indicator;

        public CircularViewPagerHandler(final ViewPager viewPager, CirclePageIndicator mIndicator) {
            mViewPager = viewPager;
            indicator = mIndicator;
        }

        @Override
        public void onPageSelected(final int position) {
            currentSelectedItem = position;
            mViewPager.setCurrentItem(position, false);
            indicator.setCurrentItem(position);
        }

        @Override
        public void onPageScrollStateChanged(final int state) {
            handleScrollState(state);
            mScrollState = state;
        }

        private void handleScrollState(final int state) {
            if (state == ViewPager.SCROLL_STATE_IDLE) {
                setNextItemIfNeeded();
            }
        }

        private void setNextItemIfNeeded() {
            if (!isScrollStateSettling()) {
                handleSetNextItem();
            }
        }

        private boolean isScrollStateSettling() {
            return mScrollState == ViewPager.SCROLL_STATE_SETTLING;
        }

        private void handleSetNextItem() {
            final int lastPosition = mViewPager.getAdapter().getCount() - 1;
            if (currentSelectedItem == 0) {
                mViewPager.setCurrentItem(lastPosition, false);
                indicator.setCurrentItem(lastPosition);

            } else if (currentSelectedItem == lastPosition) {
                mViewPager.setCurrentItem(0, false);
                indicator.setCurrentItem(0);

            }
        }

        @Override
        public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {


        }
    }
}






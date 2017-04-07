package com.vpage.vcars.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.MenuItem;
import android.widget.EditText;

import com.vpage.vcars.fragment.MiddleFragment_;
import com.vpage.vcars.fragment.MiniFragment_;
import com.vpage.vcars.fragment.PrimeFragment_;
import com.vpage.vcars.fragment.SUVFragment_;
import com.vpage.vcars.pojos.CarDetail;
import com.vpage.vcars.tools.CarListCallBack;

import java.util.List;

public class HomeFragmentAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = HomeFragmentAdapter.class.getName();

    int mNumOfTabs;
    Bundle bundle;
    Activity activity;
    MenuItem searchMenuItem;
    List<CarDetail> carDetailList;
    CarListCallBack carListCallBack;


    public HomeFragmentAdapter(FragmentManager fm, int NumOfTabs, Activity activity,MenuItem searchMenuItem,List<CarDetail> carDetailList) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.activity = activity;
        this.searchMenuItem = searchMenuItem;
        this.carDetailList = carDetailList;
        bundle=new Bundle();
        bundle.putString("Data", "");
    }


    public void onCallBackToListScroll(CarListCallBack carListCallBack) {
        this.carListCallBack = carListCallBack;
    }



    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                MiniFragment_ miniFragment = new MiniFragment_();
                miniFragment.setCarListCallBack(carListCallBack);
                miniFragment.setSearchMenuItem(searchMenuItem);
                miniFragment.setCarDetailList(carDetailList);
                miniFragment.setArguments(bundle);
                return miniFragment;

            case 1:
                MiddleFragment_ middleFragment = new MiddleFragment_();
                middleFragment.setCarListCallBack(carListCallBack);
                middleFragment.setSearchMenuItem(searchMenuItem);
                middleFragment.setCarDetailList(carDetailList);
                middleFragment.setArguments(bundle);
                return middleFragment;

            case 2:
                SUVFragment_ suvFragment = new SUVFragment_();
                suvFragment.setCarListCallBack(carListCallBack);
                suvFragment.setSearchMenuItem(searchMenuItem);
                suvFragment.setCarDetailList(carDetailList);
                suvFragment.setArguments(bundle);
                return suvFragment;

            case 3:
                PrimeFragment_ primeFragment = new PrimeFragment_();
                primeFragment.setCarListCallBack(carListCallBack);
                primeFragment.setSearchMenuItem(searchMenuItem);
                primeFragment.setCarDetailList(carDetailList);
                primeFragment.setArguments(bundle);
                return primeFragment;


            default:
                return null;


        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}


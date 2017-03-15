package com.vpage.vcars.adapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.vpage.vcars.fragment.MiddleFragment_;
import com.vpage.vcars.fragment.MiniFragment_;
import com.vpage.vcars.fragment.PrimeFragment_;
import com.vpage.vcars.fragment.SUVFragment_;
import com.vpage.vcars.tools.ListScrollCallBack;

public class HomeFragmentAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = HomeFragmentAdapter.class.getName();

    int mNumOfTabs;
    Bundle bundle;
    Activity activity;


    public HomeFragmentAdapter(FragmentManager fm, int NumOfTabs, Activity activity) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.activity = activity;
        bundle=new Bundle();
        bundle.putString("Data", "");
    }
    ListScrollCallBack listScrollCallBack;

    public void onCallBackToListScroll(ListScrollCallBack listScrollCallBack) {
        this.listScrollCallBack = listScrollCallBack;
    }



    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                MiniFragment_ miniFragment = new MiniFragment_();
                miniFragment.setListScrollCallBack(listScrollCallBack);
                miniFragment.setArguments(bundle);
                return miniFragment;

            case 1:
                MiddleFragment_ middleFragment = new MiddleFragment_();
                middleFragment.setListScrollCallBack(listScrollCallBack);
                middleFragment.setArguments(bundle);
                return middleFragment;

            case 2:
                SUVFragment_ suvFragment = new SUVFragment_();
                suvFragment.setListScrollCallBack(listScrollCallBack);
                suvFragment.setArguments(bundle);
                return suvFragment;

            case 3:
                PrimeFragment_ primeFragment = new PrimeFragment_();
                primeFragment.setListScrollCallBack(listScrollCallBack);
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


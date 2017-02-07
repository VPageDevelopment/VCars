package com.vpage.vcars.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.vpage.vcars.R;
import com.vpage.vcars.fragment.HelpScreenFragment;
import com.vpage.vcars.tools.CallBackToHelpScreenListener;
import com.vpage.vcars.tools.VTools;
import com.vpage.vcars.tools.utils.LogFlag;


public class HelpFragmentAdapter extends FragmentPagerAdapter {

    String TAG = HelpFragmentAdapter.class.getName();

    CallBackToHelpScreenListener callBackToHelpScreenListener;

    private int[] offerImages = {
			R.drawable.slider1,
			R.drawable.slider2,
			R.drawable.slider3,
            R.drawable.slider4,
            R.drawable.slider5
	};

    static String[] CONTENT = VTools.getActivity().getResources().getStringArray(R.array.homeTextArray);

    private int mCount = offerImages.length;

    public HelpFragmentAdapter(FragmentManager fm) {
        super(fm);
    }

    public void onCallBackToHelpScreen(CallBackToHelpScreenListener callBackToHelpScreenListener) {
        this.callBackToHelpScreenListener = callBackToHelpScreenListener;
    }

    @Override
    public Fragment getItem(int position) {
        if (LogFlag.bLogOn)Log.d(TAG, String.valueOf(position));
        return new HelpScreenFragment(offerImages[position],CONTENT[position],callBackToHelpScreenListener);
    }

    @Override
    public int getCount() {
        return mCount;
    }

    @Override
    public CharSequence getPageTitle(int position) {
      return null;
    }

   
    public void setCount(int count) {
        if (count > 0 && count <= 8) {
            mCount = count;
            notifyDataSetChanged();
        }
    }
}
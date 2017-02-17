package com.vpage.vcars.adapter;


import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.vpage.vcars.R;
import com.vpage.vcars.tools.utils.LogFlag;

public class CarListAdapter extends BaseAdapter {

    private static final String TAG = CarListAdapter.class.getName();

    Activity activity;
    LayoutInflater mInflater;


    int listCount = 5;


    public CarListAdapter(Activity activity) {
        this.activity = activity;

    }

    @Override
    public int getCount() {
        return listCount;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int itemPosition, View convertView, ViewGroup parent) {
        mInflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        convertView = mInflater.inflate(R.layout.item_car, null);

        TextView carName = (TextView) convertView.findViewById(R.id.carName);
        TextView carDistance = (TextView) convertView.findViewById(R.id.carDistance);
        TextView carVarient = (TextView) convertView.findViewById(R.id.carVarient);

        if (LogFlag.bLogOn) Log.d(TAG, "itemPosition: "+itemPosition);
        carName.setText("FIGO");
        carDistance.setText("5 Kms");
        carVarient.setText("Petrol");

        return convertView;
    }


}

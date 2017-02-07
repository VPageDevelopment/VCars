package com.vpage.vcars.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.vpage.vcars.R;
import com.vpage.vcars.tools.VTools;

public class GridImageAdapter extends BaseAdapter {

    private static final String TAG = GridImageAdapter.class.getName();

    Activity activity;
    private LayoutInflater mInflater;
    int selectedPos = -1;
    TypedArray typedArrayImage;


    public GridImageAdapter(Activity activity, TypedArray typedArrayImage) {
        this.activity = activity;
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.typedArrayImage = typedArrayImage;

    }

    public void setSelectedPosition(int pos) {
        selectedPos = pos;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        int SIZE = 0;
        try {
            SIZE = typedArrayImage.length();
        } catch (NullPointerException e) {
            Log.d(TAG, e.toString());
        }
        return SIZE;
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
    public View getView(final int position, View convertView, final ViewGroup parent) {


        convertView = mInflater.inflate(R.layout.griditem_gallery, null);

        ImageView imageview = (ImageView) convertView.findViewById(R.id.thumbImage);
        RelativeLayout gridLayout  = (RelativeLayout) convertView.findViewById(R.id.gridLayout);

        Drawable drawable = typedArrayImage.getDrawable(position);
        imageview.setImageDrawable(drawable);

        if(selectedPos == position){
            VTools.setLayoutBackgroud(gridLayout,R.drawable.roundedcornerblue);
        }

        return convertView;
    }


}




package com.vpage.vcars.adapter;


import android.app.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.vpage.vcars.R;
import com.vpage.vcars.pojos.CarDetail;
import com.vpage.vcars.tools.utils.LogFlag;

import android.widget.Filter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CarListAdapter extends BaseAdapter implements Filterable {

    private static final String TAG = CarListAdapter.class.getName();

    Activity activity;
    LayoutInflater mInflater;
    private List<CarDetail> carDetailList = null;
    private List<CarDetail> arrayList;

    private Filter filter;
    Random randomColor;

    private int lastPosition = -1;

    public CarListAdapter(Activity activity,List<CarDetail> carDetailList) {
        this.activity = activity;
        this.carDetailList = carDetailList;
        this.arrayList = new ArrayList<>();
        this.arrayList.addAll(carDetailList);

       /* this.carDetailList = new ArrayList<>(carDetailList);
        this.arrayList = new ArrayList<>(carDetailList);*/
        this.filter = new CarFilter();

    }

    @Override
    public int getCount() {
        return carDetailList.size();
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

        Animation animation = AnimationUtils.loadAnimation(activity, (itemPosition > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        convertView.startAnimation(animation);
        lastPosition = itemPosition;

        TextView carName = (TextView) convertView.findViewById(R.id.carName);
        TextView carDistance = (TextView) convertView.findViewById(R.id.carDistance);
        TextView carVarient = (TextView) convertView.findViewById(R.id.carVarient);
        RelativeLayout contentLayout = (RelativeLayout) convertView.findViewById(R.id.contentLayout);

     //  if (LogFlag.bLogOn) Log.d(TAG, "itemPosition: "+itemPosition);
        carName.setText(carDetailList.get(itemPosition).getCarName());
        carDistance.setText("5 Kms");
        carVarient.setText("Petrol");

        contentLayout.setBackgroundColor(randomColor());

        return convertView;
    }

    int randomColor() {
        randomColor = new Random();
        int red = randomColor.nextInt(256);
        int green = randomColor.nextInt(256);
        int blue = randomColor.nextInt(256);
        return Color.rgb(red, green, blue);
    }
   /* // Filter Class
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        carDetailList.clear();
        if (charText.isEmpty()) {
            carDetailList.addAll(arrayList);
        }
        else
        {
            for (CarDetail carDetail : arrayList)
            {
                if (carDetail.getCarName().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    carDetailList.add(carDetail);
                }
            }
        }
        notifyDataSetChanged();
    }*/

    @Override
    public Filter getFilter(){

        if(filter == null){
            filter = new CarFilter();
        }
        return filter;
    }

    private class CarFilter extends Filter{
        @Override
        protected FilterResults performFiltering(CharSequence constraint){
            FilterResults results = new FilterResults();
            String prefix = constraint.toString().toLowerCase();

            if (prefix == null || prefix.length() == 0){
                ArrayList<CarDetail> list = new ArrayList<>(carDetailList);
                results.values = list;
                results.count = list.size();
            }else{
                final ArrayList<CarDetail> list = new ArrayList<>(carDetailList);
                final ArrayList<CarDetail> nlist = new ArrayList<>();
                int count = list.size();

                for (int i = 0; i<count; i++){
                    final CarDetail carDetail = list.get(i);
                    final String value = carDetail.getCarName().toLowerCase();

                    if(value.contains(prefix)){
                        nlist.add(carDetail);
                    }
                    results.values = nlist;
                    results.count = nlist.size();
                }
            }
            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            arrayList = (ArrayList<CarDetail>)results.values;
            notifyDataSetChanged();
            carDetailList.clear();
            int count = arrayList.size();
            for(int i = 0; i<count; i++){
                carDetailList.add(arrayList.get(i));
                notifyDataSetInvalidated();
            }
        }
    }


}

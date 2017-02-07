package com.vpage.vcars.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.vpage.vcars.R;
import com.vpage.vcars.activity.CarDetailActivity_;
import com.vpage.vcars.adapter.CarListAdapter;
import com.vpage.vcars.tools.VTools;
import com.vpage.vcars.tools.utils.LogFlag;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;


@EFragment(R.layout.fragment_mini)
public class SUVFragment extends Fragment {

    private static final String TAG = SUVFragment.class.getName();

    @ViewById(R.id.listView)
    ListView listView;

    CarListAdapter carListAdapter;

    @AfterViews
    public void initSUVFragment() {

        Bundle bundle = getArguments();
        String activeUserString = bundle.getString("Data");

        if (LogFlag.bLogOn) Log.d(TAG, activeUserString);

        carListAdapter = new CarListAdapter(getActivity());
        listView.setAdapter(carListAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (LogFlag.bLogOn) Log.d(TAG, "itemClickPosition: "+position);
                gotoCarDetailPage();

            }
        });
    }


    private void gotoCarDetailPage() {

        Intent intent = new Intent(getActivity(), CarDetailActivity_.class);
        intent.putExtra("SelectedCar","Car Selected");
        startActivity(intent);
        VTools.animation(getActivity());
    }
}


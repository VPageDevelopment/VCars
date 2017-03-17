package com.vpage.vcars.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.vpage.vcars.R;
import com.vpage.vcars.activity.CarDetailActivity_;
import com.vpage.vcars.adapter.CarListAdapter;
import com.vpage.vcars.pojos.CarDetail;
import com.vpage.vcars.tools.CarListCallBack;
import com.vpage.vcars.tools.VTools;
import com.vpage.vcars.tools.utils.LogFlag;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;


@SuppressLint("ValidFragment")
@EFragment(R.layout.fragment_mini)
public class MiniFragment extends Fragment {

    private static final String TAG = MiniFragment.class.getName();

    @ViewById(R.id.listView)
    ListView listView;

    @ViewById(R.id.noDataText)
    TextView noDataText;

    CarListAdapter carListAdapter;

    CarListCallBack carListCallBack;

    EditText editText;

    List<CarDetail> carDetailList;

    public void setCarListCallBack(CarListCallBack carListCallBack) {
        this.carListCallBack = carListCallBack;
    }

    public void setEditText(EditText editText) {
        this.editText = editText;
    }

    public void setCarDetailList(List<CarDetail> carDetailList) {
        this.carDetailList = carDetailList;
    }

    @AfterViews
    public void initFragment() {

        Bundle bundle = getArguments();
        String activeUserString = bundle.getString("Data");

        if (LogFlag.bLogOn) Log.d(TAG, activeUserString);

        carListAdapter = new CarListAdapter(getActivity(),carDetailList);
        listView.setAdapter(carListAdapter);
        doSearch();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                carListCallBack.onListClick();
                if (LogFlag.bLogOn) Log.d(TAG, "itemClickPosition: "+position);
                listView.clearTextFilter();
                carListAdapter.notifyDataSetChanged();
                gotoCarDetailPage();


            }
        });

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (LogFlag.bLogOn) Log.d(TAG, "scrollState: "+scrollState);
                 carListCallBack.onListScroll(scrollState);

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }



    private void doSearch() {

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                carListAdapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

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


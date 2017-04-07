package com.vpage.vcars.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import com.vpage.vcars.tools.CustomSearchView;
import com.vpage.vcars.tools.VTools;
import com.vpage.vcars.tools.utils.LogFlag;

import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EFragment;
import org.androidannotations.annotations.ViewById;

import java.util.List;


@EFragment(R.layout.fragment_mini)
public class PrimeFragment extends Fragment implements SearchView.OnQueryTextListener {

    private static final String TAG = PrimeFragment.class.getName();

    @ViewById(R.id.listView)
    ListView listView;

    @ViewById(R.id.noDataText)
    TextView noDataText;

    CarListAdapter carListAdapter;

    CarListCallBack carListCallBack;

    MenuItem searchMenuItem;
    CustomSearchView searchView;
    Boolean searchStatus = false;

    List<CarDetail> carDetailList;

    public void setCarListCallBack(CarListCallBack carListCallBack) {
        this.carListCallBack = carListCallBack;
    }

    public void setSearchMenuItem(MenuItem searchMenuItem) {
        this.searchMenuItem = searchMenuItem;
        searchView = (CustomSearchView) MenuItemCompat.getActionView(this.searchMenuItem);
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

        if(searchView!= null){
            searchView.setIconifiedByDefault(false);
            searchView.clearFocus();
            searchView.setOnQueryTextListener(this);
        }



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                carListCallBack.onListClick();
                if (searchStatus) {
                    // to get search Items position when items searched using search view
                    int searchItemPosition = setSearchedItemPosition(carDetailList.get(position).getCarName());
                    Log.d(TAG, "searchItemPosition: " + searchItemPosition);
                }else {
                    if (LogFlag.bLogOn) Log.d(TAG, "itemClickPosition: "+position);
                }

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

    int setSearchedItemPosition(String listItem){

        int searchItemPosition = 0;
        for(int i = 0;i< carDetailList.size();i++){
            if(carDetailList.get(i).getCarName().equals(listItem)){

                searchItemPosition = i;
                Log.d(TAG, "searchItemPosition: " + searchItemPosition);
            }
        }
        return searchItemPosition;
    }



    private void gotoCarDetailPage() {

        Intent intent = new Intent(getActivity(), CarDetailActivity_.class);
        intent.putExtra("SelectedCar","Car Selected");
        startActivity(intent);
        VTools.animation(getActivity());
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d(TAG, "newText: " + newText);
        if(newText.length() > 0){
            searchStatus = true;
        }
        carListAdapter.getFilter().filter(newText.toLowerCase());
        carListAdapter.notifyDataSetChanged();
        return false;
    }
}


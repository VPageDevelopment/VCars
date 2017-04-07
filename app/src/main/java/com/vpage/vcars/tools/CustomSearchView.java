package com.vpage.vcars.tools;

import android.content.Context;
import android.support.v7.widget.SearchView;

public class CustomSearchView extends SearchView {

    OnSearchViewCollapsedEventListener mSearchViewCollapsedEventListener;
    OnSearchViewExpandedEventListener mOnSearchViewExpandedEventListener;

    public CustomSearchView(Context context) {
        super(context);
    }

    @Override
    public void onActionViewCollapsed() {
        if (mSearchViewCollapsedEventListener != null)
            mSearchViewCollapsedEventListener.onSearchViewCollapsed();
        super.onActionViewCollapsed();
    }

    @Override
    public void onActionViewExpanded() {
        if (mOnSearchViewExpandedEventListener != null)
            mOnSearchViewExpandedEventListener.onSearchViewExpanded();
        super.onActionViewExpanded();
    }

    public interface OnSearchViewCollapsedEventListener {
        public void onSearchViewCollapsed();
    }

    public interface OnSearchViewExpandedEventListener {
        public void onSearchViewExpanded();
    }

    public void setOnSearchViewCollapsedEventListener(OnSearchViewCollapsedEventListener eventListener) {
        mSearchViewCollapsedEventListener = eventListener;
    }

    public void setOnSearchViewExpandedEventListener(OnSearchViewExpandedEventListener eventListener) {
        mOnSearchViewExpandedEventListener = eventListener;
    }

}

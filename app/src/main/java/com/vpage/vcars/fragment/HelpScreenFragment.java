package com.vpage.vcars.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import com.vpage.vcars.R;
import com.vpage.vcars.tools.CallBackToHelpScreenListener;
import com.vpage.vcars.tools.utils.LogFlag;


public final class HelpScreenFragment extends Fragment {
    private static final String KEY_CONTENT = "HelpScreenFragment:Content";
    String TAG = HelpScreenFragment.class.getName();

    int imageSource = 0;
    String content;

    CallBackToHelpScreenListener callBackToHelpScreenListener;

    public HelpScreenFragment(){}
	
	@SuppressLint("ValidFragment")
    public HelpScreenFragment(int imageSource, String content, CallBackToHelpScreenListener callBackToHelpScreenListener) {
		this.imageSource = imageSource;
        this.content =content;
        this.callBackToHelpScreenListener =callBackToHelpScreenListener;


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if ((savedInstanceState != null) && savedInstanceState.containsKey(KEY_CONTENT)) {
        	imageSource = savedInstanceState.getInt(KEY_CONTENT);
            content = savedInstanceState.getString(KEY_CONTENT);
            if (LogFlag.bLogOn)Log.d(TAG, String.valueOf(imageSource));
            if (LogFlag.bLogOn)Log.d(TAG, content);

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;
    try {
        view = inflater.inflate(R.layout.fragment_helpscreen, container, false);
        ImageView image = (ImageView)view.findViewById(R.id.slider_image);
        if(imageSource != 0){
            Picasso.with(view.getContext()).load(imageSource).into(image);
        }else {
            image.setImageResource(R.drawable.slider1);
        }
        FrameLayout frameLayout = (FrameLayout)view.findViewById(R.id.frameLayout);
        frameLayout.getBackground().setAlpha(150);

        TextView textView = (TextView)view.findViewById(R.id.pagerText);
        textView.setText(content);

        Button button  = (Button)view.findViewById(R.id.button);

        button.setText("SKIP");

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callBackToHelpScreenListener.onButtonClick();
            }
        });


    }catch (InflateException e){
        if (LogFlag.bLogOn)Log.e(TAG, e.getMessage());
    }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_CONTENT, imageSource);
        outState.putString(KEY_CONTENT, content);
    }

}

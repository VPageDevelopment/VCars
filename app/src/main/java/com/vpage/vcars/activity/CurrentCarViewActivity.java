package com.vpage.vcars.activity;

import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.widget.TextView;
import com.vpage.vcars.R;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;

@WindowFeature({Window.FEATURE_NO_TITLE, Window.FEATURE_ACTION_BAR_OVERLAY})
@EActivity(R.layout.activity_currentcarview)
@Fullscreen
public class CurrentCarViewActivity extends AppCompatActivity {

    private static final String TAG = CurrentCarViewActivity.class.getName();


    @ViewById(R.id.messageText)
    TextView messageText;

    @AfterViews
    public void onInitView() {

        setView();
    }

    void setView(){


    }

}

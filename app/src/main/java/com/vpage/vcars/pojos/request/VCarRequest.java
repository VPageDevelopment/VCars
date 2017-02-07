package com.vpage.vcars.pojos.request;


import com.vpage.vcars.R;
import com.vpage.vcars.tools.VCarsApplication;

public class VCarRequest {

    private String appVersion;

    public String getAppVersion() {
        return VCarsApplication.getContext().getResources().getString(R.string.app_version);
    }


}

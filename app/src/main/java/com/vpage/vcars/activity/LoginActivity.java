package com.vpage.vcars.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;
import android.util.Base64;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vpage.vcars.R;
import com.vpage.vcars.httputils.VCarRestClient;
import com.vpage.vcars.pojos.FacebookUserprofile;
import com.vpage.vcars.pojos.VLocation;
import com.vpage.vcars.pojos.ValidationStatus;
import com.vpage.vcars.pojos.request.CheckUserRequest;
import com.vpage.vcars.pojos.request.DeviceInfoData;
import com.vpage.vcars.pojos.request.SignInRequest;
import com.vpage.vcars.pojos.request.SignupRequest;
import com.vpage.vcars.pojos.response.CheckUserResponse;
import com.vpage.vcars.pojos.response.SignInResponse;
import com.vpage.vcars.pojos.response.SignupResponse;
import com.vpage.vcars.tools.LoginType;
import com.vpage.vcars.tools.NetworkUtil;
import com.vpage.vcars.tools.VCarAnalyticsTools;
import com.vpage.vcars.tools.VCarGooglePlusTools;
import com.vpage.vcars.tools.VCarRestTools;
import com.vpage.vcars.tools.VCarsApplication;
import com.vpage.vcars.tools.VPreferences;
import com.vpage.vcars.tools.VTools;
import com.vpage.vcars.tools.utils.AlertUtils;
import com.vpage.vcars.tools.utils.CommonUtils;
import com.vpage.vcars.tools.utils.LogFlag;
import com.vpage.vcars.tools.utils.StringUtils;
import com.vpage.vcars.tools.utils.ValidationUtils;
import com.vpage.vcars.view.PlayGifView;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Bean;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FocusChange;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.InstanceState;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

@Fullscreen
@EActivity(R.layout.activity_signin)
public class LoginActivity extends AppCompatActivity implements View.OnKeyListener, GoogleApiClient.OnConnectionFailedListener,GoogleApiClient.ConnectionCallbacks, View.OnClickListener {

    private static final String TAG = LoginActivity.class.getName();

    @ViewById(R.id.loginButton)
    Button loginButton;

    @ViewById(R.id.gLoginButton)
    Button gLoginButton;
/*
    @ViewById(R.id.signUpButton)
    Button signUpButton;*/

    @ViewById(R.id.referText)
    TextView referText;

    @ViewById(R.id.andText)
    TextView andText;

    @ViewById(R.id.labsText)
    TextView labsText;

    @ViewById(R.id.termsText)
    TextView termsText;

    @ViewById(R.id.privacyText)
    TextView privacyText;

    @ViewById(R.id.homeLayout)
    RelativeLayout homeLayout;

    @ViewById(R.id.loging_layout)
    LinearLayout loging_layout;

    @ViewById(R.id.homestudyTextview)
    TextView homeStudyText;

    @ViewById(R.id.homerankTextview)
    TextView homeRankText;

    @ViewById(R.id.fbLoginButton)
    Button fbLoginButton;

    @ViewById(R.id.appImage)
    ImageView appImage;

    @Bean
    VCarAnalyticsTools vCarAnalyticsTools;

    @Bean
    VCarGooglePlusTools vCarGooglePlusTools;

    GoogleSignInAccount googleSignInAccount;

    // [START resolution_variables]
    /* Is there a ConnectionResult resolution in progress? */
    @InstanceState
    boolean mIsResolving = false;

    /* Should we automatically resolve ConnectionResults when possible? */
    @InstanceState
    boolean mShouldResolve = false;
    // [END resolution_variables]

    /* RequestCode for resolutions involving sign-in */
    private static final int RC_SIGN_IN = 1;

    /* RequestCode for resolutions to get GET_ACCOUNTS permission on M */
    private static final int RC_PERM_GET_ACCOUNTS = 2;

    private static final int RC_PERM_GET_LOCATION = 3;

    /* Client used to interact with Google APIs. */
    private GoogleApiClient mGoogleApiClient;

    private String countryCode = "";

    EditText txtuserName, txtpassWord,userPhoneNumberTxt,userPhoneNumber,userOTP;

    TextView txtError;

    SignInRequest signInRequest;

    SignInResponse signInResponse;

    CheckUserRequest checkUserRequest;

    CheckUserResponse checkUserResponse;

    SignupRequest registerRequest;

    String googleEmail;

    private PopupWindow loginPopUp;

    String userNameInput, passWordInput,userPhoneNumberInput,generatedOTP,userOTPInput;

    Button btnSignIn,btnOTPGenerate,btnOTPSignIn;

    ValidationStatus validationStatus;

    CallbackManager callbackManager;

    AccessToken accessToken;

    boolean isNetworkAvailable = false;
    Gson gson;

    Profile facebookProfile;
    FacebookUserprofile facebookUserprofile;

    PlayGifView loaderGif, loaderGifinPopUp;


    VLocation vLocation;
    TextView txtHeading;
    TextView txtSubHeading;


    @AfterViews
    public void initView() {
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        generateKeyHash();
        try {
            homeStudyText.setTypeface(VTools.getAvenirLTStdHeavy());
            homeRankText.setTypeface(VTools.getAvenirLTStdHeavy());
            loaderGif = (PlayGifView) findViewById(R.id.viewGif);
            loaderGif.setImageResource(R.drawable.loader_gif);
            loaderGif.setVisibility(View.GONE);
            Intent CallingIntent = getIntent();
            final Animation login_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.anim_login);
            loging_layout.setVisibility(View.VISIBLE);
            loging_layout.startAnimation(login_anim);

            final Animation appImage_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.splashanim);
            appImage.setVisibility(View.VISIBLE);
            appImage.startAnimation(appImage_anim);

            final Animation homeStudy_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_left);
            homeStudyText.setVisibility(View.VISIBLE);
            homeStudyText.startAnimation(homeStudy_anim);

            final Animation homeRank_anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_in_right);
            homeRankText.setVisibility(View.VISIBLE);
            homeRankText.startAnimation(homeRank_anim);

            getCountryCode();
            if (null != termsText) {
                StringUtils.makeTextViewHyperlink(termsText);
            }

            if (null != privacyText) {
                StringUtils.makeTextViewHyperlink(privacyText);
            }

            callbackManager = CallbackManager.Factory.create();

            GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                    .requestEmail()
                    .build();

            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, this)
                    .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();

            vCarGooglePlusTools.setmGoogleApiClient(mGoogleApiClient);

            setTypeFace();

            fbLoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    vCarAnalyticsTools.reportEvents(LoginActivity.this, getString(R.string.event_LoginScreen), getString(R.string.action_FB), "Fb User");
                    loaderGif.setVisibility(View.VISIBLE);
                    LoginManager.getInstance().logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email"));
                }
            });


            LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(final LoginResult loginResult) {
                    // App code
                    if (LogFlag.bLogOn)Log.d(TAG, "Facebook login Success");
                    if (null != loginResult.getAccessToken().getToken()) {
                        if (LogFlag.bLogOn)Log.d(TAG, loginResult.getAccessToken().getToken());
                    }
                    GraphRequest request = GraphRequest.newMeRequest(
                            loginResult.getAccessToken(),
                            new GraphRequest.GraphJSONObjectCallback() {
                                @Override
                                public void onCompleted(JSONObject object, GraphResponse response) {
                                    // Application code
                                    if ((response != null) && (response.getJSONObject() != null)) {
                                        if (LogFlag.bLogOn)Log.d(TAG, response.toString());
                                        String profileImg = "https://graph.facebook.com/" + loginResult.getAccessToken().getUserId() + "/picture?type=large&width=50";
                                        if (LogFlag.bLogOn)Log.d(TAG, "Facebook profileImg -->" + profileImg);
                                        JSONObject fbObject = response.getJSONObject();
                                        if (LogFlag.bLogOn)Log.d(TAG, "Facebook -->" + fbObject.toString());
                                        facebookUserprofile = VCarRestTools.getInstance().getFacebookUserProfile(fbObject.toString());
                                        facebookUserprofile.setFacebookImage(profileImg);
                                        facebookUserExists();
                                    } else {
                                        loaderGif.setVisibility(View.GONE);
                                        if (LogFlag.bLogOn)Log.e(TAG, "Facebook Error -->" + response.getError());
                                        VTools.showToast("Network Error");
                                    }
                                }
                            });
                    Bundle parameters = new Bundle();
                    parameters.putString("fields", "id, token_for_business,first_name, last_name,name,email,gender, birthday");
                    request.setParameters(parameters);
                    request.executeAsync();
                }

                @Override
                public void onCancel() {
                    loaderGif.setVisibility(View.GONE);
                    if (LogFlag.bLogOn)Log.d(TAG, "Facebook login cancelled");
                }

                @Override
                public void onError(FacebookException exception) {
                    loaderGif.setVisibility(View.GONE);
                    VTools.getInstance().showToast(exception.toString());
                    if (LogFlag.bLogOn)Log.e(TAG, "Facebook login error");
                }
            });

            AccessTokenTracker accessTokenTracker = new AccessTokenTracker() {
                @Override
                protected void onCurrentAccessTokenChanged(
                        AccessToken oldAccessToken,
                        AccessToken currentAccessToken) {
                    // Set the access token using
                    // currentAccessToken when it's loaded or set
                    Log.d(TAG, "FB token tracker");
                    accessToken = currentAccessToken;
                    if (accessToken != null) {
                        Log.d(TAG, currentAccessToken.getToken());
                    }

                }
            };
            // If the access token is available already assign it.
            accessToken = AccessToken.getCurrentAccessToken();

            ProfileTracker profileTracker = new ProfileTracker() {
                @Override
                protected void onCurrentProfileChanged(
                        Profile oldProfile,
                        Profile currentProfile) {
                    // App code
                    facebookProfile = currentProfile;
                    if (facebookProfile != null) {
                        if (LogFlag.bLogOn)Log.d(TAG, "FB profile tracker");
                        if (LogFlag.bLogOn)Log.d(TAG, facebookProfile.getId());
                        if (LogFlag.bLogOn)Log.d(TAG, facebookProfile.getFirstName());
                        if (LogFlag.bLogOn)Log.d(TAG, facebookProfile.getLastName());
                        if (LogFlag.bLogOn)Log.d(TAG, "FB profile: "+facebookProfile.toString());
                    }

                }
            };
        } catch (Exception e) {
            if (LogFlag.bLogOn)Log.e(TAG, e.getMessage());
        }
    }

    @Click(R.id.loginButton)
    public void onLoginClick(View v) {
        networkStatus();
        if (!isNetworkAvailable) {
            return;
        }
        goOTPPopup();
    }


    @Click(R.id.gLoginButton)
    public void gLoginClick(View v) {
        networkStatus();
        if (!isNetworkAvailable) {
            return;
        }

        goGoogleLogin();
    }

    @FocusChange({R.id.userName, R.id.passWord,R.id.userPhoneNumberTxt,R.id.userOTP,R.id.userPhoneNumber})
    public void focusChangedOnUser(View v, boolean hasFocus) {
        if (hasFocus) {
            txtError.setVisibility(View.GONE);
        } else {

        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {

        if (keyCode == EditorInfo.IME_ACTION_GO ||
                keyCode == EditorInfo.IME_ACTION_DONE ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {

            switch (v.getId()){
                case R.id.passWord:
                    userNameInput = txtuserName.getText().toString();
                    passWordInput = txtpassWord.getText().toString();

                    validationStatus = ValidationUtils.isValidLoginUserNamePassword(userNameInput, passWordInput);

                    if (validationStatus.isStatus() == false) {
                        loaderGifinPopUp.setVisibility(View.GONE);
                        setErrorMessage(validationStatus.getMessage());
                    } else {
                        loaderGifinPopUp.setVisibility(View.VISIBLE);
                        txtError.setVisibility(View.GONE);
                        // vCarLogin();

                        vcarUserExists(); // TO be Used
                    }
                    break;

                case R.id.userOTP:
                    userPhoneNumberInput = userPhoneNumber.getText().toString();
                    userOTPInput = userOTP.getText().toString();

                    validationStatus = ValidationUtils.isValidUserPhoneNumber(userPhoneNumberInput);

                    if (validationStatus.isStatus() == false) {
                        loaderGifinPopUp.setVisibility(View.GONE);
                        btnOTPSignIn.setEnabled(true);
                        setErrorMessage(validationStatus.getMessage());
                    } else {

                        // for testing only, placed static data
                        generatedOTP = "9999";

                        if(userOTPInput.equals(generatedOTP)){


                            btnOTPSignIn.setEnabled(false);
                            loaderGifinPopUp.setVisibility(View.VISIBLE);
                            txtError.setVisibility(View.GONE);
                            //  vCarLogin();

                            //  vcarUserExists(); // TO be Used

                            goToSignUpPage(); // For testing alone

                        }else {
                            if (LogFlag.bLogOn) Log.d(TAG,"Invalid OTP ");
                            loaderGifinPopUp.setVisibility(View.GONE);
                            btnOTPSignIn.setEnabled(true);
                            setErrorMessage("Invalid OTP ");
                        }
                    }
                    break;

                case R.id.userPhoneNumberTxt:
                    if (LogFlag.bLogOn)Log.d(TAG, " userPhoneNumber GO Clicked :");
                    userPhoneNumberInput = userPhoneNumberTxt.getText().toString();

                    validationStatus = ValidationUtils.isValidUserPhoneNumber(userPhoneNumberInput);

                    if (validationStatus.isStatus() == false) {
                        loaderGifinPopUp.setVisibility(View.GONE);
                        setErrorMessage(validationStatus.getMessage());
                    } else {
                        loaderGifinPopUp.setVisibility(View.VISIBLE);
                        txtError.setVisibility(View.GONE);
                        //  goLoginPopupWithUserName();

                        generatedOTPProcessFinish();
                    }
                    break;
            }

        }


        return false;
    }

    @Click({R.id.termsText, R.id.privacyText})
    public void onTextClick(View v) {
        switch (v.getId()) {

            case R.id.termsText:
                //To Do
                break;

            case R.id.privacyText:
                //To Do
                break;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnSignIn:
                btnSignIn.setEnabled(false);

                networkStatus();
                if (!isNetworkAvailable) {
                    btnSignIn.setEnabled(true);
                    return;
                }

                userNameInput = txtuserName.getText().toString();
                passWordInput = txtpassWord.getText().toString();
                validationStatus = ValidationUtils.isValidLoginUserNamePassword(userNameInput, passWordInput);

                if (validationStatus.isStatus() == false) {
                    loaderGifinPopUp.setVisibility(View.GONE);
                    btnSignIn.setEnabled(true);
                    setErrorMessage(validationStatus.getMessage());
                } else {
                    btnSignIn.setEnabled(false);
                    loaderGifinPopUp.setVisibility(View.VISIBLE);
                    txtError.setVisibility(View.GONE);
                    //  vCarLogin();

                    vcarUserExists(); // TO be Used



                }
                break;

            case R.id.btnOTPSignIn:
                btnOTPSignIn.setEnabled(false);

                networkStatus();
                if (!isNetworkAvailable) {
                    btnOTPSignIn.setEnabled(true);
                    return;
                }

                userPhoneNumberInput = userPhoneNumber.getText().toString();
                userOTPInput = userOTP.getText().toString();
                validationStatus = ValidationUtils.isValidUserPhoneNumber(userPhoneNumberInput);

                if (validationStatus.isStatus() == false) {
                    loaderGifinPopUp.setVisibility(View.GONE);
                    btnOTPSignIn.setEnabled(true);
                    setErrorMessage(validationStatus.getMessage());
                } else {

                    // for testing only, placed static data
                    generatedOTP = "9999";

                    if(userOTPInput.equals(generatedOTP)){


                        btnOTPSignIn.setEnabled(false);
                        loaderGifinPopUp.setVisibility(View.VISIBLE);
                        txtError.setVisibility(View.GONE);
                        //  vCarLogin();

                        //  vcarUserExists(); // TO be Used

                        goToSignUpPage(); // For testing alone

                    }else {
                        if (LogFlag.bLogOn) Log.d(TAG,"Invalid OTP ");
                        loaderGifinPopUp.setVisibility(View.GONE);
                        btnOTPSignIn.setEnabled(true);
                        setErrorMessage("Invalid OTP ");
                    }
                }
                break;

            case R.id.btnOTPGenerate:
                if (LogFlag.bLogOn)Log.d(TAG, " btnOTPGenerate Clicked :");

                btnOTPGenerate.setEnabled(false);

                networkStatus();
                if (!isNetworkAvailable) {
                    btnOTPGenerate.setEnabled(true);
                    return;
                }

                userPhoneNumberInput = userPhoneNumberTxt.getText().toString();

                validationStatus = ValidationUtils.isValidUserPhoneNumber(userPhoneNumberInput);

                if (validationStatus.isStatus() == false) {
                    loaderGifinPopUp.setVisibility(View.GONE);
                    btnOTPGenerate.setEnabled(true);
                    setErrorMessage(validationStatus.getMessage());
                } else {
                    btnOTPGenerate.setEnabled(false);
                    loaderGifinPopUp.setVisibility(View.VISIBLE);
                    txtError.setVisibility(View.GONE);
                    //  goLoginPopupWithUserName();

                    generatedOTPProcessFinish();
                }
                break;
            case R.id.btnClose:
                loaderGifinPopUp.setVisibility(View.GONE);
                loginPopUp.dismiss();

                break;

        }

    }

    private void goOTPPopup() {
        final View popUpView = getLayoutInflater().inflate(R.layout.popup_otp, null); // inflating popup layout
        loginPopUp = VTools.createPopUp(popUpView);
        btnOTPGenerate = (Button) popUpView.findViewById(R.id.btnOTPGenerate);
        final ImageButton btnClose = (ImageButton) popUpView.findViewById(R.id.btnClose);
        loaderGifinPopUp = (PlayGifView) popUpView.findViewById(R.id.viewGif);
        loaderGifinPopUp.setImageResource(R.drawable.loader_gif);
        loaderGifinPopUp.setVisibility(popUpView.getRootView().GONE);
        txtHeading = (TextView) popUpView.findViewById(R.id.headingTxt);
        txtSubHeading = (TextView) popUpView.findViewById(R.id.subheadingTxt);
        userPhoneNumberTxt = (EditText) popUpView.findViewById(R.id.userPhoneNumberTxt);
        txtError = (TextView) popUpView.findViewById(R.id.errorText);
        userPhoneNumberTxt.setOnKeyListener(this);

        userPhoneNumberTxt.setTypeface(VTools.getAvenirLTStdMedium());
        txtHeading.setTypeface(VTools.getAvenirLTStdMedium());
        txtSubHeading.setTypeface(VTools.getAvenirLTStdMedium());
        btnOTPGenerate.setEnabled(true);

        btnOTPGenerate.setOnClickListener(this);
        btnClose.setOnClickListener(this);
    }


    private void goLoginPopupWithPhoneNumber() {

        final View popUpView = getLayoutInflater().inflate(R.layout.popup_signinwithphonenumber, null); // inflating popup layout
        loginPopUp = VTools.createPopUp(popUpView);
        btnOTPSignIn = (Button) popUpView.findViewById(R.id.btnOTPSignIn);
        final ImageButton btnClose = (ImageButton) popUpView.findViewById(R.id.btnClose);
        loaderGifinPopUp = (PlayGifView) popUpView.findViewById(R.id.viewGif);
        loaderGifinPopUp.setImageResource(R.drawable.loader_gif);
        loaderGifinPopUp.setVisibility(popUpView.getRootView().GONE);
        txtHeading = (TextView) popUpView.findViewById(R.id.headingTxt);
        txtSubHeading = (TextView) popUpView.findViewById(R.id.subheadingTxt);
        userPhoneNumber = (EditText) popUpView.findViewById(R.id.userPhoneNumber);
        userOTP = (EditText) popUpView.findViewById(R.id.userOTP);
        txtError = (TextView) popUpView.findViewById(R.id.errorText);
        userOTP.setOnKeyListener(this);
        userPhoneNumber.setTypeface(VTools.getAvenirLTStdMedium());
        userOTP.setTypeface(VTools.getAvenirLTStdMedium());
        txtHeading.setTypeface(VTools.getAvenirLTStdMedium());
        txtSubHeading.setTypeface(VTools.getAvenirLTStdMedium());
        btnOTPSignIn.setEnabled(true);
        userPhoneNumber.setText(userPhoneNumberInput);

        btnOTPSignIn.setOnClickListener(this);
        btnClose.setOnClickListener(this);
    }

    void setErrorMessage(String errorMessage) {
        txtError.setVisibility(View.VISIBLE);
        txtError.setText(errorMessage);
    }

    private void goToSignUpPage() {
        Intent intent = new Intent(getApplicationContext(), SignupActivity_.class);
        intent.putExtra("UserPhoneNumber", userPhoneNumberInput);
        LoginActivity.this.finish();
        startActivity(intent);

    }

    class TimeOutTask extends TimerTask {
        boolean isTimedOut = false;

        public void run() {
            isTimedOut = true;
        }
    }

    @Background
    void generatedOTPProcessFinish() {


        // Server generates an OTP and waits for client to send this
        Random r = new Random();
        String otp = new String();
        for(int i=0 ; i < 8 ; i++) {
            otp += r.nextInt(10);
        }

        // Server starts a timer of 10 seconds during which the OTP is valid.
        TimeOutTask task = new TimeOutTask();
        Timer t = new Timer();
        t.schedule(task, 100000L);

        if(task.isTimedOut) {
            // User took more than 100 seconds and hence the OTP is invalid
            if (LogFlag.bLogOn)Log.d(TAG, "PASSWORD: " + "Time out!");
        } else {
            if (LogFlag.bLogOn)Log.d(TAG,"OTP GENERATED ");
            if (LogFlag.bLogOn)Log.d(TAG,"otp: "+otp);
            generatedOTP = otp;
            //requestSmsPermission();    //to be used for developing
            callGoLoginPopup(); //for testing only
        }
    }

    @UiThread
    public void callGoLoginPopup() {

        loaderGifinPopUp.setVisibility(View.GONE);
        loginPopUp.dismiss();
        goLoginPopupWithPhoneNumber();

    }


    @Background
    public void vcarUserExists() {
        if (LogFlag.bLogOn)Log.d(TAG, "vcarUserExists");

        setCheckVCarUserRequestData();

        VCarRestClient vCarRestClient = new VCarRestClient();
        checkUserResponse = vCarRestClient.checkStudent(checkUserRequest);
        if(checkUserResponse != null) {
            if (LogFlag.bLogOn)Log.d(TAG, checkUserResponse.toString());
            checkVCarUserProcessFinish();
        }

    }


    public void setCheckVCarUserRequestData() {
        if (LogFlag.bLogOn)Log.d(TAG, "setCheckVCarUserRequestData");
        String uuid = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        checkUserRequest = new CheckUserRequest();
        checkUserRequest.setUserID(userPhoneNumberInput);
        checkUserRequest.setDeviceIdentity(uuid);
        checkUserRequest.setDevicePlatformName("Android");
        checkUserRequest.setLoginType(LoginType.VCars.name());
        checkUserRequest.setDeviceToken(VPreferences.get("gcmToken"));
        if (LogFlag.bLogOn)Log.d(TAG, checkUserRequest.toString());
    }


    @UiThread
    public void checkVCarUserProcessFinish() {
        if (LogFlag.bLogOn)Log.d(TAG, "checkVCarUserProcessFinish");
        if (LogFlag.bLogOn)Log.d(TAG, checkUserResponse.toString());
        if (checkUserResponse.getExists().equalsIgnoreCase("false")) {
            // register
            if (LogFlag.bLogOn)Log.d(TAG, " user not found. need to register");
            goToSignUpPage();

        } else {
            // TODO Login
            if (LogFlag.bLogOn)Log.d(TAG, " user  found. show error");
            if (LogFlag.bLogOn)Log.d(TAG, checkUserResponse.getError());
            //setErrorMessage("Phone no has already been registered");

            vCarLogin();

        }
    }

    @Background
    void vCarLogin() {
        Log.d(TAG, "vCarLogin");
        setSignInRequestData();

        VCarRestClient vCarRestClient = new VCarRestClient();
        vCarRestClient.setSignInParams(signInRequest);
        signInResponse = vCarRestClient.signIn();
        if (signInResponse != null) {
            signInResponse.setLoginType(LoginType.VCars.name());
            logInProcessFinish();
        } else {
            stopLoader();
        }
    }

    @UiThread
    public void stopLoader() {
        try {
            loaderGifinPopUp.setVisibility(View.GONE);
        } catch (Exception e) {
            if (LogFlag.bLogOn)Log.e(TAG, "Error while stopping loader", e);
        }
    }

    void setSignInRequestData() {
        if (LogFlag.bLogOn)Log.d(TAG, "setSignInRequestData");

        String uuid = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        if (LogFlag.bLogOn)Log.d(TAG, "uuid: " + uuid);

        String encryptedPassword = null;
        try {
            encryptedPassword = CommonUtils.getMD5EncodedString(passWordInput);
        } catch (NoSuchAlgorithmException e) {
            if (LogFlag.bLogOn)Log.e(TAG, e.getMessage());
        }
        Log.d(TAG, "encryptedPassword: " + encryptedPassword);

        signInRequest = new SignInRequest();
        //signInRequest.setDevicePlatformName("Android");
        signInRequest.setDeviceIdentity(uuid);
        signInRequest.setDeviceToken(VPreferences.get("gcmToken"));
        signInRequest.setUserId(userNameInput);
        signInRequest.setPassword(encryptedPassword);
        signInRequest.setClientEncryptedPassword(true);
        signInRequest.setDeviceInfoData(getDeviceInfo());
        signInRequest.setCountry(getVcarCountryCode());
    }

    @UiThread
    public void logInProcessFinish() {
        try {
            if (LogFlag.bLogOn)Log.d(TAG, "logInProcessFinish");
            if (null != signInResponse) {
                if (LogFlag.bLogOn)Log.d(TAG, signInResponse.toString());
            }
            if (null == signInResponse) {
                loginPopUp.dismiss();
                AlertUtils.getInstance().setTitle("VCars");
                AlertUtils.getInstance().setMessage(getString(R.string.error_unable_to_login));
                AlertUtils.getInstance().showAlert(LoginActivity.this);
                return;
            }
            if (signInResponse.isSuccess()) {
                if (LogFlag.bLogOn)Log.d(TAG, "Login Success");

                goToHome();

            } else {
                stopLoader();
                if (LogFlag.bLogOn)Log.d(TAG, "error " + signInResponse.getError());
                setErrorMessage(signInResponse.getError());
            }
        } catch (Exception e) {
            if (LogFlag.bLogOn)Log.e(TAG, "Error loginFinish", e);

        }

    }


    public void networkStatus() {
        String status = NetworkUtil.getConnectivityStatusString(this);
        if (LogFlag.bLogOn)Log.d(TAG, "Network Availability");
        if (LogFlag.bLogOn)Log.d(TAG, status);


        switch (status) {
            case "Connected to Internet with Mobile Data":
                isNetworkAvailable = true;
                break;
            case "Connected to Internet with WIFI":
                isNetworkAvailable = true;
                break;
            default:
                VTools.getInstance().showToast(status);
                isNetworkAvailable = false;
                break;
        }
    }

    void generateKeyHash(){
        try {

            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures)
            {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                if (LogFlag.bLogOn)Log.d(TAG,"KeyHash: "+ Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }

        } catch (NoSuchAlgorithmException e) {
            if (LogFlag.bLogOn)Log.e(TAG, e.getMessage());
        } catch (PackageManager.NameNotFoundException e) {
            if (LogFlag.bLogOn)Log.e(TAG, e.getMessage());
        }
    }


    //get country code if have permission else ask
    private void getCountryCode() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getCountryCodetxt();
            getBestLocation();
        }else{
            ActivityCompat.requestPermissions(LoginActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, RC_PERM_GET_LOCATION);
        }
    }

    private void getCountryCodetxt(){
        try {
            TelephonyManager teleMgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
            if (teleMgr != null) {
                if (teleMgr.getPhoneType() == TelephonyManager.PHONE_TYPE_GSM) {
                    final GsmCellLocation location = (GsmCellLocation) teleMgr.getCellLocation();
                    if (location != null) {
                        String LAC = Integer.toString(location.getLac());
                        String CID = Integer.toString(location.getCid());
                        if (LogFlag.bLogOn)Log.d(TAG, "GsmCellLocation " + LAC);
                        if (LogFlag.bLogOn)Log.d(TAG, "GsmCellLocation " + CID);
                    }
                }
                countryCode = teleMgr.getSimCountryIso();
            }
            VPreferences.save("countryCode", countryCode);
            if (LogFlag.bLogOn)Log.d(TAG, "Country : " + countryCode);
        } catch (Exception e) {
            if (LogFlag.bLogOn)Log.e(TAG,"Error in getting countryCode "+e.getMessage());
        }
    }

    //====================================================================================================
//code to get current location of user
    private void getBestLocation() {
        Location gpslocation = getLocationByProvider(LocationManager.GPS_PROVIDER);
        Location networkLocation = getLocationByProvider(LocationManager.NETWORK_PROVIDER);
        double latitude = 0;
        double longitude = 0;
        if (gpslocation != null) {
            latitude = gpslocation.getLatitude();
            longitude = gpslocation.getLongitude();
        }
        if(networkLocation != null) {
            if (latitude == 0 || longitude == 0) {
                latitude = networkLocation.getLatitude();
                longitude = networkLocation.getLongitude();
            }
        }
        Geocoder geocoder = new Geocoder(VCarsApplication.getContext());
        VLocation vLocation=new VLocation();
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if(addresses!=null) {
                for (Address address : addresses) {
                    if (LogFlag.bLogOn)Log.d(TAG, address.toString());
                    vLocation.setLocation(address.getLocality());
                    vLocation.setLatitude(latitude);
                    vLocation.setLongitude(longitude);
                    vLocation.setState(address.getAdminArea());
                    vLocation.setCounty(address.getSubAdminArea());
                    vLocation.setPostalCode(address.getPostalCode());
                    vLocation.setCountryCode(address.getCountryCode());
                    vLocation.setCountryName(address.getCountryName());
                    if (LogFlag.bLogOn)Log.d(TAG, vLocation.toString());
                    Gson gson = new GsonBuilder().create();
                    VPreferences.save("CurrentLocation", gson.toJson(vLocation));
                }
            }
        }catch (Exception e){
            if (LogFlag.bLogOn)Log.e(TAG,"Error while getting user location "+e.getMessage());
        }
    }

    private Location getLocationByProvider(String provider) {
        Location location = null;
        LocationManager locationManager = (LocationManager) getApplicationContext().getSystemService(VCarsApplication.getContext().LOCATION_SERVICE);
        try {
            if (locationManager.isProviderEnabled(provider)) {
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    location = locationManager.getLastKnownLocation(provider);
                }
            }
        } catch (IllegalArgumentException e) {
            if (LogFlag.bLogOn)Log.e(TAG, "Cannot access Provider " + e.getMessage());
        }
        return location;
    }

    private void setTypeFace() {
        loginButton.setTypeface(VTools.getAvenirLTStdHeavy());
        fbLoginButton.setTypeface(VTools.getAvenirLTStdHeavy());
        gLoginButton.setTypeface(VTools.getAvenirLTStdHeavy());
        //  signUpButton.setTypeface(VTools.getAvenirLTStdHeavy());
        referText.setTypeface(VTools.getAvenirLTStdMedium());
        privacyText.setTypeface(VTools.getAvenirLTStdMedium());
        termsText.setTypeface(VTools.getAvenirLTStdMedium());
        andText.setTypeface(VTools.getAvenirLTStdMedium());
        labsText.setTypeface(VTools.getAvenirLTStdMedium());
    }

    @Background
    public void facebookUserExists() {
        try {
            if (LogFlag.bLogOn)Log.d(TAG, "facebookUserExists");

            setCheckFacebookUserRequestData();

            VCarRestClient oustRestClient = new VCarRestClient();
            checkUserResponse = oustRestClient.checkStudent(checkUserRequest);
            if (checkUserResponse != null) {
                if (LogFlag.bLogOn)Log.d(TAG, checkUserResponse.toString());
                checkFacebookStudentProcessFinish();
            } else {
                hideLoderGifImage();
            }
        }catch (Exception e){
            if (LogFlag.bLogOn)Log.e(TAG,e.getMessage());
            hideLoderGifImage();
            showToastErrorMsg("Facebook Login Success!!! waiting for rest call");
            goToHome();
        }
    }

    public void setCheckFacebookUserRequestData() {
        if (LogFlag.bLogOn)Log.d(TAG, "setCheckFacebookUserRequestData");
        String uuid = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        checkUserRequest = new CheckUserRequest();
        checkUserRequest.setUserID(facebookUserprofile.getToken_for_business());
        checkUserRequest.setEmail(facebookUserprofile.getEmail());
        checkUserRequest.setDeviceIdentity(uuid);
        checkUserRequest.setDevicePlatformName("Android");
        checkUserRequest.setLoginType("FacebookApp");
        checkUserRequest.setCountry(getVcarCountryCode());
        checkUserRequest.setFbTokenId(facebookUserprofile.getToken_for_business());
        checkUserRequest.setDeviceToken(VPreferences.get("gcmToken"));
        checkUserRequest.setProfilePic(facebookUserprofile.getFacebookImage());
        if (LogFlag.bLogOn)Log.d(TAG, checkUserRequest.toString());
    }
    private String getVcarCountryCode() {
        if (countryCode == null || countryCode.length() == 0) {
            String sLocation = VPreferences.get("CurrentLocation");
            vLocation = VTools.getInstance().getLocationData(sLocation);
            if (vLocation != null) {
                countryCode = vLocation.getCountryCode();
            }
        }
        return countryCode;
    }

    @UiThread
    public void hideLoderGifImage(){
        loaderGif.setVisibility(View.GONE);
    }

    @UiThread
    public void showToastErrorMsg(String error) {
        VTools.showToast(error);
    }


    private void goToHome() {
        try {
            boolean isAppInstalled = VPreferences.getAppInstallStatus("isInstalled");

            if (LogFlag.bLogOn)Log.d(TAG, signInResponse.toString());
            gson = new GsonBuilder().create();
            // Keep the login
            VPreferences.save("userdata", gson.toJson(VTools.getInstance().getActiveUser(signInResponse)));
            VPreferences.save("isLoggedIn", "true");
            VPreferences.save("loginType", signInResponse.getLoginType());
            Intent intent;
            if ((isAppInstalled)) {
                intent = new Intent(getApplicationContext(), HomeActivity_.class);
            } else {
                VPreferences.saveAppInstallStatus("isInstalled",true);
                intent = new Intent(getApplicationContext(), HelpScreenActivity_.class);
            }
            intent.putExtra("ActiveUser", gson.toJson(VTools.getInstance().getActiveUser(signInResponse)));
            startActivity(intent);
            VTools.animation(this);
            finish();
        } catch (Exception e) {
            if (LogFlag.bLogOn)Log.e(TAG,  e.getMessage());
        }
    }

    @UiThread
    public void checkFacebookStudentProcessFinish() {
        if (LogFlag.bLogOn)Log.d(TAG, "checkFacebookUserProcessFinish");
        if (LogFlag.bLogOn)Log.d(TAG, checkUserResponse.toString());

        if (checkUserResponse.getExists().equalsIgnoreCase("false")) {
            // register
            Log.d(TAG, "Facebook user not found. need to register");
            facebookSocialRegister();

        } else {
            // TODO Login
            if (LogFlag.bLogOn)Log.d(TAG, "Facebook user  found. login now");
            //vcarFacebookLogin(checkUserResponse.getUserId());
            vcarFacebookLogin();

        }
        //  loginType = "FacebookApp";
    }



    @UiThread
    void vcarFacebookLogin() {
        if (LogFlag.bLogOn)Log.d(TAG, "vcarFacebookLogin");
        facebookLogInProcessFinish();
    }


    @UiThread
    public void facebookLogInProcessFinish() {
        if (LogFlag.bLogOn)Log.d(TAG, "facebookLogInProcessFinish");
    /*    if(null != signInResponse) {
            if (LogFlag.bLogOn)Log.d(TAG,signInResponse.toString());
        }*/
        setFacebookSignInRequestData();
        //if (signInResponse.isSuccess()) {
        if (LogFlag.bLogOn)Log.d(TAG, "Facebook Login Success");


        goToHome();

        //}

    }

    void setFacebookSignInRequestData() {

        if (LogFlag.bLogOn)Log.d(TAG, "setFacebookSignInRequestData");
        String uuid = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        if (LogFlag.bLogOn)Log.d(TAG, "uuid: " + uuid);

        signInResponse = new SignInResponse();
        signInResponse.setDevicePlatformName("Android");
        signInResponse.setDeviceIdentity(uuid);
        signInResponse.setUserId(checkUserResponse.getUserId());
        signInResponse.setUserDisplayName(checkUserResponse.getUserDisplayName());
        signInResponse.setUserProfileImage(facebookUserprofile.getFacebookImage());
        signInResponse.setEmail(facebookUserprofile.getEmail());
        signInResponse.setGoogleStore(checkUserResponse.getGoogleStore());
        signInResponse.setLoginType(LoginType.FacebookApp.name());

/*        signInRequest = new SignInRequest();
        signInRequest.setDevicePlatformName("Android");
        signInRequest.setDeviceIdentity(uuid);
        signInRequest.setDeviceToken(VPreferences.get("gcmToken"));
        signInRequest.setUserId(facebookProfile.getId());
        signInRequest.setPassword("");
        signInRequest.setClientEncryptedPassword(true);
        signInRequest.setDeviceInfoData(getDeviceInfo());*/

    }


    @Background
    void facebookSocialRegister() {
        if (LogFlag.bLogOn)Log.d(TAG, "facebookSocialRegister");
        setFacebookRegisterRequestData();

        VCarRestClient vCarRestClient = new VCarRestClient();
        vCarRestClient.setRegisterParams(registerRequest);

        SignupResponse signupResponse = vCarRestClient.signup();

        // TODO check signinResponse is null or not
        if (null != signupResponse) {
            if (LogFlag.bLogOn)Log.d(TAG, "signupResponse: " + signupResponse.toString());
            signupProcessFinish(signupResponse, LoginType.FacebookApp);
        } else {
            hideLoderGifImage();
            showToastErrorMsg("Registration failed");
        }

    }

    void setFacebookRegisterRequestData() {
        if (LogFlag.bLogOn)Log.d(TAG, "setFacebookRegisterRequestData");
        PackageInfo pInfo = null;
        String version = "";
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            if (LogFlag.bLogOn)Log.e(TAG, e.getMessage());
        }

        String uuid = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        if (LogFlag.bLogOn)Log.d(TAG, "version: " + version);
        if (LogFlag.bLogOn)Log.d(TAG, "uuid: " + uuid);

        String encryptedPassword = "";
        if (LogFlag.bLogOn)Log.d(TAG, "encryptedPassword: " + encryptedPassword);
        registerRequest = new SignupRequest();
        registerRequest.setUserId(facebookUserprofile.getToken_for_business());
        registerRequest.setDevicePlatformName("Android");
        registerRequest.setDeviceIdentity(uuid);
        registerRequest.setDeviceToken(VPreferences.get("gcmToken"));
        registerRequest.setPassword(encryptedPassword);
        registerRequest.setClientEncryptedPassword(true);
        registerRequest.setAppVersion(version);
        registerRequest.setEmail(facebookUserprofile.getEmail());
        registerRequest.setLoginType("FacebookApp");
        registerRequest.setFbTokenId(facebookUserprofile.getToken_for_business());
        registerRequest.setLoginId(null);
        registerRequest.setProfileImage(facebookUserprofile.getFacebookImage());
        registerRequest.setFname(facebookUserprofile.getFirst_name());
        registerRequest.setLname(facebookUserprofile.getLast_name());
        registerRequest.setCountry(getVcarCountryCode());
        registerRequest.setDeviceInfoData(getDeviceInfo());
        String sLocation = VPreferences.get("CurrentLocation");
        vLocation = VTools.getInstance().getLocationData(sLocation);
        if(vLocation !=null) {
            if (LogFlag.bLogOn)Log.d(TAG, vLocation.toString());
            registerRequest.setState(vLocation.getState());
            registerRequest.setCity(vLocation.getLocation());
        }

        if (LogFlag.bLogOn)Log.d(TAG ,"Facebook User-->"+ registerRequest.toString());
    }

    @UiThread
    public void signupProcessFinish(SignupResponse registerResponse,LoginType loginType) {
        if (LogFlag.bLogOn)Log.d(TAG, "signupProcessFinish: ");
        if (registerResponse.isSuccess()) {
            signInResponse = new SignInResponse();
            signInResponse.setUserProfileImage(registerResponse.getProfileImage());
            signInResponse.setEmail(registerResponse.getEmail());
            signInResponse.setGoogleStore(registerResponse.getGoogleStore());
            signInResponse.setUserId(registerResponse.getUserId());
            signInResponse.setUserDisplayName(registerResponse.getUserDisplayName());
            signInResponse.setLoginType(loginType.toString());

            if (LogFlag.bLogOn)Log.d(TAG, "signInResponse: " + signInResponse.toString());

            goToHome();
        } else {
            loaderGif.setVisibility(View.GONE);
            showToastErrorMsg(registerResponse.getError());
        }

    }

    public DeviceInfoData getDeviceInfo() {
        if (LogFlag.bLogOn)Log.d(TAG, "getDeviceInfo");
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            if (LogFlag.bLogOn)Log.e(TAG, e.getMessage());
        }


        DeviceInfoData deviceInfoData = new DeviceInfoData();

        String version = pInfo.versionName;
        String model = Build.DEVICE;
        String name = Build.MANUFACTURER;
        String OSVersion = Build.VERSION.SDK;


        if (LogFlag.bLogOn)Log.d(TAG, "version: " + version);
        String sLocation = VPreferences.get("CurrentLocation");
        vLocation = VTools.getInstance().getLocationData(sLocation);
        if (vLocation != null) {
            if (LogFlag.bLogOn)Log.d(TAG, vLocation.toString());
            deviceInfoData.setLatitude(Double.toString(vLocation.getLatitude()));
            deviceInfoData.setLongitude(Double.toString(vLocation.getLongitude()));
        }

        deviceInfoData.setVersion(version);
        deviceInfoData.setPlatform("Android");
        deviceInfoData.setModel(model);
        deviceInfoData.setName(name);
        return deviceInfoData;

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if (LogFlag.bLogOn)Log.d(TAG, "onConnectionFailed: "+connectionResult.toString());
        if (LogFlag.bLogOn)Log.d(TAG, "onConnectionFailed: "+connectionResult.getErrorMessage());
        if (!mIsResolving && mShouldResolve) {
            if (connectionResult.hasResolution()) {
                try {
                    // !!!
                    connectionResult.startResolutionForResult(this, RC_SIGN_IN);
                    // startActivityForResult(this.getIntent(), RC_SIGN_IN);
                    mIsResolving = true;
                } catch (IntentSender.SendIntentException e) {
                    if (LogFlag.bLogOn)Log.e(TAG, "Could not resolve ConnectionResult.", e);
                    mIsResolving = false;
                    mGoogleApiClient.connect();
                }
            }
        }
    }

    private void goGoogleLogin() {
        // To do Google Login
        mShouldResolve = true;
        mGoogleApiClient.connect();
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {

                    }
                });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (LogFlag.bLogOn)Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" + data);

        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (LogFlag.bLogOn)Log.d(TAG,"getSignInAccount: "+result.toString());
            if (LogFlag.bLogOn)Log.d(TAG,"getSignInAccount: "+result.isSuccess());
            if (LogFlag.bLogOn)Log.d(TAG,"getSignInAccount: "+result.getStatus().getStatusMessage());
            if (LogFlag.bLogOn)Log.d(TAG,"handleSignInResult: "+result.getStatus());

            // If the error resolution was not successful we should not resolve further.
            if (resultCode != RESULT_OK) {
                mShouldResolve = false;
            }
            handleGoogleSignInResult(result);

        }
    }

    private void handleGoogleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            googleSignInAccount = result.getSignInAccount();

            mIsResolving = false;
            mGoogleApiClient.connect();

        } else {
            if (LogFlag.bLogOn)Log.d(TAG,"handleSignInResult not success ");
            loaderGif.setVisibility(View.GONE);
            if (LogFlag.bLogOn)Log.d(TAG, "google signin error");
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) == PackageManager.PERMISSION_GRANTED) {
            if (LogFlag.bLogOn)Log.d(TAG, "Signed in with google");
            googleSignInSuccess();
        } else {
            VTools.showToast("Unable to access Google account. Please change app setting to grant the permission");
            loaderGif.setVisibility(View.GONE);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        try {
            if (LogFlag.bLogOn)Log.d(TAG, "onRequestPermissionsResult:" + requestCode);
            if (requestCode == RC_PERM_GET_ACCOUNTS) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    googleSignInSuccess();
                } else {
                    if (LogFlag.bLogOn)Log.d(TAG, "GET_ACCOUNTS Permission Denied.");
                }
            } else if (requestCode == RC_PERM_GET_LOCATION) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getCountryCodetxt();
                    getBestLocation();
                } else {
                    if (LogFlag.bLogOn)Log.d(TAG, "GET_LOCATION Permission Denied.");
                }
            }
        }catch (Exception e){
            if (LogFlag.bLogOn)Log.e(TAG,"Error onRequestPermissionsResult",e);
        }
    }


    public void googleSignInSuccess() {
        if (LogFlag.bLogOn)Log.d(TAG, "googleSignInSuccess");
        if (mGoogleApiClient.isConnected()) {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.GET_ACCOUNTS) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            if (googleSignInAccount != null) {

                if (LogFlag.bLogOn)Log.d(TAG,"getDisplayName: "+ googleSignInAccount.getDisplayName());
                if (LogFlag.bLogOn)Log.d(TAG,"getEmail: "+ googleSignInAccount.getEmail());
                if (LogFlag.bLogOn)Log.d(TAG,"getPhotoUrl: "+ googleSignInAccount.getPhotoUrl());

                googleUserExists();
            } else {
                loaderGif.setVisibility(View.GONE);
                if (LogFlag.bLogOn)Log.d(TAG, "google signin error");
                mGoogleApiClient.disconnect();
            }
        }else{
            loaderGif.setVisibility(View.GONE);
        }
    }

    @Background
    public void googleUserExists() {
        if (LogFlag.bLogOn)Log.d(TAG, "googleUserExists");
        setCheckGoogleUserRequestData();
        VCarRestClient vCarRestClient = new VCarRestClient();
        checkUserResponse = vCarRestClient.checkStudent(checkUserRequest);
        if (checkUserResponse != null) {
            if (LogFlag.bLogOn)Log.d(TAG, checkUserResponse.toString());
            checkGoogleUserProcessFinish();
        }
    }

    public void setCheckGoogleUserRequestData() {
        if (LogFlag.bLogOn)Log.d(TAG, "setCheckGoogleUserRequestData");
        String uuid = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        checkUserRequest = new CheckUserRequest();
        checkUserRequest.setUserID(googleSignInAccount.getId().toString());
        checkUserRequest.setDeviceIdentity(uuid);
        checkUserRequest.setDevicePlatformName("Android");
        checkUserRequest.setLoginType("GoogleApp");
        checkUserRequest.setFbTokenId("");
        checkUserRequest.setEmail(googleEmail);
        checkUserRequest.setCountry(getVcarCountryCode());
        checkUserRequest.setProfilePic(googleSignInAccount.getPhotoUrl().toString());
        checkUserRequest.setDeviceToken(VPreferences.get("gcmToken"));
        if (LogFlag.bLogOn)Log.d(TAG, checkUserRequest.toString());
    }


    @UiThread
    public void checkGoogleUserProcessFinish() {
        if (LogFlag.bLogOn)Log.d(TAG, "checkGoogleUserProcessFinish");
        if (LogFlag.bLogOn)Log.d(TAG, checkUserResponse.toString());
        if (checkUserResponse.getExists().equalsIgnoreCase("false")) {
            // register
            Log.d(TAG, "Google user not found. need to register");
            googleSocialRegister();

        } else {
            // TODO Login
            if (LogFlag.bLogOn)Log.d(TAG, "Google user  found. login now");
            vcarGoogleLogin();

        }
        // loginType = "GoogleApp";
    }

    @UiThread
    void vcarGoogleLogin() {
        if (LogFlag.bLogOn)Log.d(TAG, "vcarGoogleLogin");
        setGoogleSignInRequestData();
        googleLogInProcessFinish();

    }


    @Background
    void googleSocialRegister() {
        if (LogFlag.bLogOn)Log.d(TAG, "googleSocialRegister");
        setGoogleRegisterRequestData();

        VCarRestClient oustRestClient = new VCarRestClient();
        oustRestClient.setRegisterParams(registerRequest);

        SignupResponse signupResponse = oustRestClient.signup();

        if (null != signupResponse) {
            // TODO check signinResponse is null or not
            if (LogFlag.bLogOn)Log.d(TAG, "signupResponse: " + signupResponse.toString());
            signupProcessFinish(signupResponse,LoginType.GoogleApp);

        } else {
            hideLoderGifImage();
            showToastErrorMsg("Registration failed");
        }
    }


    void setGoogleRegisterRequestData() {
        if (LogFlag.bLogOn)Log.d(TAG, "setGoogleRegisterRequestData");
        PackageInfo pInfo = null;
        try {
            pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            if (LogFlag.bLogOn)Log.e(TAG, e.getMessage(), e);
        }
        String version = pInfo.versionName;
        String uuid = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        if (LogFlag.bLogOn)Log.d(TAG, "version: " + version);
        if (LogFlag.bLogOn)Log.d(TAG, "uuid: " + uuid);



        String encryptedPassword = "";
        if (LogFlag.bLogOn)Log.d(TAG, "encryptedPassword: " + encryptedPassword);
        registerRequest = new SignupRequest();
        registerRequest.setUserId(googleSignInAccount.getId().toString());
        registerRequest.setDevicePlatformName("Android");
        registerRequest.setDeviceIdentity(uuid);
        registerRequest.setDeviceToken(VPreferences.get("gcmToken"));
        registerRequest.setPassword(encryptedPassword);
        registerRequest.setClientEncryptedPassword(true);
        registerRequest.setAppVersion(version);
        registerRequest.setLoginType("GoogleApp");
        registerRequest.setEmail(googleEmail);
        registerRequest.setFbTokenId(null);
        registerRequest.setLoginId(googleSignInAccount.getId());
        registerRequest.setProfileImage(googleSignInAccount.getPhotoUrl().toString());
        registerRequest.setFname(googleSignInAccount.getGivenName().toString());
        registerRequest.setLname(googleSignInAccount.getFamilyName().toString());
        registerRequest.setCountry(getVcarCountryCode());
        registerRequest.setDeviceInfoData(getDeviceInfo());

        String sLocation = VPreferences.get("CurrentLocation");
        vLocation = VTools.getInstance().getLocationData(sLocation);
        if(vLocation !=null) {
            if (LogFlag.bLogOn)Log.d(TAG, vLocation.toString());
            registerRequest.setState(vLocation.getState());
            registerRequest.setCity(vLocation.getLocation());
        }

        if (LogFlag.bLogOn)Log.d(TAG, registerRequest.toString());
    }


    void setGoogleSignInRequestData() {

        if (LogFlag.bLogOn)Log.d(TAG, "setGoogleSignInRequestData");
        String uuid = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        if (LogFlag.bLogOn)Log.d(TAG, "uuid: " + uuid);

        signInResponse = new SignInResponse();
        signInResponse.setDevicePlatformName("Android");
        signInResponse.setDeviceIdentity(uuid);
        signInResponse.setUserId(checkUserResponse.getUserId());
        signInResponse.setUserDisplayName(checkUserResponse.getUserDisplayName());
        signInResponse.setUserProfileImage(googleSignInAccount.getPhotoUrl().toString());
        signInResponse.setEmail(googleEmail);
        signInResponse.setGoogleStore(checkUserResponse.getGoogleStore());
        signInResponse.setLoginType(LoginType.GoogleApp.name());

    }

    @UiThread
    public void googleLogInProcessFinish() {
        if (LogFlag.bLogOn)Log.d(TAG, "googleLogInProcessFinish");
        if (null != signInResponse) {
            if (LogFlag.bLogOn)Log.d(TAG, signInResponse.toString());
        }
        if (LogFlag.bLogOn)Log.d(TAG, "Login Success");

        goToHome();

    }


    @Override
    public void onStop() {
        super.onStop();
        if (LogFlag.bLogOn)Log.d(TAG, "onStop");
        mGoogleApiClient.disconnect();
        if (LogFlag.bLogOn)Log.d(TAG, "isConnected: " + mGoogleApiClient.isConnected());
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopLocationUpdates();
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, (LocationListener) this);
        if (LogFlag.bLogOn)Log.d(TAG, "Location update stopped ");
    }

}

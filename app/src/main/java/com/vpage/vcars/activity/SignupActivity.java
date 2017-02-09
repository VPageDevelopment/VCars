package com.vpage.vcars.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.picasso.Picasso;
import com.vpage.vcars.R;
import com.vpage.vcars.adapter.GridImageAdapter;
import com.vpage.vcars.httputils.VCarRestClient;
import com.vpage.vcars.pojos.ValidationStatus;
import com.vpage.vcars.pojos.request.CheckUserRequest;
import com.vpage.vcars.pojos.request.SignupRequest;
import com.vpage.vcars.pojos.response.CheckUserResponse;
import com.vpage.vcars.pojos.response.SignInResponse;
import com.vpage.vcars.pojos.response.SignupResponse;
import com.vpage.vcars.tools.ActionEditText;
import com.vpage.vcars.tools.NetworkUtil;
import com.vpage.vcars.tools.OnNetworkChangeListener;
import com.vpage.vcars.tools.VPreferences;
import com.vpage.vcars.tools.VTools;
import com.vpage.vcars.tools.utils.CommonUtils;
import com.vpage.vcars.tools.utils.LogFlag;
import com.vpage.vcars.tools.utils.StringUtils;
import com.vpage.vcars.tools.utils.ValidationUtils;
import org.androidannotations.annotations.AfterViews;
import org.androidannotations.annotations.Background;
import org.androidannotations.annotations.Click;
import org.androidannotations.annotations.EActivity;
import org.androidannotations.annotations.FocusChange;
import org.androidannotations.annotations.Fullscreen;
import org.androidannotations.annotations.UiThread;
import org.androidannotations.annotations.ViewById;
import org.androidannotations.annotations.WindowFeature;


import java.security.NoSuchAlgorithmException;

@WindowFeature({ Window.FEATURE_NO_TITLE, Window.FEATURE_ACTION_BAR_OVERLAY })
@Fullscreen
@EActivity(R.layout.activity_signup)
public class SignupActivity extends AppCompatActivity implements   View.OnKeyListener, View.OnClickListener, OnNetworkChangeListener {

    private static final String TAG = SignupActivity.class.getName();


    @ViewById(R.id.chooseProfile)
    ImageButton chooseProfile;

    @ViewById(R.id.userPhoneNumber)
    EditText userPhoneNumber;

    @ViewById(R.id.passWord)
    EditText passWord;

    @ViewById(R.id.confirmPassWord)
    EditText confirmPassWord;

    @ViewById(R.id.userName)
    EditText userNameEditText;

    @ViewById(R.id.displayName)
    EditText displayName;

    @ViewById(R.id.address)
    EditText address;

    @ViewById(R.id.license)
    EditText licenseNumber;

    @ViewById(R.id.createBtn)
    Button createBtn;

    @ViewById(R.id.alreadyAccountLayout)
    RelativeLayout alreadyAccountLayout;

    @ViewById(R.id.signInLayout)
    RelativeLayout signInLayout;

    @ViewById(R.id.errorText)
    TextView errorText;

    @ViewById(R.id.signInText)
    TextView signInText;

    @ViewById(R.id.txtChooseAvatar)
    TextView txtChooseAvatar;

    @ViewById(R.id.tabanim_toolbar)
    Toolbar toolbar;

    @ViewById(R.id.title)
    TextView title;

    private PopupWindow showPopUp;

    GridImageAdapter gridImageAdapter;
    TypedArray typedArrayImage;
    int typedArrayImagePosition = -1;

    String userPhoneNumberInput= "",passWordInput= "",conformPasswordInput= "",userDisplayName= ""
            ,useName= "",userAddress= "",drivingLicenseNumber= "",userPhoneNumberEntered;

    SignupRequest registerRequest;
    Intent intent;

    boolean isNetworkAvailable=false;


    CheckUserRequest checkUserRequest;

    CheckUserResponse checkUserResponse;

    SignInResponse signInResponse;


    @AfterViews
    public void initSignUp() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        title.setText(R.string.signUpTitle);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Intent callingIntent=getIntent();

        userPhoneNumberEntered = callingIntent.getStringExtra("UserPhoneNumber");

        typedArrayImage = getResources().obtainTypedArray(R.array.avatarImage);

        if (null != signInText) {
            StringUtils.makeTextViewHyperlink(signInText);
        }

        if (null == VTools.getChosenProfileImage() || VTools.getChosenProfileImage().equals("")) {
        //    chooseProfile.setImageResource(R.drawable.avatar_1);
            Picasso.with(SignupActivity.this)
                    .load(R.drawable.usersample)
                    .into(chooseProfile);


        } else {

            if (LogFlag.bLogOn)Log.d(TAG, String.valueOf(VTools.getChosenProfileImage()));
            for (int i = 0; i < typedArrayImage.length(); i++) {
                if (LogFlag.bLogOn)
                    Log.d(TAG, "typedArrayImage: " + typedArrayImage.getString(i).replace("res/drawable/", ""));
                if (typedArrayImage.getString(i).contains(VTools.getChosenProfileImage())) {
                    typedArrayImagePosition = i;
                }
            }
            Drawable drawable = typedArrayImage.getDrawable(typedArrayImagePosition);
            chooseProfile.setImageDrawable(drawable);
        }


        setSignUpFontStyle();


        // get phone number
        TelephonyManager tMgr = (TelephonyManager)getApplicationContext().getSystemService(getApplicationContext().TELEPHONY_SERVICE);
        String mPhoneNumber = tMgr.getLine1Number();
        if (LogFlag.bLogOn)Log.d(TAG, "Phone number : " + mPhoneNumber);
        userPhoneNumber.setText(mPhoneNumber);


        licenseNumber.setOnKeyListener(this);

        signInText.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                gotoSignInPage();
            }
        });

        new ActionEditText(this);

        TextWatcher textWatcher = new TextWatcher() {
            public void afterTextChanged(Editable s) {
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // you can check for enter key here
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 150) {
                    VTools.showAlertDialog(SignupActivity.this,"Character Exceed the Limit");
                }

            }
        };

        address.addTextChangedListener(textWatcher);

    }

    private void gotoSignInPage() {

        Intent intent = new Intent(getApplicationContext(), SigninActivity_.class);
        startActivity(intent);
        VTools.animation(this);
        finish();
    }


    private void gotoHomePage() {

        Gson gson = new GsonBuilder().create();
        Intent intent = new Intent(SignupActivity.this, HomeActivity_.class);
        intent.putExtra("ActiveUser", gson.toJson(VTools.getInstance().getActiveUser(signInResponse)));
        startActivity(intent);
        VTools.animation(this);
        finish();
    }




    @Click({R.id.chooseProfile, R.id.createBtn, R.id.signInLayout,R.id.phoneInfoButton,R.id.passWordInfoButton,R.id.cpassWordInfoButton,R.id.displayNameInfoButton,
            R.id.userNameInfoButton,R.id.addressInfoButton})
    public void onButtonClick(View v) {

        final View infoPopUpView =getLayoutInflater().inflate(R.layout.popup_info, null); // inflating popup layout
        final ImageButton infoClose = (ImageButton) infoPopUpView.findViewById(R.id.btnClose);
        final TextView infoText = (TextView) infoPopUpView.findViewById(R.id.infoText);
        infoText.setTypeface(VTools.getAvenirLTStdRoman());

        switch (v.getId()) {

            case R.id.chooseProfile:

                final View popUpView =getLayoutInflater().inflate(R.layout.popup_chooseprofileimage, null); // inflating popup layout
                showPopUp = VTools.createPopUp(popUpView);

                final GridView avatarGrid = (GridView) popUpView.findViewById(R.id.avatarGrid);
                final Button btnConfirmChoose = (Button) popUpView.findViewById(R.id.confirmButton);
                final ImageButton btnClose = (ImageButton) popUpView.findViewById(R.id.btnClose);
                btnConfirmChoose.setOnClickListener(this);
                btnClose.setOnClickListener(this);

                setGridViewItem(avatarGrid);
                View layoutSignup = (Button) findViewById(R.id.createBtn);
                layoutSignup.setAlpha(0);
                break;

            case R.id.phoneInfoButton:
                showPopUp = VTools.createPopUp(infoPopUpView);
                infoClose.setOnClickListener(this);
                infoText.setText("Should be integer" +"\nShould have 10 digits");
                break;

            case R.id.passWordInfoButton:
                showPopUp = VTools.createPopUp(infoPopUpView);
                infoClose.setOnClickListener(this);
                infoText.setText("MIN 6 and MAX 20 characters"+"\nAccepts integer, text and special characters");
                break;

            case R.id.cpassWordInfoButton:
                showPopUp = VTools.createPopUp(infoPopUpView);
                infoClose.setOnClickListener(this);
                infoText.setText("Should match with password");
                break;

            case R.id.userNameInfoButton:
                showPopUp = VTools.createPopUp(infoPopUpView);
                infoClose.setOnClickListener(this);
                infoText.setText("Starts with alphabet"+"\nCan have numbers, dash, underscore"+"\nMIN 3 and MAX 20 characters");
                break;

            case R.id.displayNameInfoButton:
                showPopUp = VTools.createPopUp(infoPopUpView);
                infoClose.setOnClickListener(this);
                infoText.setText("Starts with alphabet"+"\nCan have numbers, dash, underscore"+"\nMIN 3 and MAX 20 characters");
                break;

            case R.id.addressInfoButton:
                showPopUp = VTools.createPopUp(infoPopUpView);
                infoClose.setOnClickListener(this);
                infoText.setText("Can have numbers, dash, underscore"+"\n MAX 150 characters");
                break;

            case R.id.createBtn:
                registerValidation();
                break;


            case R.id.signInLayout:
                onBackPressed();
                break;

        }
    }


    private void setGridViewItem(GridView avatarGrid) {
        typedArrayImage = getResources().obtainTypedArray(R.array.avatarImage);

        if (null == VTools.getChosenProfileImage() || VTools.getChosenProfileImage().equals("")) {
            VTools.setChosenProfileImage("avatar_1.png");
        } else {

            for (int i = 0; i < typedArrayImage.length(); i++) {
                if (LogFlag.bLogOn)
                    Log.d(TAG, "typedArrayImage: " + typedArrayImage.getString(i).replace("res/drawable/", ""));
                if (typedArrayImage.getString(i).contains(VTools.getChosenProfileImage())) {
                    typedArrayImagePosition = i;
                }
            }

        }

        gridImageAdapter = new GridImageAdapter(SignupActivity.this, typedArrayImage);
        avatarGrid.setAdapter(gridImageAdapter);
        if (typedArrayImagePosition == -1) {
            gridImageAdapter.setSelectedPosition(0);
        }else {
            gridImageAdapter.setSelectedPosition(typedArrayImagePosition);
        }


        avatarGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                gridImageAdapter.notifyDataSetChanged();
                gridImageAdapter.setSelectedPosition(i);

                if (LogFlag.bLogOn)Log.d(TAG, typedArrayImage.getString(i).replace("res/drawable/", ""));

                VTools.setChosenProfileImage(typedArrayImage.getString(i).replace("res/drawable/", ""));
                if (LogFlag.bLogOn)Log.d(TAG, "getChosenProfileImage: " + String.valueOf(VTools.getChosenProfileImage()));
            }
        });


    }

    void registerValidation() {

        networkStatus();
        if (!isNetworkAvailable) {
            return;
        }
        hideKeyboard();
        if (LogFlag.bLogOn)Log.d(TAG, "registerValidation");
        ValidationStatus validationStatus,validationStatusPhoneNumber,validationStatusPassword,
                validationStatusUserName,validationStatusUserDisplayName;


       /* userPhoneNumber.setOnKeyListener(this);
        passWord.setOnKeyListener(this);
        confirmPassWord.setOnKeyListener(this);*/
       // confirmPassWord.setOnKeyListener(this);
        userPhoneNumber.setText(userPhoneNumberEntered);
        userPhoneNumberInput = userPhoneNumber.getText().toString();

        passWordInput = passWord.getText().toString();
        conformPasswordInput = confirmPassWord.getText().toString();
        userDisplayName = displayName.getText().toString();
        useName = userNameEditText.getText().toString();
        userAddress = address.getText().toString();
        drivingLicenseNumber = licenseNumber.getText().toString();


        validationStatus = ValidationUtils.isValidLoginUserNamePassword(userPhoneNumberInput,userDisplayName, passWordInput);

        validationStatusPhoneNumber =  ValidationUtils.isValidUserPhoneNumber(userPhoneNumberInput);

        validationStatusPassword =  ValidationUtils.isValidPassword(passWordInput);

        validationStatusUserName =  ValidationUtils.isValidUserUserName(useName);
        validationStatusUserDisplayName =  ValidationUtils.isValidUserUserName(userDisplayName);



        if (validationStatus.isStatus() == false) {
            if (LogFlag.bLogOn)Log.d(TAG, validationStatus.getMessage());
            setErrorMessage(validationStatus.getMessage());
            return;
        }

        if (validationStatusPhoneNumber.isStatus() == false) {
            if (LogFlag.bLogOn)Log.d(TAG, validationStatusPhoneNumber.getMessage());
            setErrorMessage(validationStatusPhoneNumber.getMessage());
            return;
        }

        if (validationStatusPassword.isStatus() == false) {
            if (LogFlag.bLogOn)Log.d(TAG, validationStatusPassword.getMessage());
            setErrorMessage(validationStatusPassword.getMessage());
            return;
        }

        if (validationStatusUserName.isStatus() == false) {
            if (LogFlag.bLogOn)Log.d(TAG, validationStatusUserName.getMessage());
            setErrorMessage(validationStatusUserName.getMessage());
            return;
        }


        if (validationStatusUserDisplayName.isStatus() == false) {
            if (LogFlag.bLogOn)Log.d(TAG, validationStatusUserDisplayName.getMessage());
            setErrorMessage(validationStatusUserDisplayName.getMessage());
            return;
        }

        if (userPhoneNumber.equals("") || passWord.equals("") ||  useName.equals("")||  displayName.equals("") || confirmPassWord.equals("") ) {
            if (LogFlag.bLogOn)Log.d(TAG, getResources().getString(R.string.nullMessage));
            setErrorMessage( getResources().getString(R.string.nullMessage));
            return;
        }
        if (!passWordInput.equals(conformPasswordInput)) {
            if (LogFlag.bLogOn)Log.d(TAG,"Password no correct"+ String.valueOf(R.string.passwordMessage));
            setErrorMessage(getResources().getString(R.string.passwordMessage));
            return;
        }
        Log.d(TAG, "registerValidation done");
       // isUserExists();   // to be used when developing

        gotoHomePage();   // For Testing alone
    }


    @Background
    public void isUserExists() {
        if (LogFlag.bLogOn)Log.d(TAG, "isUserExists");

        setCheckUserRequestData();

        VCarRestClient vCarRestClient = new VCarRestClient();
        checkUserResponse = vCarRestClient.checkStudent(checkUserRequest);
        if(checkUserResponse != null) {
            if (LogFlag.bLogOn)Log.d(TAG, checkUserResponse.toString());
            checkUserProcessFinish();
        }

    }


    @UiThread
    public void checkUserProcessFinish() {
        if (LogFlag.bLogOn)Log.d(TAG, "checkUserProcessFinish");
        if (LogFlag.bLogOn)Log.d(TAG, checkUserResponse.toString());
        if (checkUserResponse.getExists().equalsIgnoreCase("false")) {
            // register
            if (LogFlag.bLogOn)Log.d(TAG, " user not found. need to register");
            oustRegister();

        } else {
            // TODO Login
            if (LogFlag.bLogOn)Log.d(TAG, " user  found. show error");
            if (LogFlag.bLogOn)Log.d(TAG, checkUserResponse.getError());
            setErrorMessage("Phone no has already been registered");
            gotoSignInPage();

        }
    }



    public void setCheckUserRequestData() {
        if (LogFlag.bLogOn)Log.d(TAG, "setCheckUserRequestData");
        String uuid = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        checkUserRequest = new CheckUserRequest();
        checkUserRequest.setStudentid(userPhoneNumberInput);
        checkUserRequest.setDeviceIdentity(uuid);
        checkUserRequest.setDevicePlatformName("Android");
        checkUserRequest.setLoginType("VCars");
        checkUserRequest.setDeviceToken(VPreferences.get("gcmToken"));
        if (LogFlag.bLogOn)Log.d(TAG, checkUserRequest.toString());
    }




    @Background
    void oustRegister() {

        setRegisterRequestData();

        VCarRestClient vCarRestClient = new VCarRestClient();
        vCarRestClient.setRegisterParams(registerRequest);

        SignupResponse signupResponse = vCarRestClient.signup();

        if(null !=signupResponse ){
            if (LogFlag.bLogOn)Log.d(TAG, "signupResponse: " + signupResponse.toString());
            signupProcessFinish(signupResponse);
        }

    }

    void setRegisterRequestData() {
        if (LogFlag.bLogOn)Log.d(TAG, "setRegisterRequestData");
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

        String encryptedPassword= "";
        try {
            encryptedPassword = CommonUtils.getMD5EncodedString(passWordInput);
        } catch (NoSuchAlgorithmException e) {
            if (LogFlag.bLogOn)Log.e(TAG, e.toString());
        }
        if (LogFlag.bLogOn)Log.d(TAG, "encryptedPassword: " + encryptedPassword);
        registerRequest = new SignupRequest();
        registerRequest.setDevicePlatformName("Android");
        registerRequest.setDeviceIdentity(uuid);
        registerRequest.setUserId(userPhoneNumberInput);
        registerRequest.setFname(userDisplayName);
        registerRequest.setPassword(encryptedPassword);
        registerRequest.setClientEncryptedPassword(true);
        registerRequest.setAppVersion(version);
        if (null == VTools.getChosenProfileImage() || VTools.getChosenProfileImage().equals("")) {
            registerRequest.setProfileImage("avatar-1.png");
            VTools.setChosenProfileImage("avatar_1.png");
        } else {
            registerRequest.setProfileImage(VTools.getChosenProfileImage().replace("_", "-"));
        }
        if (LogFlag.bLogOn)Log.d(TAG, registerRequest.toString());
    }


    @UiThread
    public void signupProcessFinish(SignupResponse registerResponse) {

        if (registerResponse.isSuccess()) {
            signInResponse = new SignInResponse();
            signInResponse.setUserProfileImage(registerResponse.getProfileImage());
            signInResponse.setEmail(registerResponse.getEmail());
            signInResponse.setGoogleStore(registerResponse.getGoogleStore());
            signInResponse.setUserId(registerResponse.getUserId());
            signInResponse.setUserDisplayName(registerResponse.getUserDisplayName());


            if (LogFlag.bLogOn)Log.d(TAG, "signInResponse: " + signInResponse.toString());

           gotoHomePage();
        } else {
            setErrorMessage(registerResponse.getError());
        }

    }

    void setErrorMessage(String errorMessage) {
        errorText.setVisibility(View.VISIBLE);
        errorText.setText(errorMessage);
    }

    @FocusChange({R.id.userPhoneNumber, R.id.passWord, R.id.confirmPassWord, R.id.displayName,R.id.userName,R.id.address,R.id.license})
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            errorText.setVisibility(View.GONE);
        } else {

        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode == EditorInfo.IME_ACTION_GO ||
                keyCode == EditorInfo.IME_ACTION_DONE ||
                event.getAction() == KeyEvent.ACTION_DOWN &&
                        event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            registerValidation();
        }

        return false;
    }

    @Override
    public void onClick(View view) {
        View layoutSignup = (Button) findViewById(R.id.createBtn);
        switch (view.getId()){
            case R.id.confirmButton:
                if (LogFlag.bLogOn)Log.d(TAG, String.valueOf(VTools.getChosenProfileImage()));
                showPopUp.dismiss();
                layoutSignup.setAlpha(220);
                for (int i = 0; i < typedArrayImage.length(); i++) {
                    if (typedArrayImage.getString(i).contains(VTools.getChosenProfileImage())) {
                        typedArrayImagePosition = i;
                    }
                }

                Drawable drawable = typedArrayImage.getDrawable(typedArrayImagePosition);
                chooseProfile.setImageDrawable(drawable);
                break;
            case R.id.btnClose:
                showPopUp.dismiss();

                layoutSignup.setAlpha(220);
                break;

        }

    }

    @Override
    public void onChange(String status) {
        if (LogFlag.bLogOn)Log.d(TAG, "Network Availability");
        if (LogFlag.bLogOn)Log.d(TAG, status);
        VTools.getInstance().showToast(status);
        // isNetworkAvailable= true;
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    void  setSignUpFontStyle(){


        createBtn.setTypeface(VTools.getAvenirLTStdHeavy());
        userPhoneNumber.setTypeface(VTools.getAvenirLTStdRoman());
        passWord.setTypeface(VTools.getAvenirLTStdRoman());
        confirmPassWord.setTypeface(VTools.getAvenirLTStdRoman());
        displayName.setTypeface(VTools.getAvenirLTStdRoman());
        errorText.setTypeface(VTools.getAvenirLTStdRoman());
        signInText.setTypeface(VTools.getAvenirLTStdRoman());
        txtChooseAvatar.setTypeface(VTools.getAvenirLTStdHeavy());
    }

    private void hideKeyboard(){
        InputMethodManager inputManager = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

}

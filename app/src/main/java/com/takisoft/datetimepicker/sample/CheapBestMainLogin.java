package com.takisoft.datetimepicker.sample;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.takisoft.datetimepicker.sample.apputilss.Colors;
import com.takisoft.datetimepicker.sample.apputilss.MyImageLoader;
import com.takisoft.datetimepicker.sample.apputilss.SharedPref;
import com.takisoft.datetimepicker.sample.network.IResult;
import com.takisoft.datetimepicker.sample.network.NetworkURLs;
import com.takisoft.datetimepicker.sample.network.VolleyService;
import com.takisoft.datetimepicker.sample.ui.Activity.CheapBestMain;
import com.takisoft.datetimepicker.sample.ui.Activity.MainDashBoard;
import com.takisoft.datetimepicker.sample.ui.Fragments.CheapBestMainLoginFragment;
import com.takisoft.datetimepicker.sample.ui.Fragments.ForgotPassword;
import com.takisoft.datetimepicker.sample.ui.Fragments.SignUpSelect;
import org.json.JSONException;
import org.json.JSONObject;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import spinkit.style.ChasingDots;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.takisoft.datetimepicker.sample.ui.signup.ForgotResetCodeFrag;
import com.takisoft.datetimepicker.sample.ui.signup.SetPasswordFragment;
import com.takisoft.datetimepicker.sample.ui.utills.GPSTracker;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CheapBestMainLogin extends FragmentActivity implements Colors,
        ForgotPassword.OnItemSelectedListener,
        CheapBestMainLoginFragment.OnItemSelectedListener,
        SignUpSelect.OnItemSelectedListener,
        ForgotResetCodeFrag.OnItemSelectedListener,
        SetPasswordFragment.OnItemSelectedListener {


    Button BtnSignUp,BtnLogin;
    ImageView imageViewSingUp,imageViewLogin;
    RelativeLayout relativeLayoutMain,layoutForgotPassword;
    GPSTracker gpsTracker;
    public static Map<String, String> SignInData;
    public Map<String, String> FBData;
    VolleyService mVolleyService;
    IResult mResultCallback;
    LinearLayout layoutMain;
    String UserID;
    String response_status;
    public static String UserEmail,UserPass;
    private ImageView imageViewLoading;
    private ChasingDots mChasingDotsDrawable;
    private MyImageLoader myImageLoader;
    public static String FbName,FbEmail,FbUID,FbGender,FbDob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        SharedPref.init(getApplicationContext());

        setContentView(R.layout.cheap_best_main_login_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, CheapBestMainLoginFragment.newInstance())
                    .commitNow();
        }
        inintthisactivity();


    }

    @SuppressLint("NewApi")
    private void inintthisactivity() {
        requestLocationPermission();
        myImageLoader=new MyImageLoader(CheapBestMainLogin.this);
        layoutMain=findViewById(R.id.layout_main_login);
            /*loading animation initlization*/
        imageViewLoading=  findViewById(R.id.image_loading);
        mChasingDotsDrawable = new ChasingDots();
        mChasingDotsDrawable.setColor(getColor(R.color.color_custom));
        imageViewLoading.setImageDrawable(mChasingDotsDrawable);
        imageViewLoading.setVisibility(View.GONE);
        /////////////////////////////////////
        gpsTracker=new GPSTracker(CheapBestMainLogin.this);

      //  Toast.makeText(this, String.valueOf(gpsTracker.getLatitude()+","+gpsTracker.getLongitude()), Toast.LENGTH_SHORT).show();

        BtnLogin=findViewById(R.id.btnloginfrag);
        BtnSignUp=findViewById(R.id.btnSignUpSelect);
        BtnLogin.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 27.f);
        BtnSignUp.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18.f);
        imageViewLogin=findViewById(R.id.img_below_login);
        imageViewSingUp=findViewById(R.id.img_below_singup);
        relativeLayoutMain=findViewById(R.id.layout_login_signup);
        layoutForgotPassword=findViewById(R.id.layout_forgotpassword);

        imageViewSingUp.setVisibility(View.GONE);
        BtnLogin.setOnClickListener(view -> {
            imageViewSingUp.setVisibility(View.GONE);
            imageViewLogin.setVisibility(View.VISIBLE);
            BtnLogin.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 27.f);
            BtnSignUp.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18.f);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, CheapBestMainLoginFragment.newInstance())
                    .commitNow();
        });

        BtnSignUp.setOnClickListener(view -> {
            imageViewSingUp.setVisibility(View.VISIBLE);
             BtnSignUp.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 27.f);
            BtnLogin.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18.f);
             imageViewLogin.setVisibility(View.GONE);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SignUpSelect.newInstance())
                    .commitNow();
        });
    }

    @Override
    public void onLoginFragCallBack(int position) {

        if(position==1){
            relativeLayoutMain.setVisibility(View.GONE);
            layoutForgotPassword.setVisibility(View.VISIBLE);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ForgotPassword.newInstance())
                    .commitNow();
        }else if(position==2){

            SignInMethod();

        }else if(position==3){
            String Str=FbName+","+FbEmail+","+FbUID+","+FbDob+","+FbGender;
            Toast.makeText(CheapBestMainLogin.this, Str, Toast.LENGTH_LONG).show();

            FBData = new HashMap< >();
            FBData.put("user[email]",FbEmail);
            MainDashBoard.responseUid=FbEmail;
            FBData.put("user[name]",FbName);
            FBData.put("user[provider]","facebook");
            FBData.put("user[uid]",FbUID);
            FBData.put("user[birthday]",FbDob);
            FBData.put("user[gender]",FbGender);
            SignUpUsingFacebookMethod();

        }
    }

    @Override
    public void onForgotFragCallBack(int position) {

        if(position==1){
            relativeLayoutMain.setVisibility(View.VISIBLE);
            layoutForgotPassword.setVisibility(View.GONE);
            imageViewSingUp.setVisibility(View.GONE);
            imageViewLogin.setVisibility(View.VISIBLE);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, CheapBestMainLoginFragment.newInstance())
                    .commitNow();
        }else {
            relativeLayoutMain.setVisibility(View.GONE);
            layoutForgotPassword.setVisibility(View.GONE);
            imageViewSingUp.setVisibility(View.GONE);
            imageViewLogin.setVisibility(View.GONE);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, ForgotResetCodeFrag.newInstance())
                    .commitNow();
        }

    }
    @Override
    public void onBackPressed() {
        Fragment f =getSupportFragmentManager().findFragmentById(R.id.container);
        if(f instanceof CheapBestMainLoginFragment){

             Toast.makeText(this, "Your are Login Fragment", Toast.LENGTH_SHORT).show();

        }else if(f instanceof SignUpSelect){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, CheapBestMainLoginFragment.newInstance())
                    .commitNow();

        }else if(f instanceof ForgotPassword){
            relativeLayoutMain.setVisibility(View.VISIBLE);
            layoutForgotPassword.setVisibility(View.GONE);
            imageViewSingUp.setVisibility(View.GONE);
            imageViewLogin.setVisibility(View.VISIBLE);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, CheapBestMainLoginFragment.newInstance())
                    .commitNow();
        }else {
            Toast.makeText(this, "Main Menu ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSingUpFragCallBack(int position) {
        Toast.makeText(this, "Your in signup Fragment", Toast.LENGTH_SHORT).show();
        if(position==1){
            CheapBestMain.runtimefrag=1;
            Intent intent_next=new Intent(CheapBestMainLogin.this,CheapBestMain.class);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
            startActivity(intent_next);
            finish();

           /* Intent intent_next=new Intent(CheapBestMainLogin.this,CheapBestMain.class);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
            startActivity(intent_next);
            finish();*/
        }else if(position==2){
            CheapBestMain.runtimefrag=0;
            /*Intent intent_next=new Intent(CheapBestMainLogin.this,CheapBestMain.class);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
            startActivity(intent_next);
            finish();*/
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://cheapestbest.nltsf.com/vendors/sign_up")));

        }

    }

    /*volley for sign using email*/

    void SignInMethod()
    {
        mChasingDotsDrawable.start();
        imageViewLoading.setVisibility(View.VISIBLE);
        initVolleyCallbackForSignUp();
        mVolleyService = new VolleyService(mResultCallback,CheapBestMainLogin.this);
        mVolleyService.postDataVolley("POSTCALL",NetworkURLs.BaseURL+NetworkURLs.SignInURL,SignInData);
    }

    void initVolleyCallbackForSignUp(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {
                mChasingDotsDrawable.stop();
                imageViewLoading.setVisibility(View.GONE);
                if (response != null) {
                     try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                            Toast.makeText(CheapBestMainLogin.this, "status found true", Toast.LENGTH_SHORT).show();

                            JSONObject signUpResponseModel = jsonObject.getJSONObject("data");
                            UserID= signUpResponseModel.getString("id");
                            response_status="true";
                            SharedPref.write(SharedPref.IsLoginUser,"true");

                        }else {
                            response_status="false";
                            JSONObject signUpResponseModels = jsonObject.getJSONObject("error");
                            UserID= signUpResponseModels.getString("message");
                            Toast.makeText(CheapBestMainLogin.this, String.valueOf(UserID), Toast.LENGTH_SHORT).show();
                            myImageLoader.showErroDialog(UserID);

                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if(!response_status.equalsIgnoreCase("false")){


                    MainDashBoard.loadALLData=true;
                    SharedPref.write(SharedPref.UserEmail, UserEmail);
                    SharedPref.write(SharedPref.FBLogin, "true");
                    SharedPref.write(SharedPref.User_ID, UserID);
                    SharedPref.write(SharedPref.UserPassword, UserPass);
                    Toast.makeText(CheapBestMainLogin.this, "Login Succssfully", Toast.LENGTH_SHORT).show();
                    Intent Send=new Intent(CheapBestMainLogin.this,MainDashBoard.class);
                    startActivity(Send);
                    finish();
                   /* AccountVerificationFrag.newUserverify=true;

                    SharedPref.write(SharedPref.User_ID, UserID);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, AccountVerificationFrag.newInstance())
                            .commitNow();*/

                }
            }

            @Override
            public void notifyError(String requestType,VolleyError error) {
                mChasingDotsDrawable.stop();
                imageViewLoading.setVisibility(View.GONE);

                if(error.networkResponse != null && error.networkResponse.data != null){
                    //VolleyError error2 = new VolleyError(new String(error.networkResponse.data));
                    String error_response=new String(error.networkResponse.data);
                    Toast.makeText(CheapBestMainLogin.this, String.valueOf(error_response), Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject response_obj=new JSONObject(error_response);

                        {
                            JSONObject error_obj=response_obj.getJSONObject("error");
                            String message=error_obj.getString("message");

                            Toast.makeText(CheapBestMainLogin.this, message, Toast.LENGTH_SHORT).show();

                            myImageLoader.showErroDialog(String.valueOf(message));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }


        };
    }

    /* sign up using Facebook*/

    void SignUpUsingFacebookMethod()
    {
        mChasingDotsDrawable.start();
        imageViewLoading.setVisibility(View.VISIBLE);
        initVolleyCallbackForSignUpFacebook();
        mVolleyService = new VolleyService(mResultCallback,CheapBestMainLogin.this);
        mVolleyService.postDataVolley("POSTCALL",NetworkURLs.FacebookLoginUrl,FBData);
    }

    void initVolleyCallbackForSignUpFacebook(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {
                mChasingDotsDrawable.stop();
                imageViewLoading.setVisibility(View.GONE);

                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                            Toast.makeText(CheapBestMainLogin.this, "status found true", Toast.LENGTH_SHORT).show();

                            JSONObject signUpResponseModel = jsonObject.getJSONObject("data");
                            UserID= signUpResponseModel.getString("id");
                            response_status="true";
                            SharedPref.write(SharedPref.IsLoginUser,"true");
                        }else {
                            response_status="false";
                            JSONObject signUpResponseModels = jsonObject.getJSONObject("error");
                            UserID= signUpResponseModels.getString("message");

                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                if(response_status.equalsIgnoreCase("false")){

                    myImageLoader.showErroDialog("Something went wrong while sign up using facebook");

                    //Toast.makeText(CheapBestMainLogin.this, "Error occurer", Toast.LENGTH_SHORT).show();
                }else {
                    MainDashBoard.loadALLData=true;
                    SharedPref.write(SharedPref.UserEmail, UserEmail);
                    SharedPref.write(SharedPref.FBLogin,"true");
                    SharedPref.write(SharedPref.User_ID, UserID);
                   // SharedPref.write(SharedPref.UserPassword, UserPass);
                    Toast.makeText(CheapBestMainLogin.this, "Login Succssfully here", Toast.LENGTH_SHORT).show();
                    Intent Send=new Intent(CheapBestMainLogin.this,MainDashBoard.class);
                    startActivity(Send);
                    finish();


                }
            }

            @Override
            public void notifyError(String requestType,VolleyError error) {
                mChasingDotsDrawable.stop();
                imageViewLoading.setVisibility(View.GONE);
                myImageLoader.showErroDialog(error.getMessage());
            }
        };
    }

    private void requestLocationPermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.INTERNET)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                       //     Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(error -> Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show())
                .onSameThread()
                .check();
    }
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(CheapBestMainLogin.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", (dialog, which) -> {
            dialog.cancel();
            openSettings();
        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();

    }

    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }
    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        mChasingDotsDrawable.stop();
    }

    @Override
    public void onResetCodeFragCallBack(int position) {
        if(position==1){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, SetPasswordFragment.newInstance())
                    .commitNow();
        }else if(position==2){
            relativeLayoutMain.setVisibility(View.VISIBLE);
            layoutForgotPassword.setVisibility(View.GONE);
            imageViewSingUp.setVisibility(View.GONE);
            imageViewLogin.setVisibility(View.VISIBLE);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, CheapBestMainLoginFragment.newInstance())
                    .commitNow();
        }

    }

    @Override
    public void onPassFragCallBack(int position) {
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        CheapBestMainLoginFragment.callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

}

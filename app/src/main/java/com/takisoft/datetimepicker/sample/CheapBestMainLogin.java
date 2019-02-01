package com.takisoft.datetimepicker.sample;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
import com.takisoft.datetimepicker.sample.apputills.Colors;
import com.takisoft.datetimepicker.sample.apputills.DialogHelper;
import com.takisoft.datetimepicker.sample.apputills.Progressbar;
import com.takisoft.datetimepicker.sample.apputills.SharedPref;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.takisoft.datetimepicker.sample.ui.Fragments.signup.ForgotResetCodeFrag;
import com.takisoft.datetimepicker.sample.ui.Fragments.signup.SetPasswordFragment;
import com.takisoft.datetimepicker.sample.apputills.GPSTracker;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CheapBestMainLogin extends FragmentActivity implements Colors,
        ForgotPassword.OnItemSelectedListener,
        CheapBestMainLoginFragment.OnItemSelectedListener,
        SignUpSelect.OnItemSelectedListener,
        ForgotResetCodeFrag.OnItemSelectedListener,
        SetPasswordFragment.OnItemSelectedListener {

    boolean doubleBackToExitPressedOnce = false;
    Button BtnSignUp,BtnLogin;
    ImageView imageViewSingUp,imageViewLogin;
    RelativeLayout relativeLayoutMain,layoutForgotPassword;
    GPSTracker gpsTracker;
    public static Map<String, String> SignInData;
    public Map<String, String> FBData;
    VolleyService mVolleyService;
    IResult mResultCallback;
    LinearLayout layoutMain;
    String UserID,Message;
    public static String UserEmail,UserPass;

    private DialogHelper dialogHelper;
    public static String FbName,FbEmail,FbUID,FbGender,FbDob;
    RelativeLayout relativeLayoutMainHedaer;
    Progressbar progressbar;
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

        progressbar =new Progressbar(CheapBestMainLogin.this);

        relativeLayoutMainHedaer=findViewById(R.id.layout_main_login_xml);
         dialogHelper=new DialogHelper(CheapBestMainLogin.this);
        layoutMain=findViewById(R.id.layout_main_login);
        gpsTracker=new GPSTracker(CheapBestMainLogin.this);
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
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                doubleBackToExitPressedOnce=false;
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            //Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(() -> doubleBackToExitPressedOnce=false, 2000);

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

        if(position==1){
            CheapBestMain.runtimefrag=1;
            Intent intent_next=new Intent(CheapBestMainLogin.this,CheapBestMain.class);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
            startActivity(intent_next);
            finish();
        }else if(position==2){
            CheapBestMain.runtimefrag=0;

            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://cheapestbest.nltsf.com/vendors/sign_up")));

        }
    }

    /*volley for sign using email*/

    void SignInMethod()
    {

        showprogress();
        initVolleyCallbackForSignUp();
        mVolleyService = new VolleyService(mResultCallback,CheapBestMainLogin.this);
        mVolleyService.postDataVolley("POSTCALL",NetworkURLs.BaseURL+NetworkURLs.SignInURL,SignInData);
    }

    void initVolleyCallbackForSignUp(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {

                hideprogress();
                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equalsIgnoreCase("true")) {


                            JSONObject signUpResponseModel = jsonObject.getJSONObject("data");
                            UserID = signUpResponseModel.getString("id");


                            SharedPref.writeBol(SharedPref.IsLoginUser,true);
                            SharedPref.write(SharedPref.UserEmail, UserEmail);
                            SharedPref.writeBol(SharedPref.FBLogin, true);
                            SharedPref.write(SharedPref.User_ID, UserID);
                            SharedPref.write(SharedPref.UserPassword, UserPass);

                            Intent Send=new Intent(CheapBestMainLogin.this,MainDashBoard.class);
                            startActivity(Send);
                            finish();

                        }else {

                            JSONObject signUpResponseModels = jsonObject.getJSONObject("error");
                            Message = signUpResponseModels.getString("message");
                            dialogHelper.showErroDialog(Message);

                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void notifyError(String requestType,VolleyError error) {

                hideprogress();
                if(error.networkResponse != null && error.networkResponse.data != null){
                    //VolleyError error2 = new VolleyError(new String(error.networkResponse.data));
                    String error_response=new String(error.networkResponse.data);
                    Toast.makeText(CheapBestMainLogin.this, String.valueOf(error_response), Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject response_obj=new JSONObject(error_response);

                        {
                            JSONObject error_obj=response_obj.getJSONObject("error");
                            String message=error_obj.getString("message");
                            dialogHelper.showErroDialog(String.valueOf(message));
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

        showprogress();
        initVolleyCallbackForSignUpFacebook();
        mVolleyService = new VolleyService(mResultCallback,CheapBestMainLogin.this);
        mVolleyService.postDataVolley("POSTCALL",NetworkURLs.FacebookLoginUrl,FBData);
    }

    void initVolleyCallbackForSignUpFacebook(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {

                hideprogress();

                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equalsIgnoreCase("true")) {

                            JSONObject signUpResponseModel = jsonObject.getJSONObject("data");
                            UserID= signUpResponseModel.getString("id");
                            SharedPref.writeBol(SharedPref.IsLoginUser,true);
                        }else {

                            JSONObject signUpResponseModels = jsonObject.getJSONObject("error");
                            String ErrorMessage= signUpResponseModels.getString("message");
                            dialogHelper.showErroDialog(ErrorMessage);

                        }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void notifyError(String requestType,VolleyError error) {

                hideprogress();
                dialogHelper.showErroDialog(error.getMessage());
                if(error.networkResponse != null && error.networkResponse.data != null){
                    //VolleyError error2 = new VolleyError(new String(error.networkResponse.data));
                    String error_response=new String(error.networkResponse.data);
                    try {
                        JSONObject response_obj=new JSONObject(error_response);

                        {
                            JSONObject error_obj=response_obj.getJSONObject("error");
                            String message=error_obj.getString("message");

                            dialogHelper.showErroDialog(message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
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
                            Log.e("","");
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



    public void showprogress(){

        progressbar.ShowProgress();
        progressbar.setCancelable(false);

    }

    public void hideprogress(){
        progressbar.HideProgress();

    }


    private void showsnackmessage(String msg){

        Snackbar snackbar = Snackbar
                .make(relativeLayoutMainHedaer, msg, Snackbar.LENGTH_LONG);

        snackbar.show();
    }
}

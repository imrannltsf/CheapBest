package com.cheapestbest.androidapp;

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
import com.cheapestbest.androidapp.apputills.FirebaseHelper;
import com.facebook.AccessToken;
import com.facebook.login.LoginManager;
import com.google.android.material.snackbar.Snackbar;
import com.cheapestbest.androidapp.apputills.Colors;
import com.cheapestbest.androidapp.apputills.DialogHelper;
import com.cheapestbest.androidapp.apputills.Progressbar;
import com.cheapestbest.androidapp.apputills.SharedPref;
import com.cheapestbest.androidapp.network.IResult;
import com.cheapestbest.androidapp.network.NetworkURLs;
import com.cheapestbest.androidapp.network.VolleyService;
import com.cheapestbest.androidapp.ui.Activity.CheapBestMain;
import com.cheapestbest.androidapp.ui.Activity.MainDashBoard;
import com.cheapestbest.androidapp.ui.Fragments.CheapBestMainLoginFragment;
import com.cheapestbest.androidapp.ui.Fragments.ForgotPassword;
import com.cheapestbest.androidapp.ui.Fragments.SignUpSelect;
import org.json.JSONException;
import org.json.JSONObject;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.cheapestbest.androidapp.ui.Fragments.signup.ForgotResetCodeFrag;
import com.cheapestbest.androidapp.ui.Fragments.signup.SetPasswordFragment;
import com.cheapestbest.androidapp.apputills.GPSTracker;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CheapBestMainLogin extends FragmentActivity implements Colors,
        ForgotPassword.OnItemSelectedListener,
        CheapBestMainLoginFragment.OnItemSelectedListener,
        SignUpSelect.OnItemSelectedListener,
        ForgotResetCodeFrag.OnItemSelectedListener,
        SetPasswordFragment.OnItemSelectedListener {
    private AccessToken accessToken;
    private boolean isLoggedIn;
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
    private FirebaseAnalytics firebaseAnalytics;
    private DialogHelper dialogHelper;
    public static String FbName,FbEmail,FbUID;
    RelativeLayout relativeLayoutMainHedaer;
    Progressbar progressbar;
    String DeveloperName="Imran";
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

        accessToken = AccessToken.getCurrentAccessToken();
        isLoggedIn  = accessToken != null && !accessToken.isExpired();
        SharedPref.write(SharedPref.FBLogin,"false");
        LoginManager.getInstance().logOut();
        requestLocationPermission();
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        FirebaseHelper food = new FirebaseHelper();
        food.setId(1);
        // choose random food name from the list
        food.setName("Imran");
        Bundle bundle = new Bundle();
        bundle.putInt(FirebaseAnalytics.Param.ITEM_ID, food.getId());
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, food.getName());
        //Logs an app event.
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        //Sets whether analytics collection is enabled for this app on this device.
        firebaseAnalytics.setAnalyticsCollectionEnabled(true);
        //Sets the minimum engagement time required before starting a session. The default value is 10000 (10 seconds). Let's make it 20 seconds just for the fun
        firebaseAnalytics.setMinimumSessionDuration(20000);
        //Sets the duration of inactivity that terminates the current session. The default value is 1800000 (30 minutes).
        firebaseAnalytics.setSessionTimeoutDuration(500);
        //Sets the user ID property.
        firebaseAnalytics.setUserId(String.valueOf(food.getId()));
        //Sets a user property to a given value.
        firebaseAnalytics.setUserProperty("FirebaseHelper", food.getName());

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
            BtnSignUp.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20.f);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, CheapBestMainLoginFragment.newInstance())
                    .commitNow();
        });

        BtnSignUp.setOnClickListener(view -> {
            imageViewSingUp.setVisibility(View.VISIBLE);
            BtnSignUp.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 27.f);
            BtnLogin.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20.f);
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
            FBData.put("user[provider]","facebook");
            FBData.put("user[email]",FbEmail);

            FBData.put("user[name]",FbName);
            FBData.put("user[uid]",FbUID);
            SignUpUsingFacebookMethod();


           /* FBData.put("user[provider]","facebook");
            FBData.put("user[uid]",FbUID);
            FBData.put("user[birthday]",FbDob);
            FBData.put("user[gender]",FbGender);*/
        }else {
            Intent Send=new Intent(CheapBestMainLogin.this,MainDashBoard.class);
            startActivity(Send);
            finish();
            //finish();
           // onBackPressed();
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
        if(f instanceof CheapBestMainLoginFragment || f instanceof ForgotResetCodeFrag){
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                doubleBackToExitPressedOnce=false;
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

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
           // Toast.makeText(this, "Main Menu ", Toast.LENGTH_SHORT).show();
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

            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://dashboard.cheapestbest.com/vendors/sign_up")));

        }else if(position==3){

            Intent intent_next=new Intent(CheapBestMainLogin.this,MainDashBoard.class);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
            startActivity(intent_next);
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
                   // dialogHelper.showErroDialog(String.valueOf(error_response));
                 //   Toast.makeText(CheapBestMainLogin.this, String.valueOf(error_response), Toast.LENGTH_SHORT).show();
                    try {
                        JSONObject response_obj=new JSONObject(error_response);
                       // dialogHelper.showErroDialog(String.valueOf(response_obj));
                        {
                            JSONObject error_obj=response_obj.getJSONObject("error");
                            String message=error_obj.getString("message");
                            dialogHelper.showErroDialog(String.valueOf(message));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    dialogHelper.showErroDialog(String.valueOf(error));
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
                          // Toast.makeText(CheapBestMainLogin.this, String.valueOf(UserID), Toast.LENGTH_SHORT).show();
                            SharedPref.writeBol(SharedPref.IsLoginUser,true);
                            SharedPref.write(SharedPref.User_ID, UserID);
                            SharedPref.writeBol(SharedPref.FBLogin, true);
                            Intent Send=new Intent(CheapBestMainLogin.this,MainDashBoard.class);
                            startActivity(Send);
                            finish();
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

                if(error.networkResponse != null && error.networkResponse.data != null){
                    //VolleyError error2 = new VolleyError(new String(error.networkResponse.data));
                    String error_response=new String(error.networkResponse.data);
                //    dialogHelper.showErroDialog(error_response);
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

                }else {
                    dialogHelper.showErroDialog("Something went wrong please try again");
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
                            showsnackmessage("Please allow tracking to see offers near you today!");
                          //  showSettingsDialog();
                        }
                    }


                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                        showsnackmessage("Please allow tracking to see offers near you today!");
                        showSettingsDialog();
                    }
                }).
                withErrorListener(error ->
                        Toast.makeText(getApplicationContext(), "Error occurred! ", Toast.LENGTH_SHORT).show()
                )
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
        if(progressbar!=null)
        progressbar.HideProgress();

    }


    private void showsnackmessage(String msg){

        Snackbar snackbar = Snackbar
                .make(relativeLayoutMainHedaer, msg, Snackbar.LENGTH_LONG);

        snackbar.show();
    }
}

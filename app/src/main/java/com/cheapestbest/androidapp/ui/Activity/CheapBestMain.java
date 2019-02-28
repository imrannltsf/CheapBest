package com.cheapestbest.androidapp.ui.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import com.android.volley.VolleyError;
import com.cheapestbest.androidapp.CheapBestMainLogin;
import com.cheapestbest.androidapp.R;
import com.cheapestbest.androidapp.apputills.DialogHelper;
import com.cheapestbest.androidapp.apputills.FirebaseHelper;
import com.cheapestbest.androidapp.apputills.MyImageLoader;
import com.cheapestbest.androidapp.apputills.Progressbar;
import com.cheapestbest.androidapp.apputills.SharedPref;
import com.cheapestbest.androidapp.ui.Fragments.SupscriptionFragment;
import com.cheapestbest.androidapp.ui.Fragments.signup.AccountVerificationFrag;
import com.cheapestbest.androidapp.ui.Fragments.signup.SignUpFragment;
import com.cheapestbest.androidapp.network.IResult;
import com.cheapestbest.androidapp.network.NetworkURLs;
import com.cheapestbest.androidapp.network.VolleyService;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.Map;
import androidx.fragment.app.FragmentActivity;
import cn.pedant.SweetAlert.SweetAlertDialog;

public class CheapBestMain extends FragmentActivity implements
        SignUpFragment.OnItemSelectedListener,
        AccountVerificationFrag.OnItemSelectedListener{
    private FirebaseAnalytics firebaseAnalytics;
    Progressbar progressbar;
    IResult mResultCallback;
    VolleyService mVolleyService;
    public static int runtimefrag=0;
    private MyImageLoader myImageLoader;
    private DialogHelper dialogHelper;
    public static Map<String, String>SignUpData;
    FrameLayout frameLayout;
    String UserID;
    String response_status;
    public static String NewUserEmail;

    @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            setContentView(R.layout.cheap_best_main_activity);
                if(runtimefrag==1){
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, SignUpFragment.newInstance())
                            .commitNow();
                }else if (savedInstanceState == null||runtimefrag==0) {

                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, SupscriptionFragment.newInstance())
                            .commitNow();
                }
                 initthis();
        }

    private void initthis() {
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
        progressbar =new Progressbar(CheapBestMain.this);
        myImageLoader=new MyImageLoader(CheapBestMain.this);
        dialogHelper=new DialogHelper(CheapBestMain.this);
        frameLayout=findViewById(R.id.container);
        SharedPref.init(getApplicationContext());
}


    @Override
    public void onBackPressed() {
        Intent home_intent = new Intent(CheapBestMain.this, CheapBestMainLogin.class);
        overridePendingTransition(R.anim.animation_leave, R.anim.animation_enter);
        startActivity(home_intent);
        finish();
    }

    @Override
    public void onSignUpFragCallBack(int position) {
        if(position==1){
            Intent intent_next=new Intent(CheapBestMain.this,CheapBestMainLogin.class);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_right);
            startActivity(intent_next);
            finish();
        }else if(position==2){
            SignUPMethod();
        }
    }
    /*volley*/

    void SignUPMethod()
    {
        showprogress();
        initVolleyCallbackForSignUp();
        mVolleyService = new VolleyService(mResultCallback,CheapBestMain.this);
        mVolleyService.postDataVolley("POSTCALL",NetworkURLs.BaseURL+NetworkURLs.SignUPURL,SignUpData);
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
                            UserID= signUpResponseModel.getString("id");
                            response_status="true";

                            AccountVerificationFrag.newUserverify=true;
                            SharedPref.write(SharedPref.UserEmail, NewUserEmail);
                            SharedPref.write(SharedPref.User_ID, UserID);

                          /*  new SweetAlertDialog(CheapBestMain.this, SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Success!")
                                    .setContentText(StrMessage)
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                            listener.onForgotFragCallBack(2);
                                        }
                                    })
                                    .show();*/
                            getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.container, AccountVerificationFrag.newInstance())
                                    .commitNow();


                        }else {
                            response_status="false";
                            JSONObject signUpResponseModels = jsonObject.getJSONObject("error");
                           String ErrorMessage= signUpResponseModels.getString("message");
                            dialogHelper.showDialog(ErrorMessage);

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

                }else{
                    dialogHelper.showErroDialog("Something went wrong please try again");
                }

            }
        };
    }

    @Override
    public void onAccountVerfyFragCallBack(int position) {

    }


    public void showprogress(){

        progressbar.ShowProgress();
        progressbar.setCancelable(false);

    }

    public void hideprogress(){
        progressbar.HideProgress();

    }

}

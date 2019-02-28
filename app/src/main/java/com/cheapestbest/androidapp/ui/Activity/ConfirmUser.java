package com.cheapestbest.androidapp.ui.Activity;

import androidx.appcompat.app.AppCompatActivity;
import cn.pedant.SweetAlert.SweetAlertDialog;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.cheapestbest.androidapp.R;
import com.cheapestbest.androidapp.apputills.DialogHelper;
import com.cheapestbest.androidapp.apputills.FirebaseHelper;
import com.cheapestbest.androidapp.apputills.MyImageLoader;
import com.cheapestbest.androidapp.apputills.Progressbar;
import com.cheapestbest.androidapp.apputills.SharedPref;
import com.cheapestbest.androidapp.network.IResult;
import com.cheapestbest.androidapp.network.NetworkURLs;
import com.cheapestbest.androidapp.network.VolleyService;
import com.google.firebase.analytics.FirebaseAnalytics;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Map;

public class ConfirmUser extends AppCompatActivity {
    private MyImageLoader myImageLoader;
    Progressbar progressbar;
    private DialogHelper dialogHelper;
    Button buttonConfirm;
    private FirebaseAnalytics firebaseAnalytics;

    String UserID;
    String response_status;
    @SuppressLint("NewApi")
    VolleyService mVolleyService;
    IResult mResultCallback;
    @SuppressLint("NewApi")
    public  Map<String, String> ConfirmCodeValue;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_user);
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
        progressbar =new Progressbar(ConfirmUser.this);
        myImageLoader=new MyImageLoader(ConfirmUser.this);
        dialogHelper=new DialogHelper(ConfirmUser.this);
        buttonConfirm=findViewById(R.id.btn_submit_code);
        buttonConfirm.setOnClickListener(view -> {
           /* String ss=editTextCode.getText().toString();
            if(!TextUtils.isEmpty(ss)){
                ConfirmCodeValue = new HashMap< >();
                ConfirmCodeValue.put("code",ss);
                AccountVerifyMethod();

            }else {
                Toast.makeText(this, "Please Enter Code:", Toast.LENGTH_SHORT).show();
            }*/
        });

      /*  textViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Send=new Intent(ConfirmUser.this,CheapBestMainLogin.class);
                startActivity(Send);
                finish();
            }
        });*/
    }


    /*volley*/

    void AccountVerifyMethod()
    {

       showprogress();
        initVolleyCallbackForSignUp();
        mVolleyService = new VolleyService(mResultCallback,ConfirmUser.this);
        mVolleyService.postDataVolleyHeader("POSTCALL",NetworkURLs.BaseURL+NetworkURLs.VerifyNewUserURL,ConfirmCodeValue);
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
                           // Toast.makeText(ConfirmUser.this, "status found true", Toast.LENGTH_SHORT).show();

                            JSONObject signUpResponseModel = jsonObject.getJSONObject("data");
                            UserID= signUpResponseModel.getString("id");
                            response_status="true";

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

                    dialogHelper.showErroDialog(UserID);

                 //   Toast.makeText(ConfirmUser.this, "Error occurer status false", Toast.LENGTH_SHORT).show();
                }else {

                  /*  SharedPref.write(SharedPref.User_ID, UserID);
                    Toast.makeText(ConfirmUser.this, "User Confirmed Succssfully", Toast.LENGTH_SHORT).show();
                    Intent Send=new Intent(ConfirmUser.this,SetPasswordUser.class);
                    startActivity(Send);
                    finish();*/

                    new SweetAlertDialog(ConfirmUser.this, SweetAlertDialog.SUCCESS_TYPE)
                            .setTitleText("Success!")
                            .setContentText("User Verified Successfully")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismissWithAnimation();
                                    SharedPref.write(SharedPref.User_ID, UserID);
                                    Intent Send=new Intent(ConfirmUser.this,SetPasswordUser.class);
                                    startActivity(Send);
                                    finish();
                                }
                            })
                            .show();
                }
            }

            @Override
            public void notifyError(String requestType,VolleyError error) {

              hideprogress();
                if(error.networkResponse != null && error.networkResponse.data != null){
                    //VolleyError error2 = new VolleyError(new String(error.networkResponse.data));
                    String error_response=new String(error.networkResponse.data);
                    try {
                        JSONObject response_obj=new JSONObject(error_response);

                        {
                            JSONObject error_obj=response_obj.getJSONObject("error");
                            String message=error_obj.getString("message");

                           // Toast.makeText(ConfirmUser.this, message, Toast.LENGTH_SHORT).show();

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


    public void showprogress(){

        progressbar.ShowProgress();
        progressbar.setCancelable(false);

    }

    public void hideprogress(){
        progressbar.HideProgress();

    }
}

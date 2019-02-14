package com.cheapestbest.androidapp.ui.Activity;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.cheapestbest.androidapp.apputills.FirebaseHelper;
import com.google.android.material.snackbar.Snackbar;
import com.cheapestbest.androidapp.CheapBestMainLogin;
import com.cheapestbest.androidapp.R;
import com.cheapestbest.androidapp.apputills.DialogHelper;
import com.cheapestbest.androidapp.apputills.MyImageLoader;
import com.cheapestbest.androidapp.apputills.Progressbar;
import com.cheapestbest.androidapp.apputills.SharedPref;
import com.cheapestbest.androidapp.network.IResult;
import com.cheapestbest.androidapp.network.NetworkURLs;
import com.cheapestbest.androidapp.network.VolleyService;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class SetPasswordUser extends AppCompatActivity {
    Progressbar progressbar;
    private MyImageLoader myImageLoader;
    EditText editTextCode;
    Button buttonConfirm;
    String UserID;
    String response_status;
    @SuppressLint("NewApi")
    VolleyService mVolleyService;
    IResult mResultCallback;
    @SuppressLint("NewApi")
    public static Map<String, String> ConfirmPass;
    @SuppressLint("NewApi")
    LinearLayout layoutXml;
    private DialogHelper dialogHelper;
    private FirebaseAnalytics firebaseAnalytics;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password_user);
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
        dialogHelper=new DialogHelper(SetPasswordUser.this);
        myImageLoader=new MyImageLoader(SetPasswordUser.this);
        editTextCode=findViewById(R.id.et_setpassword);
        buttonConfirm=findViewById(R.id.btn_submit_password);
        progressbar =new Progressbar(SetPasswordUser.this);
        layoutXml=findViewById(R.id.layout_set_pass);
        buttonConfirm.setOnClickListener(view -> {
            String ss=editTextCode.getText().toString();
            if(!TextUtils.isEmpty(ss)){
                ConfirmPass = new HashMap< >();
                ConfirmPass.put("password",ss);
                ConfirmPass.put("password_confirmation",ss);

                PutMethod();

            }else {
                showsnackmessage("Please Enter Password:");

            }
        });
    }

    /*volley*/

    void PutMethod()
    {

       showprogress();
        initVolleyCallbackForSignUp();
        mVolleyService = new VolleyService(mResultCallback,SetPasswordUser.this);
        mVolleyService.PutReqquestVolley("PUTCALL",NetworkURLs.BaseURL+NetworkURLs.SetPasswordUserURL,ConfirmPass);
    }

    void initVolleyCallbackForSignUp(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {

                showprogress();
                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equalsIgnoreCase("true")) {
                            Toast.makeText(SetPasswordUser.this, "status found true", Toast.LENGTH_SHORT).show();
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

                    Toast.makeText(SetPasswordUser.this, UserID, Toast.LENGTH_SHORT).show();
                }else {

                    SharedPref.write(SharedPref.User_ID, UserID);
                    Toast.makeText(SetPasswordUser.this, "Password Reset Succssfully", Toast.LENGTH_SHORT).show();
                    Intent Send=new Intent(SetPasswordUser.this,CheapBestMainLogin.class);
                    startActivity(Send);
                    finish();
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
                            //Toast.makeText(SetPasswordUser.this, message, Toast.LENGTH_SHORT).show();
                            dialogHelper.showErroDialog(message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

    private void showsnackmessage(String msg){

        Snackbar snackbar = Snackbar
                .make(layoutXml, msg, Snackbar.LENGTH_LONG);

        snackbar.show();
    }
}

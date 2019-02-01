package com.takisoft.datetimepicker.sample.ui.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import com.android.volley.VolleyError;
import com.takisoft.datetimepicker.sample.CheapBestMainLogin;
import com.takisoft.datetimepicker.sample.R;
import com.takisoft.datetimepicker.sample.apputills.DialogHelper;
import com.takisoft.datetimepicker.sample.apputills.MyImageLoader;
import com.takisoft.datetimepicker.sample.apputills.Progressbar;
import com.takisoft.datetimepicker.sample.apputills.SharedPref;
import com.takisoft.datetimepicker.sample.ui.Fragments.SupscriptionFragment;
import com.takisoft.datetimepicker.sample.ui.Fragments.signup.AccountVerificationFrag;
import com.takisoft.datetimepicker.sample.ui.Fragments.signup.SignUpFragment;
import com.takisoft.datetimepicker.sample.network.IResult;
import com.takisoft.datetimepicker.sample.network.NetworkURLs;
import com.takisoft.datetimepicker.sample.network.VolleyService;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Map;
import androidx.fragment.app.FragmentActivity;

public class CheapBestMain extends FragmentActivity implements
        SignUpFragment.OnItemSelectedListener,
        AccountVerificationFrag.OnItemSelectedListener{
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

                            SharedPref.write(SharedPref.User_ID, UserID);
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

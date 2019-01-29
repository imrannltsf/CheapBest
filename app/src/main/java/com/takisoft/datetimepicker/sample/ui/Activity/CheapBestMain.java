package com.takisoft.datetimepicker.sample.ui.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.takisoft.datetimepicker.sample.CheapBestMainLogin;
import com.takisoft.datetimepicker.sample.R;
import com.takisoft.datetimepicker.sample.apputilss.MyImageLoader;
import com.takisoft.datetimepicker.sample.apputilss.SharedPref;
import com.takisoft.datetimepicker.sample.ui.Fragments.CheapBestMainLoginFragment;
import com.takisoft.datetimepicker.sample.ui.Fragments.SupscriptionFragment;
import com.takisoft.datetimepicker.sample.ui.signup.AccountVerificationFrag;
import com.takisoft.datetimepicker.sample.ui.signup.SignUpFragment;
import com.takisoft.datetimepicker.sample.network.IResult;
import com.takisoft.datetimepicker.sample.network.NetworkURLs;
import com.takisoft.datetimepicker.sample.network.VolleyService;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.Map;
import androidx.fragment.app.FragmentActivity;
import spinkit.style.ChasingDots;

public class CheapBestMain extends FragmentActivity implements
        SignUpFragment.OnItemSelectedListener,
        AccountVerificationFrag.OnItemSelectedListener{

    IResult mResultCallback;
    VolleyService mVolleyService;
    public static int runtimefrag=0;
    private MyImageLoader myImageLoader;
    public static Map<String, String>SignUpData;
    FrameLayout frameLayout;
    String UserID;
    String response_status;
    private ImageView imageViewLoading;
    private ChasingDots mChasingDotsDrawable;

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

        myImageLoader=new MyImageLoader(CheapBestMain.this);
        mChasingDotsDrawable = new ChasingDots();
        mChasingDotsDrawable.setColor(getResources().getColor(R.color.color_custom));
        imageViewLoading = findViewById(R.id.image_loading_signup);
        imageViewLoading.setImageDrawable(mChasingDotsDrawable);
        imageViewLoading.setVisibility(View.GONE);
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
        mChasingDotsDrawable.start();
        imageViewLoading.setVisibility(View.VISIBLE);
        initVolleyCallbackForSignUp();
        mVolleyService = new VolleyService(mResultCallback,CheapBestMain.this);
        mVolleyService.postDataVolley("POSTCALL",NetworkURLs.BaseURL+NetworkURLs.SignUPURL,SignUpData);
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
                            Toast.makeText(CheapBestMain.this, "status found true", Toast.LENGTH_SHORT).show();

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

                    myImageLoader.showErroDialog(UserID);
                }else {
                    AccountVerificationFrag.newUserverify=true;

                    SharedPref.write(SharedPref.User_ID, UserID);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.container, AccountVerificationFrag.newInstance())
                            .commitNow();

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

    @Override
    public void onAccountVerfyFragCallBack(int position) {

    }

}

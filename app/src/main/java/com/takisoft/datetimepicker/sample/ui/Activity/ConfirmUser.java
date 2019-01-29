package com.takisoft.datetimepicker.sample.ui.Activity;

import androidx.appcompat.app.AppCompatActivity;
import spinkit.style.ChasingDots;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.takisoft.datetimepicker.sample.CheapBestMainLogin;
import com.takisoft.datetimepicker.sample.R;
import com.takisoft.datetimepicker.sample.apputilss.MyImageLoader;
import com.takisoft.datetimepicker.sample.apputilss.SharedPref;
import com.takisoft.datetimepicker.sample.network.IResult;
import com.takisoft.datetimepicker.sample.network.NetworkURLs;
import com.takisoft.datetimepicker.sample.network.VolleyService;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class ConfirmUser extends AppCompatActivity {
    private MyImageLoader myImageLoader;
    EditText editTextCode;
    Button buttonConfirm;
    TextView textViewBack;
    private ImageView imageViewLoading;
    private ChasingDots mChasingDotsDrawable;
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

        textViewBack=findViewById(R.id.tv_back_confirm);
        myImageLoader=new MyImageLoader(ConfirmUser.this);
        imageViewLoading=  findViewById(R.id.image_loading_confirm);
        mChasingDotsDrawable = new ChasingDots();
        mChasingDotsDrawable.setColor(getColor(R.color.color_custom));
        imageViewLoading.setImageDrawable(mChasingDotsDrawable);
        editTextCode=findViewById(R.id.et_confirmcode);
        buttonConfirm=findViewById(R.id.btn_submit_code);
        buttonConfirm.setOnClickListener(view -> {
            String ss=editTextCode.getText().toString();
            if(!TextUtils.isEmpty(ss)){
                ConfirmCodeValue = new HashMap< >();
                ConfirmCodeValue.put("code",ss);
                AccountVerifyMethod();

            }else {
                Toast.makeText(this, "Please Enter Code:", Toast.LENGTH_SHORT).show();
            }
        });

        textViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent Send=new Intent(ConfirmUser.this,CheapBestMainLogin.class);
                startActivity(Send);
                finish();
            }
        });
    }


    /*volley*/

    void AccountVerifyMethod()
    {
        mChasingDotsDrawable.start();
        imageViewLoading.setVisibility(View.VISIBLE);
        initVolleyCallbackForSignUp();
        mVolleyService = new VolleyService(mResultCallback,ConfirmUser.this);
        mVolleyService.postDataVolleyHeader("POSTCALL",NetworkURLs.BaseURL+NetworkURLs.VerifyNewUserURL,ConfirmCodeValue);
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
                            Toast.makeText(ConfirmUser.this, "status found true", Toast.LENGTH_SHORT).show();

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

                    Toast.makeText(ConfirmUser.this, "Error occurer status false", Toast.LENGTH_SHORT).show();
                }else {

                    SharedPref.write(SharedPref.User_ID, UserID);
                    Toast.makeText(ConfirmUser.this, "User Confirmed Succssfully", Toast.LENGTH_SHORT).show();
                    Intent Send=new Intent(ConfirmUser.this,SetPasswordUser.class);
                    startActivity(Send);
                    finish();
                }
            }

            @Override
            public void notifyError(String requestType,VolleyError error) {
                mChasingDotsDrawable.stop();
                imageViewLoading.setVisibility(View.GONE);
                if(error.networkResponse != null && error.networkResponse.data != null){
                    //VolleyError error2 = new VolleyError(new String(error.networkResponse.data));
                    String error_response=new String(error.networkResponse.data);
                    try {
                        JSONObject response_obj=new JSONObject(error_response);

                        {
                            JSONObject error_obj=response_obj.getJSONObject("error");
                            String message=error_obj.getString("message");

                            Toast.makeText(ConfirmUser.this, message, Toast.LENGTH_SHORT).show();

                            myImageLoader.showErroDialog(message);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
    }
}

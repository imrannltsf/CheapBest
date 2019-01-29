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

public class SetPasswordUser extends AppCompatActivity {
    private MyImageLoader myImageLoader;
    EditText editTextCode;
    Button buttonConfirm;
    private ImageView imageViewLoading;
    private ChasingDots mChasingDotsDrawable;
    String UserID;
    String response_status;
    @SuppressLint("NewApi")
    VolleyService mVolleyService;
    IResult mResultCallback;
    @SuppressLint("NewApi")
    public static Map<String, String> ConfirmPass;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_password_user);
        myImageLoader=new MyImageLoader(SetPasswordUser.this);
        imageViewLoading=  findViewById(R.id.image_loading_password);
        mChasingDotsDrawable = new ChasingDots();
        mChasingDotsDrawable.setColor(getColor(R.color.color_custom));
        imageViewLoading.setImageDrawable(mChasingDotsDrawable);
        editTextCode=findViewById(R.id.et_setpassword);
        buttonConfirm=findViewById(R.id.btn_submit_password);


        buttonConfirm.setOnClickListener(view -> {
            String ss=editTextCode.getText().toString();
            if(!TextUtils.isEmpty(ss)){
                ConfirmPass = new HashMap< >();
                ConfirmPass.put("password",ss);
                ConfirmPass.put("password_confirmation",ss);

                PutMethod();

            }else {
                Toast.makeText(this, "Please Enter Password:", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /*volley*/

    void PutMethod()
    {
        mChasingDotsDrawable.start();
        imageViewLoading.setVisibility(View.VISIBLE);
        initVolleyCallbackForSignUp();
        mVolleyService = new VolleyService(mResultCallback,SetPasswordUser.this);
        mVolleyService.PutReqquestVolley("PUTCALL",NetworkURLs.BaseURL+NetworkURLs.SetPasswordUserURL,ConfirmPass);
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

                    myImageLoader.showErroDialog(UserID);

                    Toast.makeText(SetPasswordUser.this, UserID, Toast.LENGTH_SHORT).show();
                }else {

                    SharedPref.write(SharedPref.User_ID, UserID);
                    Toast.makeText(SetPasswordUser.this, "Reset Successfully Succssfully", Toast.LENGTH_SHORT).show();
                    Intent Send=new Intent(SetPasswordUser.this,CheapBestMainLogin.class);
                    startActivity(Send);
                    finish();
                }
            }

            @Override
            public void notifyError(String requestType,VolleyError error) {
                mChasingDotsDrawable.stop();
                imageViewLoading.setVisibility(View.GONE);
                if(error.networkResponse != null && error.networkResponse.data != null){

                    String error_response=new String(error.networkResponse.data);
                    try {
                        JSONObject response_obj=new JSONObject(error_response);
                        {
                            JSONObject error_obj=response_obj.getJSONObject("error");
                            String message=error_obj.getString("message");
                            Toast.makeText(SetPasswordUser.this, message, Toast.LENGTH_SHORT).show();
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

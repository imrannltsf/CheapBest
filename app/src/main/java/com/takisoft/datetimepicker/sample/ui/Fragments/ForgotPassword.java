package com.takisoft.datetimepicker.sample.ui.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.takisoft.datetimepicker.sample.R;
import com.takisoft.datetimepicker.sample.apputilss.MyImageLoader;
import com.takisoft.datetimepicker.sample.network.IResult;
import com.takisoft.datetimepicker.sample.network.NetworkURLs;
import com.takisoft.datetimepicker.sample.network.VolleyService;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import spinkit.style.ChasingDots;


public class ForgotPassword extends Fragment {

    private EditText etEmail;
    private MyImageLoader myImageLoader;
    private IResult mResultCallback;
    private ImageView imageViewLoading;
    private ChasingDots mChasingDotsDrawable;
    private  Map<String, String> ResetEmailData;
  //  private  Map<String, String> ResetEmailCode;
    public static ForgotPassword newInstance() {
        return new ForgotPassword();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forgotpassword, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        TextView tvBack = view.findViewById(R.id.tv_back_forgotpassword);
        Button btnForGot = view.findViewById(R.id.btn_submitemail_forgotpassword);
        etEmail=view.findViewById(R.id.et_email_forgot);
        myImageLoader=new MyImageLoader(getActivity());
        mChasingDotsDrawable = new ChasingDots();
        mChasingDotsDrawable.setColor(getResources().getColor(R.color.color_custom));
        imageViewLoading =  view.findViewById(R.id.image_loading_forgot);
        imageViewLoading.setImageDrawable(mChasingDotsDrawable);
        imageViewLoading.setVisibility(View.GONE);

        btnForGot.setOnClickListener(view12 -> {
            if(TextUtils.isEmpty(etEmail.getText().toString())){
                myImageLoader.showErroDialog("Enter Email Address To Reset Password");
            }else{
                ResetEmailData= new HashMap< >();
                ResetEmailData.put("email",etEmail.getText().toString());

                SendPasswordResetRequest();
            }
        });
        tvBack.setOnClickListener(view1 -> listener.onForgotFragCallBack(1));

      /*            *//*  SendResetCode*//*
        ResetEmailCode= new HashMap< >();
        ResetEmailCode.put("reset_password_token","51325720");

        SendResetCodeResetRequest();*/
    }


    private OnItemSelectedListener listener;


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnItemSelectedListener){      // context instanceof YourActivity
            this.listener = (OnItemSelectedListener) context; // = (YourActivity) context
        } else {
            throw new ClassCastException(context.toString()
                    + " must implement ForgotPassword.OnItemSelectedListener");
        }
    }


    // Define the events that the fragment will use to communicate
    public interface OnItemSelectedListener {
        // This can be any number of events to be sent to the activity

        void onForgotFragCallBack(int position);
    }

    /* volley*/

    private void SendPasswordResetRequest()
    {
        mChasingDotsDrawable.start();
        imageViewLoading.setVisibility(View.VISIBLE);
        initVolleyCallbackForResetPassword();
        VolleyService mVolleyService = new VolleyService(mResultCallback, getActivity());
        mVolleyService.PostReqquestVolleyWithoutHeader("POSTCALL",NetworkURLs.EmailUrlForPasswordReset,ResetEmailData);
    }

    private void initVolleyCallbackForResetPassword(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {
                // dismissporgress();
                mChasingDotsDrawable.stop();
                imageViewLoading.setVisibility(View.GONE);

                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equalsIgnoreCase("true")) {

                            JSONObject DataRecivedObj = jsonObject.getJSONObject("data");
                            String StrMessage=DataRecivedObj.getString("message");

                            myImageLoader.showDialogAlert(StrMessage);

                            listener.onForgotFragCallBack(2);

                        }else {
                            JSONObject DataRecivedObj = jsonObject.getJSONObject("error");
                            String StrMessage=DataRecivedObj.getString("message");
                            myImageLoader.showErroDialog(StrMessage);

                       }
                    }catch (JSONException e) {
                        e.printStackTrace();
                    }
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

                            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();

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

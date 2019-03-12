package com.cheapestbest.androidapp.ui.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.volley.VolleyError;
import com.cheapestbest.androidapp.apputills.SharedPref;
import com.google.android.material.snackbar.Snackbar;
import com.cheapestbest.androidapp.R;
import com.cheapestbest.androidapp.apputills.DialogHelper;
import com.cheapestbest.androidapp.apputills.Progressbar;
import com.cheapestbest.androidapp.network.IResult;
import com.cheapestbest.androidapp.network.NetworkURLs;
import com.cheapestbest.androidapp.network.VolleyService;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import cn.pedant.SweetAlert.SweetAlertDialog;


public class ForgotPassword extends Fragment {

    public static ForgotPassword newInstance() {
        return new ForgotPassword();
    }
    private OnItemSelectedListener listener;

    private DialogHelper dialogHelper;
    private LinearLayout layoutMain;
    private EditText etEmail;
     private IResult mResultCallback;
    private  Map<String, String> ResetEmailData;
    private Progressbar progressbar;
    private String StrUserEmail;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_forgotpassword, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        progressbar =new Progressbar(getActivity());
        layoutMain=view.findViewById(R.id.layout_forgot_pass);
        TextView tvBack = view.findViewById(R.id.tv_back_forgotpassword);
        Button btnForGot = view.findViewById(R.id.btn_submitemail_forgotpassword);
        etEmail=view.findViewById(R.id.et_email_forgot);
       dialogHelper=new DialogHelper(getActivity());
        btnForGot.setOnClickListener(view12 -> {
            StrUserEmail=etEmail.getText().toString();
            if(TextUtils.isEmpty(StrUserEmail)){
                showsnackmessage("Please Fill Email");

            }else{
                ResetEmailData= new HashMap< >();
                if(isEmailValid(StrUserEmail)){
                    ResetEmailData.put("email",StrUserEmail);

                    SendPasswordResetRequest();
                }else {
                    showsnackmessage("Email is not valid ");
                }

            }
        });
        tvBack.setOnClickListener(view1 -> listener.onForgotFragCallBack(1));

      /*            *//*  SendResetCode*//*
        ResetEmailCode= new HashMap< >();
        ResetEmailCode.put("reset_password_token","51325720");

        SendResetCodeResetRequest();*/
    }





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

        showprogress();
        initVolleyCallbackForResetPassword();
        VolleyService mVolleyService = new VolleyService(mResultCallback, getActivity());
        mVolleyService.PostReqquestVolleyWithoutHeader("POSTCALL",NetworkURLs.EmailUrlForPasswordReset,ResetEmailData);
    }

    private void initVolleyCallbackForResetPassword(){
        mResultCallback = new IResult() {
            @Override
            public void notifySuccess(String requestType,String response) {

               hideprogress();

                if (response != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.getString("status").equalsIgnoreCase("true")) {

                            JSONObject DataRecivedObj = jsonObject.getJSONObject("data");

                            SharedPref.write(SharedPref.UserEmail, StrUserEmail);
                            String StrMessage=DataRecivedObj.getString("message");
                           // dialogHelper.showDialogAlert(StrMessage);

                            new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Email Sent!")
                                    .setConfirmText("Reset Password")
                                    .setContentText(StrMessage)
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            sDialog.dismissWithAnimation();
                                            listener.onForgotFragCallBack(2);
                                        }
                                    })
                                    .show();
                          /*  listener.onForgotFragCallBack(2);*/

                        }else {
                            JSONObject DataRecivedObj = jsonObject.getJSONObject("error");
                            String StrMessage=DataRecivedObj.getString("message");
                            dialogHelper.showErroDialog(StrMessage);

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
               //     dialogHelper.showErroDialog(error_response);
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

    public void showprogress(){

        progressbar.ShowProgress();
        progressbar.setCancelable(false);
    }

    public void hideprogress(){
        progressbar.HideProgress();

    }
    private void showsnackmessage(String msg){

        Snackbar snackbar = Snackbar
                .make(layoutMain, msg, Snackbar.LENGTH_LONG);

        snackbar.show();
    }

    private boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}
